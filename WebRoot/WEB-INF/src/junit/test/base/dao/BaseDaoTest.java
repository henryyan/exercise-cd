package junit.test.base.dao;

import java.util.HashMap;
import java.util.Map;

import junit.test.BaseTest;
import net.yanhl.base.dao.BaseDao;
import net.yanhl.tactics.pojo.TacticsPrice;

import org.junit.Before;
import org.junit.Test;


public class BaseDaoTest extends BaseTest {
	
	BaseDao baseDao = null;

	@Before
	public void before() {
		baseDao = (BaseDao) context.getBean("baseDao");
	}
	
	@Test
	public void testDao() {
		System.out.println(baseDao);
	}
	
	@Test
	public void deleteAllForMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tactics.id", 3l);
		//params.put("tactics.fieldType", "tennis");
		Integer deleteAll = baseDao.deleteAll(TacticsPrice.class, params);
		System.out.println(deleteAll);
	}
	
	public static void main(String[] args) {
		String a= "ttt.id.aa";
		System.out.println(a.replaceAll("\\.", ""));
	}
	
}
