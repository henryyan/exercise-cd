package net.yanhl.venue.dao;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.venue.pojo.BusinessInfo;
import net.yanhl.venue.pojo.VenueUser;


public interface VenueDao extends BaseDao {
	
	/**
	 * 根据姓名和密码查找用户
	 * @param name
	 * @param pass
	 * @return 
	 */
	VenueUser findByNameAndPass(String name, String pass);
	
	/**
	 * 查找工商信息
	 * @param venueInfoId
	 * @return
	 */
	BusinessInfo loadBusinessInfo(long venueInfoId);
	
	/**
	 * 设置图片路径
	 * @param id
	 * @return
	 */
	int setVenuePicture(Long venueInfoId, String pictureRealName);
}
