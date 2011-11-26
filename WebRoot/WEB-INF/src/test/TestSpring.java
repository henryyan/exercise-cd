package test;

import java.util.HashMap;
import java.util.Map;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.information.pojo.Information;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestSpring {
	public static void main(String[] args) {
//		ApplicationContext ctx = new FileSystemXmlApplicationContext(
//				new String[] { "D:/epiboly/projects/exercise/WebRoot/WEB-INF/config/applicationContext.xml",
//						"D:/epiboly/projects/exercise/WebRoot/WEB-INF/config/daoContext.xml",
//						"D:/epiboly/projects/exercise/WebRoot/WEB-INF/config/action-servlet.xml"});
//		System.out.println(ctx);
		
		ApplicationContext ctx =
		    new FileSystemXmlApplicationContext("classpath:/WEB-INF/config/applicationContext.xml");
		System.out.println(ctx);

		
//		TacticsManager tacticsManager = (TacticsManager) ctx.getBean("tacticsManager");
//		
//		Date fromDate = Date.valueOf("2010-04-05");
//		Date toDate = Date.valueOf("2010-05-01");
//		try {
//			JSONObject repeatDates = tacticsManager.checkRepeatDates(1l, fromDate , toDate);
//			System.out.println(repeatDates);
//		} catch (TacticsDateException e) {
//			e.printStackTrace();
//		}
//		try {
//			tacticsManager.deleteAndBakActivity(39l, 6l, "badmintoon");
//		} catch (TacticsException e) {
//			e.printStackTrace();
//		} catch (FieldException e) {
//			e.printStackTrace();
//		}
		
//		FieldManager fieldManager = (FieldManager) ctx.getBean("fieldManager");
//		java.sql.Date fromCreateDate = java.sql.Date.valueOf("2010-01-15");
//		java.sql.Date toCreateDate = java.sql.Date.valueOf("2010-01-18");
//		try {
//			fieldManager.createActivity(7l, fromCreateDate, toCreateDate);
//		} catch (FieldException e) {
//			e.printStackTrace();
//		}
		
//		TacticsDao tacticsDao = (TacticsDao) ctx.getBean("tacticsDao");
//		List<? extends FieldActivity> findActivity = tacticsDao.findActivity(7l, 12l, "badmintoon");
//		System.out.println(findActivity.size());
//		List<Integer> repeatBookActivity = tacticsDao.repeatBookActivity(22l, 2l, "badmintoon");
//		System.out.println(repeatBookActivity);
//		Date[] datePeriod = tacticsDao.getDatePeriod(7l, 1l, "badmintoon");
//		System.out.println(datePeriod[0]);
//		System.out.println(datePeriod[1]);
//		tacticsDao.repeatBookActivity(7l, 1l, "badmintoon");
		
//		List<Date[]> tacticsDates = tacticsDao.getTacticsDates(1l);
//		for (Object[] dates : tacticsDates) {
//			System.out.println(dates[0] + "\t" + dates[1]);
//		}
		
//		BaseManager baseManager = (BaseManager) ctx.getBean("baseManager");
//		int update = baseManager.update(Information.class, new String[] {"infoLabel"}, new Object[] {"nsfds"}, 
//				new String[] {"id"} , new Object[] {new Long[] {2l,3l,4l}});
//		System.out.println(update);
//		baseManager.deleteAll(FieldBadmintoonActivity.class, "usableDate", 
//				new Object[] { java.sql.Date.valueOf("2010-01-19"), java.sql.Date.valueOf("2010-01-20") });
		
		BaseDao baseDao = (BaseDao) ctx.getBean("baseDao");
		Map<String, Object> setKVMap = new HashMap<String, Object>();
		setKVMap.put("infoLabel", "ssssssss");
		
		Map<String, Object[]> filterKVMap = new HashMap<String, Object[]>();
		filterKVMap.put("id", new Object[] {BaseQuery.GT, "3"});
		
		int update = baseDao.update(Information.class, setKVMap, filterKVMap);
		System.out.println(update);
		/*HibernateTemplate template = baseDao.getHibernateTemplate();
		System.out.println(template);
		
		java.sql.Date d1 = java.sql.Date.valueOf("2010-01-25");
		java.sql.Date d2 = java.sql.Date.valueOf("2010-01-26");
		java.sql.Date d3 = java.sql.Date.valueOf("2010-01-27");
		java.sql.Date d4 = java.sql.Date.valueOf("2010-01-28");
		
		final List<java.sql.Date> dates = new ArrayList<java.sql.Date>();
		dates.add(d1);
		dates.add(d2);
		dates.add(d3);
		dates.add(d4);
		
		template.executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String varname1 = ""
					+ "SELECT DISTINCT tba.id "
					+ "FROM   t_field_badmintoon_activity tba, "
					+ "       t_field_order tr "
					+ "WHERE  tba.usable_date IN (:dates) "
					+ "       AND tba.order_id = tr.id "
					+ "       AND tba.venue_id = :vid";


				List list = session.createSQLQuery(varname1).setParameterList("dates", dates).setLong("vid", 39l).list();
				System.out.println(list);
				return list;
			}
		});*/
		
//		FieldDao fieldDao = (FieldDao) ctx.getBean("fieldDao");
//		List<? extends BaseField> enableField = fieldDao.getEnableField(39l, "badmintoon");
//		System.out.println(enableField.size());
//		System.out.println(enableField);
		
	}
}
