package test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import net.yanhl.report.pojo.ReportDaily;

import org.hibernate.Session;

/**
 * <p><b>Title：</b> </p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
public class ReportTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		long venueId = 9;
		String reportDate = "2009-09-19";
		String reportName = "锻炼";
		Session session = HibernateSessionFactory.getSession();
		Connection conn = session.connection();
		conn.setAutoCommit(false);
		String proc = "{call report_daily(?,?,?)}";
		CallableStatement stmt = conn.prepareCall(proc);
		stmt.setLong(1, venueId);
		stmt.setString(2, reportDate);
		stmt.setString(3, reportName);
		ResultSet rs = stmt.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		ReportDaily titles = new ReportDaily();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			if (i == 1) {
				titles.setId(columnName);
			} else if (i == 2) {
				titles.setPeriod(columnName);
			} else if (i == columnCount) {
				titles.setTotal(columnName);
			} else {
				titles.getActivities().add(columnName);
			}
		}
		
		List<ReportDaily> dailys = new ArrayList<ReportDaily>();
		while (rs.next()) {
			ReportDaily daily = new ReportDaily();
			for (int i = 1; i <= columnCount; i++) {
				// System.out.print(metaData.getColumnName(i) + " " +
				// rs.getObject(i) + "\t");
				String columnName = metaData.getColumnName(i);
				if (columnName.equalsIgnoreCase("ID")) {
					daily.setId(rs.getString(columnName));
				} else if (columnName.equalsIgnoreCase("PERIOD")) {
					daily.setPeriod(rs.getString(columnName));
				} else if (columnName.equalsIgnoreCase("total")) {
					daily.setTotal(rs.getString(columnName));
				} else {
					daily.getActivities().add(rs.getString(columnName));
				}
			}
			dailys.add(daily);
		}

		System.out.println(dailys);

	}

}
