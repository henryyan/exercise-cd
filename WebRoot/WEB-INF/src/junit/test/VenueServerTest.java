package junit.test;

import net.yanhl.venue.service.VenueServer;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @author HenryYan
 *
 */
public class VenueServerTest extends BaseTest {
	
	VenueServer venueServer;

	@Before
	public void setUp() throws Exception {
		venueServer = (VenueServer) context.getBean("venueServer");
	}
	
	@Test
	public void test1() {
		System.out.println(venueServer.checkVenueNameExist("sdfdsf"));
	}
	
}
