package junit.test.base.service;

import junit.test.BaseTest;
import net.yanhl.base.service.BaseManager;

import org.junit.Before;
import org.junit.Test;

public class BaseManagerTest extends BaseTest {

	BaseManager baseManager = null;

	@Before
	public void before() {
		baseManager = (BaseManager) context.getBean("baseManager");
	}
	
	@Test
	public void testManager() {
		System.out.println(baseManager);
	}
	
}
