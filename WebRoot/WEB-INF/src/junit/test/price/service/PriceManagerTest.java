package junit.test.price.service;

import junit.test.BaseTest;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.service.PriceManager;

import org.junit.Before;
import org.junit.Test;


public class PriceManagerTest extends BaseTest {

	PriceManager manager;

	@Before
	public void setUp() throws Exception {
		manager = (PriceManager) context.getBean("priceManager");
	}
	
	@Test
	public void hasPrice() throws PriceException {
		boolean hasPrice = manager.hasPrice(6l);
		System.out.println(hasPrice);
	}
	
}
