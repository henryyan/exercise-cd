package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.field.pojo.badminton.FieldBadmintoonActivity;

import org.springframework.context.ApplicationContext;

public class TestActivityGrid {

	@SuppressWarnings("unchecked")
	private static Map<String, List<Object[]>> getGrid(Long venueId, String usableDate) {
//		Map<String, List<Object[]>> result = new HashMap<String, List<Object[]>>();
		
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
		
		ApplicationContext context = SpringUtil.getContext();
		BaseDaoHibernate bean = (BaseDaoHibernate) context.getBean("baseDao");
		String hql = "from FieldBadmintoonActivity where venueId=" + venueId + " and usableDate='"
				+ usableDate + "' order by fromTime";
		List<FieldBadmintoonActivity> find = bean.find(hql);

		List<Object[]> singleActivity = null;
		for (FieldBadmintoonActivity activity : find) {
			Object[] activityObj = new Object[] {activity.getId(), activity.getFieldName(), activity.getActivity()};
			if (!result.containsKey(activity.getPeriod())) {
				singleActivity = new ArrayList<Object[]>();
				result.put(activity.getPeriod(), singleActivity);
			} else {
				singleActivity = result.get(activity.getPeriod());
			}
			singleActivity.add(activityObj);
		}
		System.out.println(result);
		// System.out.println(find);
		return null;
	}

	public static void main(String[] args) {
		getGrid(7l, "2009-09-20");

	}

}
