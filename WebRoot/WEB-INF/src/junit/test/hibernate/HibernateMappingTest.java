package junit.test.hibernate;

import java.util.Map;

import junit.test.BaseTest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Before;
import org.junit.Test;

public class HibernateMappingTest extends BaseTest {

	private SessionFactory sessionFactory;

	@Before
	public void setUp() throws Exception {
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
	}

	@Test
	public void allClassMapping() throws Exception {
		Session session = sessionFactory.openSession();

		try {
			Map<?, ?> metadata = sessionFactory.getAllClassMetadata();
			for (Object o : metadata.values()) {
				EntityPersister persister = (EntityPersister) o;
				String className = persister.getEntityName();
				Query q = session.createQuery("from " + className + " c");
				q.iterate();
				System.out.println("ok: " + className);
			}
		} finally {
			session.close();
		}
	}

}
