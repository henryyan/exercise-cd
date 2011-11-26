package net.yanhl.report.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.pojo.AccountOrder;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.pojo.activity.AllFieldActivity;
import net.yanhl.report.ReportException;
import net.yanhl.report.dao.ReportDao;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.report.pojo.VenueFinancial;
import net.yanhl.util.DateUtil;
import net.yanhl.util.PaymentStyle;
import net.yanhl.util.UserUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 报表相关Manager</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 */
@Service
@Transactional(readOnly = true)
public class ReportManager extends BaseManagerImpl {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected ReportDao reportDao;

	/**
	 * 一天的报表
	 * @param venueId		场馆ID
	 * @param reportDate	查询报表日期
	 * @param activityType	活动类型：场地活动中的锻炼状态
	 * @return	封装为{@link ReportDaily}对象的List
	 * @throws ReportException	查询报表时
	 */
	public List<ReportDaily> reportDaily(long venueId, String reportDate, String reportName) throws ReportException {
		return reportDao.reportDaily(venueId, reportDate, reportName);
	}

	/**
	 * <b>功能：</b>资金状况<br/>
	 * 查询前先执行更新资金报表的存储过程：<b>build_financial_statement</b>
	 * @param venueId		场馆ID
	 * @param reportDate	查询报表日期
	 * @return	封装为{@link VenueFinancial}对象的List
	 * @throws ReportException	查询报表时
	 */
	@SuppressWarnings("unchecked")
	public List<VenueFinancial> reportCash(String venueId, String reportDate) throws ReportException {

		// 1、执行资金报表存储过程
		try {
			boolean update = reportDao.updateCashReport();
			if (update) {
				log.debug("更新资金报表存储过程成功");
			}
		} catch (ReportException e) {
			e.printStackTrace();
			log.error("更新资金报表存储过程失败");
		}

		ListQuery listQuery = new ListQuery(VenueFinancial.class);
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });
		listQuery.getCustomFilter().add(new Object[] { "TDate", BaseQuery.EQ, reportDate });
		List<Object> reportCashList = find(listQuery);

		return (List<VenueFinancial>) reportCashList.get(1);
	}

	/**
	 * 场馆订单列表
	 * @return	封装为{@link AllFieldActivity}对象的List
	 * @throws ReportException	查询订单时
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<AllFieldActivity> orderItemList(ListQuery listQuery) throws ReportException {

		// 设置场馆ID
		HttpServletRequest request = listQuery.getRequest();
		String venueId = UserUtil.getCurrentVenueId(request);
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });
		String bookTime = request.getParameter("bookTime");
		if (StringUtils.isNotEmpty(bookTime)) {
			listQuery.getCustomFilter().add(new Object[] { "o.fieldOrder.bookTime", BaseQuery.LIKE, bookTime });
		}

		List<AllFieldActivity> activities = baseDao.find(listQuery);
		return activities;
	}

	/**
	 * 付款订单列表
	 * @param listQuery
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> accountOrderList(ListQuery listQuery) throws ReportException {
		// 设置场馆ID
		HttpServletRequest request = listQuery.getRequest();
		String venueId = UserUtil.getCurrentVenueId(request);
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });
		listQuery.getCustomFilter().add(new Object[] { "o.paymentStatus", BaseQuery.EQ, "1" });

		// 为每个付款订单设置场地订单数量
		List<Object> listResult = baseDao.find(listQuery);
		List<AccountOrder> accountOrders = (List<AccountOrder>) listResult.get(1);
		Map<Long, AccountOrder> accountOrderMapper = new HashMap<Long, AccountOrder>();
		for (AccountOrder accountOrder : accountOrders) {
			accountOrderMapper.put(accountOrder.getId(), accountOrder);
		}

		if (!accountOrders.isEmpty()) {
			String sql = "select tao.id, (select count(tfo.id) from t_field_order tfo where tfo.account_order_id = tao.id) ct from t_account_order tao"
					+ " where tao.id in (:ids) and tao.payment_status = '1'";
			Query query = getCurrentSession().createSQLQuery(sql).setParameterList("ids", accountOrderMapper.keySet());
			List<Object[]> list = query.list();
			for (Object[] objects : list) {
				Long accountOrderId = Long.valueOf(objects[0].toString());
				Integer fieldOrderSize = Integer.valueOf(objects[1].toString());
				AccountOrder accountOrder = accountOrderMapper.get(accountOrderId);
				accountOrder.setFieldOrderSize(fieldOrderSize);
			}
		}

		return listResult;
	}

	/**
	 * 协议服务费列表
	 * @return	封装为{@link AllFieldActivity}对象的List
	 * @throws ReportException	查询订单时
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<AllFieldActivity> protocolFee(ListQuery listQuery) throws ReportException {

		// 设置场馆ID
		HttpServletRequest request = listQuery.getRequest();
		String venueId = UserUtil.getCurrentVenueId(request);
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });
		String startDate = StringUtils.defaultIfEmpty(request.getParameter("startDate"), DateUtil.getSysdate());
		String endDate = request.getParameter("endDate");

		//listQuery.getCustomFilter().add(new Object[] {"o.fieldOrder.paymentCommision", BaseQuery.EQ, 1});
		listQuery.getCustomFilter().add(
				new Object[] { "o.fieldOrder.paymentStyle", BaseQuery.EQ, PaymentStyle.快钱.name() });
		listQuery.getCustomFilter().add(new Object[] { "o.usableDate", BaseQuery.GE, startDate });
		if (StringUtils.isNotEmpty(endDate)) {
			listQuery.getCustomFilter().add(new Object[] { "o.fieldOrder.bookTime", BaseQuery.LE, endDate });
		}

		List<AllFieldActivity> activities = baseDao.find(listQuery);
		return activities;
	}

	/**
	 * 统计协议服务费
	 * @param venueId	场馆ID
	 * @param startDate	活动开始日期
	 * @param endDate	活动结束日期
	 * @param payStyles	支付方式
	 * @return	没有统计数字为NULL
	 * @throws ReportException
	 */
	@Transactional(readOnly = true)
	public List<Double> sumProtocolFee(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyles)
			throws ReportException {
		return reportDao.sumProtocolFee(venueId, startDate, endDate, payStyles);
	}

	/**
	 * 统计所有活动的原始价格
	 * @param venueId	场馆ID
	 * @param startDate	活动开始日期
	 * @param endDate	活动结束日期
	 * @param payStyles	支付方式
	 * @return	没有统计数字为NULL
	 * @throws ReportException
	 */
	@Transactional(readOnly = true)
	public List<Double> sumActivityPrice(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyles)
			throws ReportException {
		return reportDao.sumActivityPrice(venueId, startDate, endDate, payStyles);
	}

	/**
	 * 通过付款订单查询活动订单
	 * @param accountOrderId	付款订单ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AllFieldActivity> findFieldOrder(Long accountOrderId) {
		// 1、查询活动订单ID
		Criteria criteria = getCurrentSession().createCriteria(FieldOrder.class);
		criteria.add(Restrictions.eq("accountOrderId", accountOrderId));
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.property("id"));
		List<Long> fieldOrderIdList = criteria.setProjection(projection).list();
		if (fieldOrderIdList.isEmpty()) {
			return new ArrayList<AllFieldActivity>();
		}
		
		criteria = getCurrentSession().createCriteria(AllFieldActivity.class);
		criteria.add(Restrictions.in("fieldOrder.id", fieldOrderIdList));
		return criteria.list();
	}

}
