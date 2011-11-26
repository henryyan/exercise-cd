package net.yanhl.report.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.report.ReportException;
import net.yanhl.report.dao.ReportDao;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.util.PaymentStyle;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 报表DAO Hibernate实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090919
 */
@SuppressWarnings("unchecked")
@Repository(value="reportDao")
public class ReportDaoHibernateImpl extends BaseDaoHibernate implements ReportDao {

	public List<ReportDaily> reportDaily(long venueId, String reportDate, String activityType)
			throws ReportException {
		List<ReportDaily> dailys = new ArrayList<ReportDaily>();
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
			conn.setAutoCommit(false);
			String proc = "{call report_daily(?,?,?)}";
			CallableStatement stmt = conn.prepareCall(proc);
			stmt.setLong(1, venueId);
			stmt.setString(2, reportDate);
			stmt.setString(3, activityType);

			/**
			 * 因为结果的列不固定，所以解决办法为获取元数据根据元数据中的列数循环
			 * 封装为一个临时对象放到结果集中
			 */

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			ReportDaily titles = new ReportDaily();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				if (columnName.equalsIgnoreCase("id")) {
					titles.setId("序号");
				} else if (columnName.equalsIgnoreCase("period")) {
					titles.setPeriod("时段");
				} else if (columnName.equalsIgnoreCase("status_total")) {
					titles.setTotal("运营场次");
				} else if (columnName.equalsIgnoreCase("sum_price")) {
					titles.setSumPrice("合计时段金额");
				} else {
					titles.getActivities().add(columnName);
				}
			}
			dailys.add(titles);

			while (rs.next()) {
				ReportDaily daily = new ReportDaily();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					if (columnName.equalsIgnoreCase("ID")) {
						daily.setId(rs.getString(columnName));
					} else if (columnName.equalsIgnoreCase("PERIOD")) {
						daily.setPeriod(rs.getString(columnName));
					} else if (columnName.equalsIgnoreCase("status_total")) {
						daily.setTotal(rs.getString(columnName));
					} else if (columnName.equalsIgnoreCase("sum_price")) {
						daily.setSumPrice(rs.getString("sum_price"));
					} else {
						daily.getActivities().add(rs.getString(columnName));
					}
				}
				dailys.add(daily);
			}
		} catch (Exception e) {
			throw new ReportException("获取每日报表出错", e);
		} finally {
			session.close();
		}
		return dailys;
	}

	public boolean updateCashReport() throws ReportException {
		boolean execute = false;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
			String proc = "{call build_financial_statement()}";
			CallableStatement stmt = conn.prepareCall(proc);
			execute = stmt.execute();
		} catch (Exception e) {
			throw new ReportException("更新现金报表出错", e);
		} finally {
			session.close();
		}
		return execute;
	}
	
	@Transactional(readOnly = true)
	public List<Double> sumProtocolFee(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyles) throws ReportException {
		String payStylesSql = createPayStyleSql(payStyles);
		String hql = "select sum(fo.paymentCommision) from AllFieldActivity o left join o.fieldOrder fo where o.venueId=? and o.usableDate>=?"
					+ " and fo.paymentStyle in (" + payStylesSql + ")";
		Object[] objects = new Object[] {venueId, startDate};
		if (endDate != null) {
			hql += " and o.usableDate <=?";
			objects = ArrayUtils.add(objects, endDate);
		}
		List<Double> find = getHibernateTemplate().find(hql, objects);
		return find;
	}
	
	@Transactional(readOnly = true)
	public List<Double> sumActivityPrice(Long venueId, Date startDate, Date endDate, PaymentStyle[] payStyles) throws ReportException {
		String payStylesSql = createPayStyleSql(payStyles);
		String hql = "select sum(fo.standardPrice) from AllFieldActivity o left join o.fieldOrder fo where o.venueId=? and o.usableDate>=?"
			+ " and fo.paymentStyle in (" + payStylesSql + ") and fo.paymentCommision > 0";
		Object[] objects = new Object[] {venueId, startDate};
		if (endDate != null) {
			hql += " and o.usableDate <=?";
			objects = ArrayUtils.add(objects, endDate);
		}
		List<Double> find = getHibernateTemplate().find(hql, objects);
		return find;
	}

	/**
	 * @param payStyles
	 */
	private String createPayStyleSql(PaymentStyle[] payStyles) {
		String payStylesSql = "";
		if (!ArrayUtils.isEmpty(payStyles)) {
			for (int i = 0; i < payStyles.length; i++) {
				payStylesSql += "'" + payStyles[i].name() + "'";
				if (i + 1 < payStyles.length) {
					payStylesSql += ",";
				}
			}
		}
		return payStylesSql;
	}

}
