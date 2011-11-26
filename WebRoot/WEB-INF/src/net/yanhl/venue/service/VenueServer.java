package net.yanhl.venue.service;

import javax.annotation.Resource;
import javax.jws.WebService;

import net.sf.json.JSONObject;
import net.yanhl.venue.pojo.VenueInfo;

import org.springframework.stereotype.Component;

@WebService
@Component(value = "venueServer")
public class VenueServer {
	
	@Resource
	protected VenueManager venueManager;

	public String checkVenueNameExist(String venueName) {
		JSONObject result = new JSONObject();
		try {
			VenueInfo venueInfo = venueManager.getVenueByName(venueName);
			if(venueInfo == null) {
				result.accumulate("exist", false);
			} else {
				result.accumulate("exist", true);
				result.accumulate("infoId", venueInfo.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
}
