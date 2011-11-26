package net.yanhl.tactics.dao.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.tactics.dao.TacticsDao;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsPrice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

/**
 * <p><b>Title：</b> 价格策略DAO，Hibernate实现类</p>
 * <p><b>Description：</b>利用Hibernate框架实现接口功能</p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091218
 */
@SuppressWarnings("unchecked")
@Repository("tacticsDao")
public class TacticsDaoHibernateImpl extends BaseDaoHibernate implements TacticsDao {

	Log log = LogFactory.getLog(this.getClass());

	public List<TacticsPrice> getTacticsPriceList(Long venueId, String fieldType) {
		String hql = "select p from TacticsPrice p, Tactics o where o.id = p.tactics.id and o.venueId=? and o.fieldType=?";
		List<TacticsPrice> find = getHibernateTemplate().find(hql, new Object[] {venueId, fieldType});
		return find;
	}

	public List<Integer> activityBetweenTacticsDate(final Long venueId, final Long tacticsId,
			final String fieldType, final List<Date> tacticsDates) {
		List<Integer> executeFind = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				if (tacticsDates.isEmpty()) {
					return new ArrayList<Integer>();
				}

				String activityTable = null;
				try {
					activityTable = FieldUtil.getFieldActivityTableName(fieldType);
				} catch (FieldException e) {
				}
				String sql = ""
					+ "SELECT DISTINCT tba.id "
					+ "FROM   " + activityTable + " tba, "
					+ "       t_field_order tr "
					+ "WHERE  tba.usable_date IN (:dates) "
					+ "       AND tba.order_id = tr.id "
					+ "       AND tba.venue_id = :vid";
				return session.createSQLQuery(sql)
						.setParameterList("dates", tacticsDates)
						.setLong("vid", venueId)
						.list();
			}

		});
		return executeFind;
	}

	public Integer copyBookedActivityToTacticsActivity(final List<Integer> activityIdList, final Long tacticsId, final String fieldType) {
		if (activityIdList.isEmpty()) {
			return 0;
		}
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String activityTable;
				String tacticsActivityTable;
				try {
					activityTable = FieldUtil.getFieldActivityTableName(fieldType);
					tacticsActivityTable = FieldUtil.getFieldActivityTacticsTableName(fieldType);
				} catch (FieldException e) {
					throw new RuntimeException(e);
				}

				String updateSql = "update " + tacticsActivityTable + " set tactics_id =" + tacticsId
								+ " where id=?";

				int executeCounter = 0;
				String sql = "insert into " + tacticsActivityTable + " select * from " + activityTable + " where id=?";
				for (Integer activityId : activityIdList) {
					executeCounter += session.createSQLQuery(sql).setLong(0, activityId).executeUpdate();
					session.createSQLQuery(updateSql).setLong(0, activityId).executeUpdate();
				}

				return executeCounter;
			}
		});

		return (Integer) execute;

	}

	
	public List<java.sql.Date> getUsableDate(final Long venueId, final Long tacticsId, final String fieldType) {
		List<java.sql.Date> dates = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String activityTable;
				try {
					activityTable = FieldUtil.getFieldActivityTableName(fieldType);
				} catch (FieldException e) {
					throw new RuntimeException(e);
				}
				String sql = ""
					+ "SELECT DISTINCT tba.usable_date "
					+ "FROM   " + activityTable + " tba, "
					+ "       t_tactics tt, "
					+ "       t_tactics_date td "
					+ "WHERE  tt.id = ?"
					+ "       AND tba.tactics_id = tt.id "
					+ "       AND tt.id = td.tactics_id "
					+ "       AND tba.venue_id = ?";
				List<java.sql.Date> list = session.createSQLQuery(sql)
				.setLong(0, tacticsId)
				.setLong(1, venueId)
				.list();

				if (list == null || list.isEmpty()) {
					list = new ArrayList<java.sql.Date>();
				}

				return list;

			}

		});
		return dates;
	}

	public List<java.sql.Date[]> getTacticsDates(Long tacticsId) {
		String hql = "SELECT td.fromDate, td.toDate FROM Tactics tm, TacticsDate td "
					+ "WHERE tm.id = td.tactics.id "
					+ "AND tm.id = ?";
		List<java.sql.Date[]> find = getHibernateTemplate().find(hql, new Object[] {tacticsId});
		return find;
	}

	public List<Integer> repeatBookActivity(final Long venueId, final Long tacticsId, final String fieldType) {

		Object execute = getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String activityTable = null;
				String tacticsActivityTable = null;
				try {
					activityTable = FieldUtil.getFieldActivityTableName(fieldType);
					tacticsActivityTable = FieldUtil.getFieldActivityTacticsTableName(fieldType);
				} catch (FieldException e) {
					throw new RuntimeException(e);
				}

				String condition = " where a.usable_date = b.usable_date AND a.from_time = b.from_time" +
									" AND a.to_time = b.to_time AND b.order_id is not null" +
									" AND a.field_id = b.field_id AND a.venue_id = ? and b.tactics_id = ?";

				String updateSql = "update " + activityTable + " a, " + tacticsActivityTable + " b" +
									" set a.order_id = b.order_id, a.activity = b.activity, a.AUTHENTICODE = b.AUTHENTICODE," +
									" a.VERIFICATION = b.VERIFICATION, a.TACTICS_ID = b.TACTICS_ID," +
									" a.ORDER_USER = b.ORDER_USER, a.price = b.price" + condition;
				int executeUpdate = session.createSQLQuery(updateSql)
						.setLong(0, venueId)
						.setLong(1, tacticsId)
						.executeUpdate();
				System.out.println("更新了" + executeUpdate + "条");

				String findTacticsActivityIds = "select b.id from " + activityTable + " a, " + tacticsActivityTable + " b" + condition;
				List<Integer> ids = session.createSQLQuery(findTacticsActivityIds)
						.setLong(0, venueId)
						.setLong(1, tacticsId)
						.list();
				return ids;
			}

		});

		return (List<Integer>) execute;
	}

	public Integer deleteTacticsActivity(final Long venueId, final Long tacticsId, final String fieldType) {

		Object resultSize = getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String tacticsActivityTable;
				try {
					tacticsActivityTable = FieldUtil.getFieldActivityTacticsTableName(fieldType);
				} catch (FieldException e) {
					throw new RuntimeException(e);
				}

				String updateSql = "delete from " + tacticsActivityTable +
								" where VENUE_ID = ? and TACTICS_ID = ?";
				return session.createSQLQuery(updateSql)
							.setLong(0, venueId)
							.setLong(1, tacticsId)
							.executeUpdate();
			}

		});

		return (Integer) resultSize;

	}

	public List<Tactics> findTactics(Long venueId, String fieldType) {
		String hql = "from Tactics where venueId = ? and fieldType = ?";
		Object[] values = new Object[] {venueId, fieldType};
		List<Tactics> tactics = getHibernateTemplate().find(hql, values);
		return tactics;
	}

	public List<? extends FieldActivity> findActivity(Long venueId, Long tacticsId, String fieldType) throws FieldException {
		String activityPojoName = FieldUtil.getFieldActivityPojoName(fieldType);
		String hql = " from " + activityPojoName + " where venueId = ? and tacticsId = ?";
		Object[] values = new Object[] {venueId, tacticsId};
		List<? extends FieldActivity> activitys = getHibernateTemplate().find(hql, values);
		return activitys;
	}

}
