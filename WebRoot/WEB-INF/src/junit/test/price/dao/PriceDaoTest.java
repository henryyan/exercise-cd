package junit.test.price.dao;

import java.util.Map;

import junit.test.BaseTest;
import net.yanhl.price.dao.PriceDao;
import net.yanhl.price.exception.PriceException;

import org.junit.Before;
import org.junit.Test;

public class PriceDaoTest extends BaseTest {
	PriceDao dao;

	@Before
	public void setUp() throws Exception {
		dao = (PriceDao) context.getBean("priceDao");
	}
	
	@Test
	public void getCountPrice() throws PriceException {
		Map<String, Map<String, Long>> countPrice = dao.countPrice(1l);
		System.out.println(countPrice);
	}
	
}
