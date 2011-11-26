package junit.test.field.type.dao;

import junit.test.BaseTest;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.util.StringUtil;

import org.junit.Before;
import org.junit.Test;

public class FieldTypeDaoTest extends BaseTest {

	FieldTypeDao dao;

	@Before
	public void FieldTypeDao() throws Exception {
		dao = (FieldTypeDao) context.getBean("fieldTypeDao");
	}
	
	@Test
	public void getEnableFieldTypes() {
		String[] enableFieldTypes = dao.getEnableFieldTypes(5l);
		System.out.println(StringUtil.getValue(enableFieldTypes));
	}
	
}
