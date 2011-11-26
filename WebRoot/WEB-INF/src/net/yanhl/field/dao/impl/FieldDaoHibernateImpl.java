package net.yanhl.field.dao.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.field.dao.FieldDao;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.util.DateUtil;
import net.yanhl.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

/**
 * <p><b>Title：</b>场地DAO的Hibernate实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 */
@SuppressWarnings({ "unchecked" })
@Repository(value="fieldDao")
public class FieldDaoHibernateImpl extends BaseDaoHibernate implements FieldDao {

	Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	protected FieldTypeDao fieldTypeDao;

	public void setFieldTypeDao(FieldTypeDao fieldTypeDao) {
		this.fieldTypeDao = fieldTypeDao;
	}

	public Integer getMaxFieldNo(String fieldType, Long venueId) throws FieldException {
		// 根据@fieldType设置读取哪个POJO对象数据
		String pojoName = FieldUtil.getFieldPojoName(fieldType);

		String hql = "select count(fieldCode) from " + pojoName + " where venueInfo.id=" + venueId;
		List<Long> maxFieldCode = getHibernateTemplate().find(hql);
		int max = maxFieldCode.get(0).intValue() + 1;
		return max;
	}

	@SuppressWarnings("rawtypes")
	public List getFieldList(Long venueId, String fieldType) throws FieldException {
		// 根据@fieldType设置读取哪个POJO对象数据
		String pojoName = FieldUtil.getFieldPojoName(fieldType);

		if (StringUtils.isEmpty(pojoName)) {
			return new ArrayList<Object[]>();
		}

		String hql = "select f from " + pojoName + " f where f.venueInfo.id=?";
		List find = getHibernateTemplate().find(hql, new Object[] {venueId});
		return find;
	}

	@SuppressWarnings("rawtypes")
	public List getFieldList(String fieldType, String status) throws FieldException {
		// 根据@fieldType设置读取哪个POJO对象数据
		String pojoName = FieldUtil.getFieldPojoName(fieldType);

		if (StringUtils.isEmpty(pojoName)) {
			return new ArrayList<Object[]>();
		}

		String hql = "select f from " + pojoName + " f where f.status=?";
		List find = getHibernateTemplate().find(hql, new Object[] {status});
		return find;
	}

	@SuppressWarnings("rawtypes")
	public List getFieldList(Long venueId, String fieldType, String status) throws FieldException {
		// 根据@fieldType设置读取哪个POJO对象数据
		String pojoName = FieldUtil.getFieldPojoName(fieldType);

		if (StringUtils.isEmpty(pojoName)) {
			return new ArrayList<Object[]>();
		}

		String hql = "select f from " + pojoName + " f, VenueInfo vi where f.status=?"
					+ " and f.venueInfo.id = vi.id and vi.id=?";
		List find = getHibernateTemplate().find(hql, new Object[] {status, venueId});
		return find;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getFieldActivityStatusForDate(final String fieldType, final Long venueId, final String strFromDate) {

		final Map<String, String> status = new HashMap<String, String>();

		List executeFind = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				/**
				 * 1、先把所有活动可用日期查询出来，默认为不可用状态
				 * 2、根据活动的可用条件查询可用活动信息
				 */

				String fieldActivityTableName = null;
				try {
					fieldActivityTableName = FieldUtil.getFieldActivityTableName(fieldType);
				} catch (FieldException e1) {
					log.error(e1.getMessage(), e1);
				}

				Date fromDate = null;
				try {
					fromDate = DateUtil.getSqlDate(strFromDate, DateUtil.TYPE_DATE);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// 查询出从@param strFromDate开始的所有可用日期
				String getUsableDatesSql = "select distinct usable_date from " + fieldActivityTableName +
										" where usable_date >= :usable_date and usable_date <= :usable_end_date and venue_id=:venue_id";
				Calendar ca = Calendar.getInstance();
				ca.setTime(fromDate);
				int year = ca.get(Calendar.YEAR);
				int month = ca.get(Calendar.MONTH);
				String lastDateDayOfMonth = DateUtil.getLastDateDayOfMonth(year, month);
				java.util.Date usableEndDate = null;
				try {
					usableEndDate = DateUtil.getDate(lastDateDayOfMonth, DateUtil.TYPE_DATE);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				List<Object[]> usableDates = session.createSQLQuery(getUsableDatesSql)
													.setDate("usable_date", fromDate)
													.setDate("usable_end_date", usableEndDate)
													.setLong("venue_id", venueId).list();

				for (Object object : usableDates) {
					status.put(object.toString(), "-1");
				}

				// 条件一：可预订状态
				String filter = "(t.activity='未预订' or t.activity='取消')";

				// 条件二：可预订日期从@param strFromDate 开始，当天的再根据系统时间比较
				filter += " and ((t.usable_date = :usable_date " +
								"and ( HOUR(from_time) > :from_hour " +
								"or ( HOUR(from_time) = :from_hour and MINUTE(from_time) > :from_minute )))" +
								//"or HOUR(from_time) > :from_hour and MINUTE(from_time) > :from_minute) ) " +
							"or (t.usable_date >= :usable_date and usable_date <= :usable_end_date) )";


				String sql = "select t.usable_date,"
					+ "if(count(t.id) > 0, 1, 0) empty"
					+ " from " + fieldActivityTableName + " t "
					+ " where t.venue_id = :venue_id"
					+ " and " + filter
					+ " group by t.usable_date";

				Calendar ca1 = Calendar.getInstance();
				return session.createSQLQuery(sql)
					.setLong("venue_id", venueId)
					.setDate("usable_date", fromDate)
					.setDate("usable_end_date", usableEndDate)
					.setInteger("from_hour", ca1.get(Calendar.HOUR_OF_DAY))
					.setInteger("from_minute", ca1.get(Calendar.MINUTE))
					.list();
			}

		});

		for (Object object : executeFind) {
			Object[] values = (Object[]) object;
			status.put(values[0].toString(), values[1].toString());
		}

		return status;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getFieldActivityStatusForPeriod(final String fieldType, final Long venueId, final String usableDate) {

		Map<String, String> status = new TreeMap<String, String>(new Comparator() {

			public int compare(Object arg0, Object arg1) {
				int fromTime1 = new Integer(arg0.toString().split("~")[0].split(":")[0]);
				int fromTime2 = new Integer(arg1.toString().split("~")[0].split(":")[0]);
				if(fromTime1 < fromTime2) {
					return -1;
				} else if(fromTime1 == fromTime2) {
					return 0;
				} else if(fromTime1 > fromTime2) {
					return 1;
				}
				return 0;
			}

		});

		// 1、查找所有时间段
		List periods = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String sql = null;
				try {
					sql = "select distinct period from " + FieldUtil.getFieldActivityTableName(fieldType)
					+ " where usable_date = DATE_FORMAT('" + usableDate + "', '%Y-%m-%e')"
					+ " and venue_id = ?";
				} catch (FieldException e) {
					log.error(e.getMessage(), e);
				}
				return session.createSQLQuery(sql).setLong(0, venueId).list();
			}

		});

		//status = CollectionUtils.getCardinalityMap(periods);
		for (Object period : periods) {
			status.put(period.toString(), "0");
		}

		// 2、查找各时间段预订结果
		List executeFind = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String filter = "(t.order_user is null or t.order_user = '' or t.order_user = '作废' or t.order_user = '取消')";

				String sql = null;
				try {
					sql = "select t.period,"
						+ "if(count(t.id) > 0, 1, 0) empty"
						+ " from " + FieldUtil.getFieldActivityTableName(fieldType) + " t "
						+ " where t.usable_date = DATE_FORMAT('" + usableDate + "', '%Y-%m-%e')"
						+ " and t.venue_id = ?"
						+ " and " + filter
						+ " group by t.period order by from_time";
				} catch (FieldException e) {
					e.printStackTrace();
				}
				return session.createSQLQuery(sql)
					.setLong(0, venueId)
					.list();
			}

		});

		for (Object object : executeFind) {
			Object[] values = (Object[]) object;
			if(!status.containsKey(values[0].toString())) {
				status.put(values[0].toString(), "0");
			} else {
				status.put(values[0].toString(), values[1].toString());
			}
		}

		return status;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getFieldActivityStatusForField(final String fieldType, final Long venueId,
			final String usableDate, final String period) {
		List executeFind = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String filter = "(t.order_user is null or t.order_user = '' or t.order_user = ? or t.order_user = ?)";

				String sql = null;
				try {
					sql = "select distinct t.field_id,t.field_name"
						+ " from " + FieldUtil.getFieldActivityTableName(fieldType) + " t "
						+ " where t.usable_date = DATE_FORMAT('" + usableDate + "', '%Y-%m-%e')"
						+ " and period = ?"
						+ " and t.venue_id = ?"
						+ " and " + filter;
				} catch (FieldException e) {
					log.error(e.getMessage(), e);
				}
				return session.createSQLQuery(sql)
					.setString(0, period)
					.setLong(1, venueId)
					.setString(2, "作废")
					.setString(3, "取消")
					.list();
			}

		});

		Map<String, String> status = new HashMap<String, String>();
		for (Object object : executeFind) {
			Object[] values = (Object[]) object;
			status.put(values[0].toString(), values[1].toString());
		}

		return status;
	}

	public Map<String, List<Object[]>> getActivityGrid(Long venueId, String fieldType, String usableDate)
			throws FieldActivityException, FieldException {

		Map<String, List<Object[]>> result = new TreeMap<String, List<Object[]>>(new Comparator<Object>() {

			public int compare(Object arg0, Object arg1) {
				int fromTime1 = new Integer(arg0.toString().split("~")[0].split(":")[0]);
				int fromTime2 = new Integer(arg1.toString().split("~")[0].split(":")[0]);
				if(fromTime1 < fromTime2) {
					return -1;
				} else if(fromTime1 == fromTime2) {
					return 0;
				} else if(fromTime1 > fromTime2) {
					return 1;
				}
				return 0;
			}

		});

		String hql = "from " + FieldUtil.getFieldActivityPojoName(fieldType) + " where venueId=" + venueId + " and usableDate='"
				+ usableDate + "' order by fromTime, field.id";
		List<FieldActivity> find = find(hql);

		List<Object[]> singleActivity = null;
		for (FieldActivity activity : find) {
			Long orderId = 0l;
			BaseField field = activity.getField();
			FieldOrder fieldOrder = activity.getFieldOrder();
			if (fieldOrder != null) {
				orderId = fieldOrder.getId();
			}
			Object[] activityObj = new Object[] {orderId, field.getId(), activity.getFieldName(), activity.getActivity(), activity.getFieldOrder()};
			if (!result.containsKey(activity.getPeriod())) {
				singleActivity = new ArrayList<Object[]>();
				result.put(activity.getPeriod(), singleActivity);
			} else {
				singleActivity = result.get(activity.getPeriod());
			}
			singleActivity.add(activityObj);
		}
		return result;
	}

	public List<FieldActivity> getFieldActivity(Long fieldId, int nextStep, String fieldType) throws FieldException{
		String activityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
		String hql = "from " + activityPojoName + " where usableDate = DATE_FORMAT('"
				+  DateUtil.format(DateUtil.dateAdd(DateUtil.getSysdate(), nextStep), "date") + "', '%Y-%m-%e')"
				+ " and field.id=" + fieldId + " order by fromTime";
		List<FieldActivity> result = getHibernateTemplate().find(hql);
		return result;
	}

	public boolean changeActivityStatus(String fieldType, String fieldActivityIds, String status) throws FieldException {
		boolean result = false;
		boolean isCancel = false;
		
		String realDbStatus = "";

		if(fieldActivityIds == null) {
			return false;
		}

		String tempUpdateHql = "update " + FieldUtil.getFieldActivityPojoName(fieldType) + " f set f.activity=:status";

		// 取消、作废操作时清除验证码及状态
		String[] resetCodeStatus = new String[] {FieldUtil.FIELD_ACTIVITY_CANCEL, FieldUtil.FIELD_ACTIVITY_BLANKOUT};
		if(StringUtil.hasInArray(resetCodeStatus, status)) {
			tempUpdateHql += ",f.authenticode = null, f.verification = null";
		}

		if(status.equals(FieldUtil.FIELD_ACTIVITY_BLANKOUT)) {
			isCancel = true;
		}

		// 更新状态
		String[] activityStatus = new String[] {FieldUtil.FIELD_ACTIVITY_CANCEL, FieldUtil.FIELD_ACTIVITY_BLANKOUT,
												FieldUtil.FIELD_ACTIVITY_PRESERVING, FieldUtil.FIELD_ACTIVITY_UNPRESERVING};

		if(StringUtil.hasInArray(activityStatus, status)) {

			if(status.equals(FieldUtil.FIELD_ACTIVITY_BLANKOUT) 
					|| status.equals(FieldUtil.FIELD_ACTIVITY_PRESERVING)
					|| status.equals(FieldUtil.FIELD_ACTIVITY_CANCEL)
				) {
				realDbStatus = status;
			}
			// 取消保留改为未预定
			else if(status.equals(FieldUtil.FIELD_ACTIVITY_UNPRESERVING)) {
				realDbStatus = FieldUtil.FIELD_ACTIVITY_UNRESERVED;
			}

		}

		String whereHql = " where id in(:ids)";
		String updateSql =  tempUpdateHql + whereHql;

		Object execute = innerChangeType(fieldActivityIds, realDbStatus, updateSql);

		int rows = (Integer) execute;
		if(rows != -1) {
			result = true;
			if(isCancel) {
				log.info("把场地活动[" + fieldActivityIds + "]重置为状态<" + status + ">，并清空验证码");
			} else {
				log.info("把场地活动[" + fieldActivityIds + "]重置为状态<" + status + ">");
			}
		}
		return result;
	}

	/**
	 * 修改状态的内部实现
	 * @param fieldActivityIds
	 * @param status
	 * @param updateSql
	 * @return
	 */
	private Object innerChangeType(final String fieldActivityIds, final String status,
			final String updateSql) {
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String[] split = fieldActivityIds.split(",");
				Long[] longIds = new Long[split.length];
				for(int i = 0; i < split.length; i++) {
					longIds[i] = new Long(split[i]);
				}
				Query updateQuery = session.createQuery(updateSql);
				updateQuery.setParameterList("ids", longIds).setString("status", status);

				int dels = updateQuery.executeUpdate();
				return dels;
			}

		});
		return execute;
	}

	public boolean deleteOrderByActivity(final String fieldType, final String fieldActivityIds) throws FieldException {

		boolean result = false;

		if(fieldActivityIds == null) {
			return false;
		}
		
		String fieldActivityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
		final String deleteHql = "delete FieldOrder fo where fo.id in (select fa.fieldOrder.id from " + fieldActivityPojoName + " fa where fa.id in(:fids)) ";
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String[] split = fieldActivityIds.split(",");
				Long[] longIds = new Long[split.length];
				for(int i = 0; i < split.length; i++) {
					longIds[i] = new Long(split[i]);
				}
				Query updateQuery = session.createQuery(deleteHql);
				updateQuery.setParameterList("fids", longIds);

				int dels = updateQuery.executeUpdate();
				return dels;
			}

		});

		int rows = (Integer) execute;
		if(rows != -1) {
			result = true;
		}

		return result;
	}

	public boolean refundment(final String fieldType, final FieldOrder fieldOrder, final Double refundmentSum, final boolean deleteRecored) {
		boolean result = false;

		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 1、删除用卡记录
				/*if(deleteRecored) {
					String deleteHql = "delete CardUsageRecord where cardNo = "
						+ "(select fa.fieldOrder.userCode from "
						+ FieldUtil.getFieldActivityPojoName(fieldType) + " fa where fa.id = ?)";
					session.createQuery(deleteHql)
					.setParameter(0, new Long(fieldActivityId))
					.executeUpdate();
				}*/

				// 2、根据refundSum设置卡的余额
				String updateHql = "update MemberCard card set card.balance = card.balance + ?"
									+ " where card.id = ?";
				return session.createQuery(updateHql)
							.setParameter(0, refundmentSum)
							.setParameter(1, fieldOrder.getCardId())
							.executeUpdate();
			}
		});

		int rows = (Integer) execute;
		if(rows > 0) {
			log.debug("订单ID=" + fieldOrder.getId() + "，成功退款" + refundmentSum + "元至会员卡");
			result = true;
		} else if(rows == 0) {
			log.warn("场地活动ID=" + fieldOrder.getId() + "，退款时没有找到对应会员卡号");
		} else {
			log.warn("场地活动ID=" + fieldOrder.getId() + "，退款失败");
		}

		return result;
	}

	public List<? extends BaseField> getEnableField(Long venueId, String fieldType) throws FieldException {
		String fieldPojoName = FieldUtil.getFieldPojoName(fieldType);
		String hql = "from " + fieldPojoName + " where venueInfo.id = ? and status = ?";
		List<? extends BaseField> find = getHibernateTemplate().find(hql, new Object[] {venueId, "启用"});
		return find;
	}

	public List<? extends BaseField> getEnableField(String fieldType) throws FieldException {
		String fieldPojoName = FieldUtil.getFieldPojoName(fieldType);
		String hql = "from " + fieldPojoName + " where status = ?";
		List<? extends BaseField> find = getHibernateTemplate().find(hql, new Object[] {"启用"});
		return find;
	}

	public Integer deleteActivity(final Long venueId, final String fieldType, final Object[] usableDates) {
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String fieldActivityPojoName = null;
				try {
					fieldActivityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
				} catch (FieldException e) {
					log.error(e.getMessage(), e);
				}
				String hql = "DELETE FROM " + fieldActivityPojoName + " WHERE venueId = :vid"
							+ " AND usableDate in (:dates)";
				int executeUpdate = session.createQuery(hql)
						.setLong("vid", venueId)
						.setParameterList("dates", usableDates)
						.executeUpdate();
				return executeUpdate;
			}
		});
		return (Integer) execute;
	}

	public Long countVerificationStatusActivity(Long venueId, String fieldType, Date usableDate, boolean verificationStatus) throws FieldException {
		String fieldActivityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
		String hql = "select count(*) from " + fieldActivityPojoName + " f, FieldOrder o where f.venueId=?"
					+ " and f.fieldOrder.id=o.id and f.usableDate=? and verification=?";
		List<Long> find = getHibernateTemplate().find(hql, new Object[] {venueId, usableDate, verificationStatus});
		return find.get(0);
	}

	public Long countInvalidActivity(Long venueId, String fieldType, Date usableDate) throws FieldException {
		String fieldActivityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
		String hql = "select count(*) from " + fieldActivityPojoName + " where venueId=?"
					+ " and activity=? and usableDate=? and to_time<?";
		Object[] params = new Object[] {venueId, FieldUtil.FIELD_ACTIVITY_UNSTART, usableDate, DateUtil.getSystemTime()};
		List<Long> find = getHibernateTemplate().find(hql, params);
		return find.get(0);
	}

	public Long countMemberHasOrder(Long venueId, Date usableDate) throws FieldException {
		Long count = 0l;
		String[] enableFieldTypes = fieldTypeDao.getEnableFieldTypes(venueId);
		for (String fieldType : enableFieldTypes) {
			String fieldActivityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
			String hql = "select count(*) from " + fieldActivityPojoName + " where venueId=?"
						+ " and usableDate=? and fieldOrder is not null";
			Object[] params = new Object[] {venueId, usableDate};
			List<Long> find = getHibernateTemplate().find(hql, params);
			count += find.get(0);
		}
		
		return count;
	}

	public Long countFields(Long venueId, String fieldType) throws FieldException {
		String fieldPojoName = FieldUtil.getFieldPojoName(fieldType);
		String hql = "select count(*) from " + fieldPojoName + " where venueInfo.id=?";
		Object[] params = new Object[] {venueId};
		List<Long> find = getHibernateTemplate().find(hql, params);
		return find.get(0);
	}

}
