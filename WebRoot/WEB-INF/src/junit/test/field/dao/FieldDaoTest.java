package junit.test.field.dao;

import junit.test.BaseTest;
import net.yanhl.field.dao.FieldDao;
import net.yanhl.field.util.FieldUtil;

import org.junit.Before;
import org.junit.Test;

public class FieldDaoTest extends BaseTest {

	FieldDao dao;

	@Before
	public void setUp() throws Exception {
		dao = (FieldDao) context.getBean("fieldDao");
	}

	@Test
	public void countVerificationActivity() throws Exception {
		Long countVerificationActivity = dao.countVerificationStatusActivity(1l,
				FieldUtil.FIELD_TYPE_BADMINTOON, new java.sql.Date(System.currentTimeMillis()),
				false);
		System.out.println(countVerificationActivity);
	}

	@Test
	public void countInvalidActivity() throws Exception {
		Long invalidActivity = dao.countInvalidActivity(1l, FieldUtil.FIELD_TYPE_BADMINTOON, new java.sql.Date(System.currentTimeMillis()));
		System.out.println(invalidActivity);
	}

	@Test
	public void countMemberHasOrder() throws Exception {
		Long invalidActivity = dao.countMemberHasOrder(1l, new java.sql.Date(System.currentTimeMillis()));
		System.out.println(invalidActivity);
	}

	@Test
	public void countFields() throws Exception {
		Long countFields = dao.countFields(1l, FieldUtil.FIELD_TYPE_BADMINTOON);
		System.out.println(countFields);
	}

}
