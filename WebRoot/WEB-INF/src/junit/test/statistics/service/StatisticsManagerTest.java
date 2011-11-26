package junit.test.statistics.service;

import java.util.Map;

import junit.test.BaseTest;
import net.sf.json.JSONObject;
import net.yanhl.statistics.service.StatisticsManager;

import org.junit.Before;
import org.junit.Test;


public class StatisticsManagerTest extends BaseTest {

	StatisticsManager manager;

	@Before
	public void setUp() throws Exception {
		manager = (StatisticsManager) context.getBean("statisticsManager");
	}

	@Test
	public void getStatisticsDatas() throws Exception {
		Map<String, Object> datas = manager.getStatisticsDatas(1l);
		System.out.println(datas);
		System.out.println(JSONObject.fromObject(datas));
	}

}
