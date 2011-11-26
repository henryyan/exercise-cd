package junit.test;

import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;

import test.SpringUtil;

public class BaseTest {

	protected static ApplicationContext context;
	protected static SessionFactory sessionFactory;

	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			context = SpringUtil.getContext();
			sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
