package net.yanhl.report.dao;

import java.sql.Date;
import java.util.List;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.report.ReportException;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.util.PaymentStyle;

/**
 * <p><b>Title：</b> 报表DAO接口</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090918
 */
public interface ReportDao extends BaseDao {
	
	/**
	 * 一天的报表
	 * @param venueId		场馆ID
	 * @param reportDate	查询报表日期
	 * @param activityType	活动类型：场地活动中的锻炼状态
	 * @return	封装为{@link net.yanhl.report.pojo.ReportDaily}对象的List
	 * @throws ReportException	查询报表时
	 */
	List<ReportDaily> reportDaily(long venueId, String reportDate, String activityType) throws ReportException;
	
	/**
	 * 执行资金报表存储过程：build_financial_statement
	 * @return 成功true，失败false
	 * @throws ReportException	执行存储过程时
	 */
	boolean updateCashReport() throws ReportException;
	
	/**
	 * 统计协议服务费
	 * @param venueId	场馆ID
	 * @param startDate	活动开始日期
	 * @param endDate	活动结束日期
	 * @param payStyles	支付方式
	 * @return	没有统计数字为NULL
	 * @throws ReportException
	 */
	List<Double> sumProtocolFee(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyles) throws ReportException;
	
	/**
	 * 统计所有活动的原始价格
	 * @param venueId	场馆ID
	 * @param startDate	活动开始日期
	 * @param endDate	活动结束日期
	 * @param payStyles	支付方式
	 * @return	没有统计数字为NULL
	 * @throws ReportException
	 */
	List<Double> sumActivityPrice(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyle) throws ReportException;

}
