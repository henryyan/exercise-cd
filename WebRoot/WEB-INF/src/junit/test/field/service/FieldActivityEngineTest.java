package junit.test.field.service;

import java.sql.Date;

import junit.test.BaseTest;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.exception.PriceException;

import org.junit.Before;
import org.junit.Test;

public class FieldActivityEngineTest extends BaseTest {

	FieldActivityEngine fieldActivityEngine;

	@Before
	public void setUp() throws Exception {
		fieldActivityEngine = (FieldActivityEngine) context.getBean("fieldActivityEngine");
	}
	
	@Test
	public void createActivityForServlet() {
		try {
			fieldActivityEngine.createActivityForServlet();
		} catch (FieldException e) {
			e.printStackTrace();
		} catch (FieldActivityException e) {
			e.printStackTrace();
		} catch (PriceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createActivityAppointFieldType() {
		try {
			fieldActivityEngine.createActivity(5l, FieldUtil.FIELD_TYPE_TENNIS, java.sql.Date.valueOf("2010-06-05"));
		} catch (FieldException e) {
			e.printStackTrace();
		} catch (FieldActivityException e) {
			e.printStackTrace();
		} catch (PriceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createActivity() {
		try {
			fieldActivityEngine.createActivity(5l, java.sql.Date.valueOf("2010-06-05"));
		} catch (FieldException e) {
			e.printStackTrace();
		} catch (FieldActivityException e) {
			e.printStackTrace();
		} catch (PriceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void insertActivityUseDefault() {
		try {
			fieldActivityEngine.insertActivityUseDefault(6l, Date.valueOf("2010-05-27"), Date.valueOf("2010-06-01"));
		} catch (FieldException e) {
			e.printStackTrace();
		} catch (FieldActivityException e) {
			e.printStackTrace();
		} catch (PriceException e) {
			e.printStackTrace();
		}
	}
	
}
