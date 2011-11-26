package junit.test.field.util;

import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.badminton.FieldBadmintoon;
import net.yanhl.field.util.FieldUtil;

import org.junit.Test;

public class FieldUtilTest {

	@Test
	public void test() throws ClassNotFoundException, FieldException {
		Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass("badmintoon");
		assert fieldPojoClass == FieldBadmintoon.class;
	}

}
