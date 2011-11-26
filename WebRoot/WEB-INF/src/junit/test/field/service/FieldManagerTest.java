package junit.test.field.service;

import junit.test.BaseTest;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.service.FieldManager;

import org.junit.Before;
import org.junit.Test;

public class FieldManagerTest extends BaseTest {

	FieldManager manager;
	FieldActivityEngine fieldActivityEngine;

	@Before
	public void setUp() throws Exception {
		manager = (FieldManager) context.getBean("fieldManager");
		fieldActivityEngine = (FieldActivityEngine) context.getBean("fieldActivityEngine");
	}

	@Test
	public void createActivityForDefault() {
		try {
			System.out.println(manager);
			fieldActivityEngine.insertActivityUseDefault(2l, java.sql.Date.valueOf("2010-05-17"), java.sql.Date.valueOf("2010-05-25"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void countAllFields() throws FieldException {
		Long countAllFields = manager.countAllFields(1l);
		System.out.println(countAllFields);
	}

	@Test
	public void createActivity() throws Exception {
		fieldActivityEngine.createActivityForServlet();
	}

}
