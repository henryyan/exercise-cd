package net.yanhl.tactics.services.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.service.FieldManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.service.PriceManager;
import net.yanhl.tactics.dao.TacticsDao;
import net.yanhl.tactics.exception.TacticsDateException;
import net.yanhl.tactics.exception.TacticsException;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsDate;
import net.yanhl.tactics.pojo.TacticsPrice;
import net.yanhl.tactics.service.TacticsManager;
import net.yanhl.util.DateUtil;
import net.yanhl.util.UserUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 策略业务实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091205
 */
@Service(value="tacticsManager")
@Transactional(rollbackFor=Exception.class)
public class TacticsManagerImpl extends BaseManagerImpl implements TacticsManager {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected TacticsDao tacticsDao;
	
	@Resource
	protected FieldManager fieldManager;
	
	@Resource
	protected FieldActivityEngine fieldActivityEngine;

	/**
	 * 价格管理BO
	 */
	@Resource
	protected PriceManager priceManager;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Tactics> getTacticsList(ListQuery listQuery) throws TacticsException {
		// 设置场馆ID
		String venueId = UserUtil.getCurrentVenueId(listQuery.getRequest());
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });
		return baseDao.find(listQuery);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<TacticsDate> getTacticsDateList(ListQuery listQuery) throws TacticsException {
		// 设置场馆ID
		listQuery.getQueryFilter().put("o.tactics.id", new String[] { BaseQuery.AND, BaseQuery.EQ });
		listQuery.getOrderByMap().put("fromDate", null);
		return baseDao.find(listQuery);
	}

	public void savePrices(Long tacticsId, Collection<TacticsPrice> prices) {

		if (prices == null || prices.isEmpty()) {
			return;
		}

		// 1、删除已设置数据
		List<TacticsPrice> listPrices = (List<TacticsPrice>) prices;
		baseDao.deleteAll(listPrices.get(0).getClass(), "tactics.id", tacticsId);

		// 2、保存新数据
		saveCollection(prices);

	}

	@Transactional(readOnly = true)
	public List<TacticsPrice> getTacticsPriceList(Long venueId, String fieldType) {
		return tacticsDao.getTacticsPriceList(venueId, fieldType);
	}

	public void deleteAndBakActivity(Long venueId, Long tacticsId, String fieldType)
		throws TacticsException, FieldException, FieldActivityException, PriceException {
		try {

			//1.1、查询使用当前策略生成活动的日期戳
			List<Date[]> tacticsDates = tacticsDao.getTacticsDates(tacticsId);
			List<Date> allTacticsDates = DateUtil.getAllDate(tacticsDates);

			// 1.1.1、日期排序、分组
			List<List<? extends java.util.Date>> groupDates = DateUtil.groupDates(allTacticsDates);

			// 1.2、检查已生成的活动是否在策略日期范围内
			List<Integer> activityIdsIntacticsDateArea = tacticsDao.activityBetweenTacticsDate(venueId, tacticsId, fieldType, allTacticsDates);
			log.debug("找到在策略日期范围内已预定的记录：" + activityIdsIntacticsDateArea);

			// 2、在策略日期范围内

				// 2.1、复制在策略日期范围内并且已预定的记录到策略活动临时表中
				Integer activity = tacticsDao.copyBookedActivityToTacticsActivity(activityIdsIntacticsDateArea, tacticsId, fieldType);
				log.debug("共复制" + activity + "条已预定记录");

				// 2.2、删除在策略日期范围内的所有活动
				Integer deleteActivity = fieldManager.deleteActivity(venueId, fieldType, allTacticsDates.toArray());

				log.debug("删除在策略日期范围内的所有活动成功,共" + deleteActivity + "条");

				// 2.3、重新生成刚刚删除的活动
				Date startActivityDate = null;
				Date endActivityDate = null;
				for (List<? extends java.util.Date> dateList : groupDates) {
					if (dateList != null && !dateList.isEmpty()) {
						startActivityDate = (Date) dateList.get(0);
						endActivityDate = (Date) dateList.get(dateList.size() - 1);

						log.debug("正在生成活动：" + startActivityDate + "至" + endActivityDate);

						// 生成活动
						fieldActivityEngine.insertActivityUseTactics(venueId, fieldType, startActivityDate, endActivityDate);

						log.debug("活动生成成功：" + startActivityDate + "至" + endActivityDate);
					}
				}

				/**
				 * 事务处理，生成活动后提交事务为下面的自动匹配活动准备
				 */
				getCurrentSession().flush();

			/**
			 * 3、把备份的活动根据日期、时间匹配覆盖到原来的活动表中
			 * 	     自动匹配条件：<可用日期、时段、场馆ID、场地ID>全部匹配，订单不能为空
			 */
				// 3.1、用备份的活动记录更新到正式表中
				List<Integer> repeatBookActivity = tacticsDao.repeatBookActivity(venueId, tacticsId, fieldType);
				log.debug("自动匹配活动ID：" + repeatBookActivity);

				// 3.2、删除备份的记录
				Class<?> fieldTacticsActivityPojoClass = FieldUtil.getFieldTacticsActivityPojoClass(fieldType);

				Long[] tacticsActivityIds = new Long[repeatBookActivity.size()];
				for (int i = 0; i < repeatBookActivity.size(); i++) {
					tacticsActivityIds[i] = new Long(repeatBookActivity.get(i));
				}

				Integer deleteTacticsActivity = baseDao.deleteAll(fieldTacticsActivityPojoClass, tacticsActivityIds);
				log.debug("删除策略备份表活动数：" + deleteTacticsActivity);


			// 4、设置策略的更新状态为未修改
				Tactics tactics = (Tactics) get(Tactics.class, tacticsId);
				if (tactics.getIsModify()) {
					tactics.setIsModify(false);
					super.insertOrUpdate(tactics);
				}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new TacticsException(e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			log.error("[deleteAndBakActivity]，删除、备份活动时：" + e.getMessage());
			throw new TacticsException(e);
		}

	}

	public void applyDefaultTactics(Long venueId, Long tacticsId, String fieldType)
			throws TacticsException, FieldException, FieldActivityException, PriceException {

		// 1、查找和策略相关的活动
		List<? extends FieldActivity> findActivity = tacticsDao.findActivity(venueId, tacticsId, fieldType);
		log.debug("共有" + findActivity.size() + "和策略[ID=" + tacticsId + "]相关联的活动");
		if (findActivity.size() == 0) {
			return;
		}

		List<Integer> activityIdsIntacticsDateArea = new ArrayList<Integer>();
		String[] ids = new String[findActivity.size()];
		for (int i = 0; i < findActivity.size(); i++) {
			Long id = findActivity.get(i).getId();
			ids[i] = id.toString();
			// 过滤有订单项的活动
			if (findActivity.get(i).getFieldOrder() != null) {
				activityIdsIntacticsDateArea.add(id.intValue());
			}
		}

		// 2、复制在策略日期范围内并且已预定的记录到策略活动临时表中
		Integer activity = tacticsDao.copyBookedActivityToTacticsActivity(activityIdsIntacticsDateArea, tacticsId, fieldType);
		log.debug("共复制" + activity + "条已预定记录");

		// 3、删除和策略相关的活动
		Class<? extends FieldActivity> activityPojoClass = null;
		try {
			activityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.equals("未找到和[" + fieldType + "]对应的POJO");
			throw new TacticsException(e);
		}

		deleteAll(activityPojoClass, ids);

		// 删除动作生效
		getCurrentSession().flush();

		// 4、重新按照默认策略生成活动
		Tactics tactics = (Tactics) get(Tactics.class, tacticsId);
		Set<TacticsDate> tacticsDates = tactics.getTacticsDates();
		for (TacticsDate tacticsDate : tacticsDates) {
			Date fromDate = tacticsDate.getFromDate();
			Date toDate = tacticsDate.getToDate();
			fieldActivityEngine.insertActivityUseDefault(venueId, fromDate, toDate);
		}

		// 刷新生成的活动到数据库
		getCurrentSession().flush();

		/**
		 * 5、把备份的活动根据日期、时间匹配覆盖到原来的活动表中
		 * 	     自动匹配条件：<可用日期、时段、场馆ID、场地ID>全部匹配，订单不能为空
		 */
			// 3.1、用备份的活动记录更新到正式表中
			List<Integer> repeatBookActivity = tacticsDao.repeatBookActivity(venueId, tacticsId, fieldType);
			log.debug("自动匹配活动ID：" + repeatBookActivity);

			// 3.2、删除备份的记录
			Class<?> fieldTacticsActivityPojoClass = null;
			try {
				fieldTacticsActivityPojoClass = FieldUtil.getFieldTacticsActivityPojoClass(fieldType);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			Long[] tacticsActivityIds = new Long[repeatBookActivity.size()];
			for (int i = 0; i < repeatBookActivity.size(); i++) {
				tacticsActivityIds[i] = new Long(repeatBookActivity.get(i));
			}

			Integer deleteTacticsActivity = baseDao.deleteAll(fieldTacticsActivityPojoClass, tacticsActivityIds);
			log.debug("删除策略备份表活动数：" + deleteTacticsActivity);

	}

	@Transactional(readOnly = true)
	public JSONObject checkRepeatDates(Long venueId, String fieldType, Date fromDate, Date toDate) throws TacticsDateException {

		// 检查数据完整性
		if (fromDate == null && toDate == null) {
			throw new TacticsDateException("日期段不能为空");
		}

		boolean repeat = false;
		JSONObject result = new JSONObject();

		// 存放策略名称和重复的日期段
		Map<String, List<String[]>> tacticsAndRepeatDates = new HashMap<String, List<String[]>>();

		// 查找本场馆的特殊策略
		String[] keys = new String[] {"venueId", "fieldType"};
		Object[] values = new Object[] {venueId, fieldType};
		List<Tactics> tacticses = super.findBy(Tactics.class, keys, values);

		for (Tactics tactics : tacticses) {

			String tacticsName = tactics.getTacticsName();
			Set<TacticsDate> tacticsDates = tactics.getTacticsDates();

			for (TacticsDate tacticsDate : tacticsDates) {
				boolean fromBetweenDays = DateUtil.betweenDays(tacticsDate.getFromDate(), tacticsDate.getToDate(), fromDate);
				boolean toBetweenDays = DateUtil.betweenDays(tacticsDate.getFromDate(), tacticsDate.getToDate(), toDate);
				if (fromBetweenDays || toBetweenDays) {

					// 设置重复标志
					repeat = true;

					/*
					 * 把与之重复的策略放到集合里面
					 */
					if (!tacticsAndRepeatDates.containsKey(tacticsName)) {
						tacticsAndRepeatDates.put(tacticsName, new ArrayList<String[]>());
					}
					// 重复的日期段
					String[] tempRepeatDates = new String[] {DateUtil.format(tacticsDate.getFromDate(), DateUtil.TYPE_DATE),
															 DateUtil.format(tacticsDate.getToDate(), DateUtil.TYPE_DATE)};
					tacticsAndRepeatDates.get(tacticsName).add(tempRepeatDates);
				}
			}
		}

		/**
		 * 处理结果，如果有重复的日期抛出异常，返回重复的日期段
		 */
		String repeatLabel = "repeat";
		if (repeat) {
			result.accumulate(repeatLabel, true);
			Set<String> keySet = tacticsAndRepeatDates.keySet();
			JSONObject tempJson = new JSONObject();
			for (String key : keySet) {
				if (!tempJson.containsKey(key)) {
					tempJson = new JSONObject();
				}
				tempJson.accumulate("name", key);
				tempJson.accumulate("dates", tacticsAndRepeatDates.get(key));

				result.accumulate("datas", tempJson);
			}
		} else {
			result.accumulate(repeatLabel, false);
		}

		return result;
	}

	@Transactional(readOnly = true)
	public List<Object> tacticsActivityList(Long venueId, String fieldType) throws TacticsException, ClassNotFoundException, FieldException {
		Class<?> activityPojoClass = FieldUtil.getFieldTacticsActivityPojoClass(fieldType);
		ListQuery listQuery = new ListQuery(activityPojoClass);
		listQuery.setOwnerLabel(new String[] {"venueId", String.valueOf(venueId)});
		listQuery.getOrderByMap().put("fromTime", "");
		List<Object> list = find(listQuery);
		return list;
	}

}
