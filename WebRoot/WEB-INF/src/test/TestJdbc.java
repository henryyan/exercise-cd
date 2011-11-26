package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestJdbc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			Connection con = java.sql.DriverManager.getConnection(
					"jdbc:mysql://localhost/exercise?useUnicode=true&characterEncoding=UTF-8",
					"root", "root");
			String date = "2010-01-10";
			String sql = "select count(*) from t_tactics tm,t_tactics_date td where tm.id=1"
					+ " and tm.id = td.TACTICS_ID and td.FROM_DATE <= '" + date
					+ "' and td.TO_DATE >= '" + date + "'";
			System.out.println(sql);
			PreparedStatement pstmt = con.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery(sql);
			if (rs.next()) {
				System.out.println(rs.getInt(1));
			}

			// 关闭连接、释放资源
			rs.close();
			pstmt.close();
			con.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
