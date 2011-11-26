package junit.test.field.type.service;

import java.util.List;
import java.util.Set;

import junit.test.BaseTest;
import net.yanhl.field.exception.FieldTypeException;
import net.yanhl.field.pojo.type.FieldType;
import net.yanhl.field.pojo.type.VenueInfoFieldTypeLink;
import net.yanhl.field.service.FieldTypeManager;

import org.junit.Before;
import org.junit.Test;

public class FieldTypeServiceTest extends BaseTest {

	FieldTypeManager manager;

	@Before
	public void setUp() throws Exception {
		manager = (FieldTypeManager) context.getBean("fieldTypeManager");
	}
	
	@Test
	public void linkFieldType() {
		try {
			manager.linkFieldType(5l, 1l);
		} catch (FieldTypeException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void unLinkFieldType() {
		manager.unLinkFieldType(5l, 1l);
	}
	
	@Test
	public void loadVenueFieldLink() {
		List<VenueInfoFieldTypeLink> findBy = manager.findBy(VenueInfoFieldTypeLink.class, new String[] { "venueInfo.id",
		"fieldType.id" }, new Object[] { 5l, 1l });
		for (VenueInfoFieldTypeLink link : findBy) {
			System.out.println(link.getFieldType());
		}
	}
	
	@Test
	public void loadFieldType() {
		FieldType fieldType = (FieldType) manager.get(FieldType.class, 1l);
		System.out.println(fieldType);
		Set<VenueInfoFieldTypeLink> venueInfoFieldTypeLinks = fieldType.getVenueInfoFieldTypeLinks();
		System.out.println(venueInfoFieldTypeLinks.size());
		for (VenueInfoFieldTypeLink venueInfoFieldTypeLink : venueInfoFieldTypeLinks) {
			if (venueInfoFieldTypeLink.getVenueInfo().getId() == 5) {
				System.out.println(venueInfoFieldTypeLink.getVenueInfo());
			}
		}
	}
	
	@Test
	public void getEnableFieldTypes() {
		String[] enableFieldTypes = manager.getEnableFieldTypes(5l);
		System.out.println(enableFieldTypes);
	}
	
}
