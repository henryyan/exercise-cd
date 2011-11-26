package net.yanhl.field.engine.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.dao.FieldDao;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.price.service.PriceManager;
import net.yanhl.price.util.PriceUtil;
import net.yanhl.tactics.dao.TacticsDao;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsPrice;
import net.yanhl.tactics.util.TacticsUtil;
import net.yanhl.util.DateUtil;
import net.yanhl.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b>场地活动业务实现类>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
@Service(value = "fieldActivityEngine")
@Transactional(rollbackFor = Exception.class)
public class FieldActivityEngineImpl extends BaseManagerImpl implements FieldActivityEngine {

	Log log = LogFactory.getLog(this.getClass());

	/**
	 * 价格管理BO
	 */
	@Resource
	protected PriceManager priceManager;

	/**
	 * 价格策略DAO接口
	 */
	@Resource
	protected TacticsDao tacticsDao;

	/**
	 * 场地DAO接口
	 */
	@Resource
	protected FieldDao fieldDao;

	/**
	 * 场地类型DAO接口
	 */
	@Resource
	protected FieldTypeDao fieldTypeDao;

	public synchronized void createActivityForServlet() throws FieldException, FieldActivityException, PriceException {
		log.info("**********活动生成定时任务--开始**********");
		createActivity(-1l, null);
		log.info("**********活动生成定时任务--结束**********");
	}

	public synchronized void createActivity(Long venueId, Date toCreateDate) throws FieldException,
			FieldActivityException, PriceException {

		long start = System.currentTimeMillis();
		// 获取已启用的场地类型
		String[] fieldTypes = getFieldTypes(venueId);
		log.info("**********生成所有场馆、所有场地的活动--开始**********");
		
		/**
		 * 循环场地类型
		 */
		for (String fieldType : fieldTypes) {
			createActivity(venueId, fieldType, toCreateDate);
		}
		log.debug("生成所有场馆、所有场地的活动结束；耗时：[" + (System.currentTimeMillis() - start) + "]毫秒");
		log.info("**********生成所有场馆、所有场地的活动--结束**********");
	}

	public void createActivity(Long venueId, String fieldType, Date toCreateDate) throws FieldException,
			FieldActivityException, PriceException {
		
		log.info("开始生成[" + FieldUtil.getFieldZhType(fieldType) + "]的活动");
		
		long startTime = System.currentTimeMillis();
		long oldVenueId = venueId.longValue();

		// 临时使用的日历对象
		Calendar calendar = Calendar.getInstance();

		// 当前日期
		Date currentDate = Date.valueOf(DateUtil.getSysdate());

		// 价格临时对象
		List<? extends BasicPricePojo> priceList = null;

		// 价格MAP
		Map<String, List<? extends BasicPricePojo>> priceMap = new HashMap<String, List<? extends BasicPricePojo>>();
		String basicFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_BASIC;
		String weekendFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_WEEKEND;

		// 特殊策略MAP
		Map<String, List<Tactics>> tacticsMap = new HashMap<String, List<Tactics>>();

		// 保存场馆是否设置了活动价格
		Map<String, Boolean> hasPriceMap = new HashMap<String, Boolean>();

		long activityAmount = 0l;

		// 1、查询当前场馆所有【启用】的场地
		List<? extends BaseField> fieldList = getEnableFieldList(oldVenueId, fieldType);
		log.debug("自动生成：查询到" + fieldList.size() + "块[" + FieldUtil.getFieldZhType(fieldType) + "]场地");

		/**
		 * 循环场地列表
		 */
		for (BaseField field : fieldList) {
			
			// 1、验证是否满足可使用条件
			if (field == null
					|| field.getVenueInfo() == null
					|| !field.getVenueInfo().hasFieldType(fieldType)) {
				continue;
			}

			// 2、最后生成日期，如果最后生成日期为空则默认从当天开始
			Date lastIssueDate = field.getIssueLastDate() == null ? currentDate : field.getIssueLastDate();
			lastIssueDate = lastIssueDate.getTime() < currentDate.getTime() ? currentDate : lastIssueDate;

			// 当前日期
			calendar.setTime(currentDate);
			calendar.add(Calendar.DATE, field.getAdvance() - 1);

			// 3、理论活动应该生成到日期
			Date normalActivityDate = null;
			if (toCreateDate == null) {
				normalActivityDate = new Date(calendar.getTime().getTime());
			} else {// 自定义的日期
				calendar.setTime(toCreateDate);
				normalActivityDate = new Date(calendar.getTime().getTime());
			}

			// 4、计算从最后发布日期后应该生成多少天的活动
			int advance = (int) ((normalActivityDate.getTime() - lastIssueDate.getTime()) / 86400000);

			// 包括当天创建的记录
			if (DateUtil.isSameDate(lastIssueDate, currentDate)) {
				advance++;
			} else if (DateUtil.isSameDate(lastIssueDate, normalActivityDate)) {
				log.debug("场馆ID=" + venueId + "，最后生成日期和计划生成日期相同：" + lastIssueDate + "，跳过");
				continue;
			} else if (lastIssueDate.getTime() > currentDate.getTime()) {
				calendar.setTime(lastIssueDate);
				calendar.add(Calendar.DATE, 1);
				lastIssueDate = new Date(calendar.getTime().getTime());
			}

			// 5、自动生成活动时使用场地的场馆ID
			if (field.getVenueInfo() != null) {
				venueId = field.getVenueInfo().getId();
				basicFieldPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_BASIC);
				weekendFieldPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_WEEKEND);
			}

			// 5.1、获取价格列表
			setActivityPrice(venueId, priceMap, fieldType);

			// 6、读取价格策略
			String hasPriceKey = venueId + "-" + fieldType;
			List<Tactics> tacticsList = tacticsMap.get(hasPriceKey);
			if (tacticsList == null) {
				tacticsList = tacticsDao.findTactics(venueId, fieldType);
				log.debug("自动生成：得到特殊策略数量：" + tacticsList.size());
			}

			// 6.1、如果没有任何价格设置也没有特殊策略(日期、价格)则进行下一次循环
			if (hasPriceMap.containsKey(hasPriceKey)) {
				if (!hasPriceMap.get(hasPriceKey)) {
					log.warn("自动生成：没有设置价格也没有设置特殊策略的日期和价格，跳过此场地");
					continue;
				}
			} else {
				if (priceMap.get(basicFieldPriceKey).isEmpty() && priceMap.get(weekendFieldPriceKey).isEmpty()) {
					boolean hasTacticsDateAndPrice = true;
					for (Tactics tactics : tacticsList) {
						hasTacticsDateAndPrice &= tactics.hasDateAndPrice();
					}
					hasPriceMap.put(hasPriceKey, hasTacticsDateAndPrice);
					if (!hasTacticsDateAndPrice) {
						log.warn("自动生成：没有设置价格也没有设置特殊策略的日期和价格，跳过此场地");
						continue;
					}
				}
			}

			log.debug("开始自动生成：场地<" + venueId + "，" + field.getName() + ">，最后生成日期=" + lastIssueDate + "，正常="
					+ normalActivityDate + "，差 " + advance + "天");

			// 7、按照可提前预订天数循环
			for (int j = 0; j < advance; j++) {
				calendar.setTime(lastIssueDate);
				calendar.add(Calendar.DATE, j);

				// 可预订日期
				Date usableDate = new Date(calendar.getTime().getTime());

				// 从priceMap中获取价格列表
				priceList = setPriceList(priceMap, basicFieldPriceKey, weekendFieldPriceKey, tacticsList, usableDate);

				// 没有价格不发布
				if (priceList == null || priceList.isEmpty()) {
					continue;
				} else {
					// 设置、保存活动
					saveActivity(venueId, fieldType, field, usableDate, priceList);
					activityAmount += priceList.size();

					// 设置场地的最后发布日期
					field.setIssueLastDate(usableDate);
					update(field);
				}

			}
		}
		log.debug("完成[" + FieldUtil.getFieldZhType(fieldType) + "]活动生成：，共" + activityAmount + "条活动，耗时["
				+ (System.currentTimeMillis() - startTime) + "]毫秒");
	}

	public void insertActivityUseDefault(Long venueId, Date fromDate, Date toDate) throws FieldException,
			FieldActivityException, PriceException {

		long startTime = System.currentTimeMillis();
		// 获取已启用的场地类型
		String[] fieldTypes = getFieldTypes(venueId);

		// 临时使用的日历对象
		Calendar calendar = Calendar.getInstance();

		// 当前日期
		Date currentDate = Date.valueOf(DateUtil.getSysdate());

		// 价格临时对象
		List<? extends BasicPricePojo> priceList = null;

		// 价格MAP
		Map<String, List<? extends BasicPricePojo>> priceMap = new HashMap<String, List<? extends BasicPricePojo>>();
		String basicFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_BASIC;
		String weekendFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_WEEKEND;

		/**
		 * 循环场地类型
		 */
		for (String fieldType : fieldTypes) {

			long activityAmount = 0l;

			// 1、查询当前场馆所有【启用】的场地
			List<? extends BaseField> fieldList = getEnableFieldList(venueId, fieldType);
			log.debug("默认策略插入活动：查询到" + fieldList.size() + "块[" + FieldUtil.getFieldZhType(fieldType) + "]场地");

			/**
			 * 循环场地列表
			 */
			for (BaseField field : fieldList) {

				// 1.1、判断场馆是否开启了此场地类型
				if (!field.getVenueInfo().hasFieldType(fieldType)) {
					continue;
				}

				// 1.2、如果要生成的活动还没有过最后生成日期则不生成
				if (field.getIssueLastDate() != null && fromDate.getTime() > field.getIssueLastDate().getTime()) {
					continue;
				}

				// 设置场地的场馆ID
				if (field.getVenueInfo() != null) {
					venueId = field.getVenueInfo().getId();
					basicFieldPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_BASIC);
					weekendFieldPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_WEEKEND);
				}

				// 获取价格列表
				setActivityPrice(venueId, priceMap, fieldType);

				// 最后生成日期
				Date lastIssueDate = fromDate;

				// 当前日期
				calendar.setTime(currentDate);
				calendar.add(Calendar.DATE, field.getAdvance() - 1);

				/*
				 * 理论活动应该生成到日期
				 */
				Date normalActivityDate = null;
				if (toDate == null) {
					normalActivityDate = new Date(calendar.getTime().getTime());
				} else {// 自定义的日期
					calendar.setTime(toDate);
					normalActivityDate = new Date(calendar.getTime().getTime());
				}

				/*
				 *计算从最后发布日期后应该生成多少天的活动
				 */
				int advance = (int) ((normalActivityDate.getTime() - lastIssueDate.getTime()) / 86400000);
				if (normalActivityDate.getTime() > lastIssueDate.getTime()) {
					advance++;
				}

				log.debug("默认策略插入活动：场地<" + venueId + "，" + field.getName() + ">,最后生成日期=" + lastIssueDate + ",正常="
						+ normalActivityDate + ",相差=" + advance + "天");

				// 按照可提前预订天数循环
				for (int j = 0; j < advance; j++) {
					calendar.setTime(lastIssueDate);
					calendar.add(Calendar.DATE, j);

					// 可预订日期
					Date usableDate = new Date(calendar.getTime().getTime());

					// 从priceMap中获取价格列表
					priceList = setPriceList(priceMap, basicFieldPriceKey, weekendFieldPriceKey, usableDate);

					// 没有价格不发布
					if (priceList == null || priceList.isEmpty()) {
						continue;
					} else {
						// 设置、保存活动
						saveActivity(venueId, fieldType, field, usableDate, priceList);
						activityAmount += priceList.size();
					}

				} // 生成活动循环
			} // 场地列表循环
			log.debug("默认策略插入活动：生成场地类型[" + FieldUtil.getFieldZhType(fieldType) + "]完成，共" + activityAmount + "条活动，耗时["
					+ (System.currentTimeMillis() - startTime) + "]毫秒");
		} // 场地类型循环
	}

	public synchronized void insertActivityUseTactics(Long venueId, Date fromCreateDate, Date toCreateDate)
			throws FieldException, FieldActivityException, PriceException {

		// 获取已启用的场地类型
		String[] fieldTypes = getFieldTypes(venueId);

		/**
		 * 循环场地类型
		 */
		for (int i = 0; i < fieldTypes.length; i++) {
			insertActivityUseTactics(venueId, fieldTypes[i], fromCreateDate, toCreateDate);
		}

	}

	public synchronized void insertActivityUseTactics(Long venueId, String fieldType, Date fromCreateDate,
			Date toCreateDate) throws FieldException, FieldActivityException, PriceException {

		long startTime = System.currentTimeMillis();

		// 生成的活动数量
		long activityAmount = 0l;

		// 价格临时对象
		List<? extends BasicPricePojo> priceList = null;

		// 价格MAP
		Map<String, List<? extends BasicPricePojo>> priceMap = new HashMap<String, List<? extends BasicPricePojo>>();
		String basicFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_BASIC;
		String weekendFieldPriceKey = venueId + "-" + PriceUtil.PRICE_TYPE_WEEKEND;

		Calendar calendar = Calendar.getInstance();
		Date currentDate = Date.valueOf(DateUtil.getSysdate());

		// 查询当前场馆所有【启用】的场地
		List<? extends BaseField> fieldList = getEnableFieldList(venueId, fieldType);

		//读取特殊价格策略
		List<Tactics> tacticsList = tacticsDao.findTactics(venueId, fieldType);

		/**
		 * 循环场地列表
		 */
		for (BaseField field : fieldList) {

			// 1、判断场馆是否开启了此场地类型
			if (!field.getVenueInfo().hasFieldType(fieldType)) {
				continue;
			}

			// 最后生成日期
			Date lastIssueDate = fromCreateDate;

			// 当前日期
			calendar.setTime(currentDate);
			calendar.add(Calendar.DATE, field.getAdvance() - 1);

			/*
			 * 理论活动应该生成到日期
			 */
			Date normalActivityDate = null;
			if (toCreateDate == null) {
				normalActivityDate = new Date(calendar.getTime().getTime());
			} else {// 自定义的日期
				calendar.setTime(toCreateDate);
				normalActivityDate = new Date(calendar.getTime().getTime());
			}

			/*
			 *计算从最后发布日期后应该生成多少天的活动
			 */
			int advance = (int) ((normalActivityDate.getTime() - lastIssueDate.getTime()) / 86400000);
			if (normalActivityDate.getTime() > lastIssueDate.getTime()) {
				advance++;
			}

			log.debug("特殊策略插入活动：场地<" + venueId + "，" + field.getName() + ">,最后生成日期=" + lastIssueDate + ",正常="
					+ normalActivityDate + ",相差=" + advance + "天");

			// 按照可提前预订天数循环
			for (int j = 0; j < advance; j++) {
				calendar.setTime(lastIssueDate);
				calendar.add(Calendar.DATE, j);
				// 可预订日期
				Date usableDate = new Date(calendar.getTime().getTime());

				// 从priceMap中获取价格列表
				priceList = setPriceList(priceMap, basicFieldPriceKey, weekendFieldPriceKey, tacticsList, usableDate);

				// 没有价格不发布
				if (priceList == null || priceList.isEmpty()) {
					continue;
				} else {
					// 设置、保存活动
					saveActivity(venueId, fieldType, field, usableDate, priceList);
					activityAmount += priceList.size();
				}
			}
		}
		log.debug("特殊策略插入活动：生成场地类型[" + FieldUtil.getFieldZhType(fieldType) + "]完成，共" + activityAmount + "条活动，耗时["
				+ (System.currentTimeMillis() - startTime) + "]毫秒");
	}

	/**
	 * 根据周末与否返回相应的价格列表
	 * @param priceMap					周末、非周末价格列表MAP
	 * @param basicFieldPriceKey		指定场地类型的非周末价格KEY，使用{@link #createFieldPriceKey(Long, String, String)}生成
	 * @param weekendFieldPriceKey		指定场地类型的周末价格KEY，使用{@link #createFieldPriceKey(Long, String, String)}生成
	 * @param usableDate				活动预定日期
	 * @return	如果usableDate在给定的策略范围内返回策略价格，否则根据给定的周末与否返回对应价格列表
	 */
	private List<? extends BasicPricePojo> setPriceList(Map<String, List<? extends BasicPricePojo>> priceMap,
			String basicFieldPriceKey, String weekendFieldPriceKey, Date usableDate) {

		List<? extends BasicPricePojo> priceList = null;
		;

		// 是否为周末
		boolean weekend = DateUtil.isWeekend(usableDate);

		// 周末价格
		if (weekend) {
			priceList = priceMap.get(weekendFieldPriceKey);
		}
		// 非周末价格
		else {
			priceList = priceMap.get(basicFieldPriceKey);
		}

		return priceList;
	}

	/**
	 * 根据策略、周末与否返回相应的价格列表
	 * @param priceMap					周末、非周末价格列表MAP
	 * @param basicFieldPriceKey		指定场地类型的非周末价格KEY，使用{@link #createFieldPriceKey(Long, String, String)}生成
	 * @param weekendFieldPriceKey		指定场地类型的周末价格KEY，使用{@link #createFieldPriceKey(Long, String, String)}生成
	 * @param tacticsList				策略列表
	 * @param usableDate				活动预定日期
	 * @return	如果usableDate在给定的策略范围内返回策略价格，否则根据给定的周末与否返回对应价格列表
	 */
	private List<? extends BasicPricePojo> setPriceList(Map<String, List<? extends BasicPricePojo>> priceMap,
			String basicFieldPriceKey, String weekendFieldPriceKey, List<Tactics> tacticsList, Date usableDate) {
		List<? extends BasicPricePojo> priceList = null;
		;
		List<? extends BasicPricePojo> betweenTacticsPrice = TacticsUtil
				.getBetweenTacticsPrice(tacticsList, usableDate);
		if (betweenTacticsPrice.isEmpty()) {

			// 是否为周末
			boolean weekend = DateUtil.isWeekend(usableDate);

			// 周末价格
			if (weekend) {
				priceList = priceMap.get(weekendFieldPriceKey);
			}
			// 非周末价格
			else {
				priceList = priceMap.get(basicFieldPriceKey);
			}

		}
		// 特殊策略价格
		else {
			priceList = betweenTacticsPrice;
		}
		return priceList;
	}

	/**
	 * 生成价格KEY，格式：场馆ID-场地类型-价格类型
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param priceType		价格类型
	 * @return
	 */
	private String createFieldPriceKey(Long venueId, String fieldType, String priceType) {
		return venueId + "-" + fieldType + "-" + priceType;
	}

	/**
	 * 根据场馆ID查询价格并设置到priceMap中，priceMap的key使用{@link #createFieldPriceKey(Long, String, String)}生成
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param priceType		价格类型
	 * @throws PriceException	根据场地、价格类型找不到对应的价格对象时
	 */
	private void setActivityPrice(Long venueId, Map<String, List<? extends BasicPricePojo>> priceMap, String fieldType)
			throws PriceException {
		String basicPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_BASIC);
		String weekendPriceKey = createFieldPriceKey(venueId, fieldType, PriceUtil.PRICE_TYPE_WEEKEND);
		// 非周末价格
		if (!priceMap.containsKey(basicPriceKey)) {
			List<? extends BasicPricePojo> basicPriceList = priceManager.getFieldPriceList(venueId, fieldType,
					PriceUtil.PRICE_TYPE_BASIC);
			priceMap.put(basicPriceKey, basicPriceList);
			log.debug("自动生成：得到价格[key=" + basicPriceKey + "，数量=" + basicPriceList.size() + "]");
		}
		// 周末价格
		if (!priceMap.containsKey(weekendPriceKey)) {
			List<? extends BasicPricePojo> weekendPriceList = priceManager.getFieldPriceList(venueId, fieldType,
					PriceUtil.PRICE_TYPE_WEEKEND);
			priceMap.put(weekendPriceKey, weekendPriceList);
			log.debug("自动生成：得到价格[key=" + weekendPriceKey + "，数量=" + weekendPriceList.size() + "]");
		}
	}

	/**
	 * 设置活动信息
	 * @param fieldType		场地类型
	 * @param venueId		场馆ID
	 * @param field			场地ID
	 * @param usableDate	可用日期
	 * @param price			价格对象
	 * @return	包装好的FieldActivity对象
	 * @throws FieldException 找不到类型对应的值
	 * @throws ClassNotFoundException 构建场地活动时
	 * @throws IllegalAccessException 构建场地活动时
	 * @throws InstantiationException 构建场地活动时
	 */
	private FieldActivity setFieldActivity(String fieldType, Long venueId, BaseField field, Date usableDate,
			BasicPricePojo price) throws FieldException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		FieldActivity activity = FieldUtil.getFieldActivityPojoClass(fieldType).newInstance();
		activity.setVenueId(venueId);
		activity.setField(field);
		activity.setFieldType(FieldUtil.getFieldTypeByPojo(field.getClass()));
		activity.setFieldName(StringUtils.defaultString(field.getName(), "未命名"));
		activity.setPeriod(price.getFromTime() + "~" + price.getToTime());
		activity.setPrice(price.getPrice());
		activity.setPaymentCommision(price.getPaymentCommision());
		activity.setFromTime(Time.valueOf(price.getFromTime() + ":00"));
		activity.setToTime(Time.valueOf(price.getToTime() + ":00"));
		activity.setUsableDate(usableDate);

		// 默认设置为“未预定”
		activity.setActivity(FieldUtil.FIELD_ACTIVITY_UNRESERVED);

		// 处理策略价格情况，如果是策略价格则设置活动的策略ID
		if (price instanceof TacticsPrice) {
			TacticsPrice tacticsPrice = (TacticsPrice) price;
			activity.setTacticsId(tacticsPrice.getTactics().getId());
		}
		return activity;
	}

	/**
	 * 保存活动到数据库
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param field			场地对象
	 * @param usableDate	预定日期
	 * @param priceList		价格列表
	 * @throws FieldException
	 * @throws FieldActivityException
	 */
	private void saveActivity(Long venueId, String fieldType, BaseField field, Date usableDate,
			List<? extends BasicPricePojo> priceList) throws FieldException, FieldActivityException {

		// 插入活动列表
		for (int k = 0; k < priceList.size(); k++) {
			BasicPricePojo price = priceList.get(k);
			FieldActivity activity = null;
			try {
				activity = setFieldActivity(fieldType, venueId, field, usableDate, price);
			} catch (InstantiationException e) {
				throw new FieldActivityException(e);
			} catch (IllegalAccessException e) {
				throw new FieldActivityException(e);
			} catch (ClassNotFoundException e) {
				throw new FieldActivityException(e);
			}
			save(activity);
		}
	}

	/**
	 * 查找本场馆所有启用的场地，场馆ID为-1时查找全部场地
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @return	所有启用的场地列表
	 * @throws FieldException	根据场地类型查找不到对应的POJO实体时
	 */
	private List<? extends BaseField> getEnableFieldList(Long venueId, String fieldType) throws FieldException {
		List<? extends BaseField> fieldList;
		if (venueId == null || venueId == -1) {
			fieldList = fieldDao.getEnableField(fieldType);
		} else {
			fieldList = fieldDao.getEnableField(venueId, fieldType);
		}
		return fieldList;
	}

	/**
	 * 使用场地类型DAO返回场地类型数组
	 * @param venueId	场馆ID
	 * @return
	 */
	private String[] getFieldTypes(Long venueId) {
		String[] fieldTypes = fieldTypeDao.getEnableFieldTypes(venueId);
		log.debug("生成活动，场地类型：" + StringUtil.getValue(fieldTypes));
		return fieldTypes;
	}

}