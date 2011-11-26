package net.yanhl.venue.service;

import net.yanhl.base.exception.EmptyValueException;
import net.yanhl.venue.exception.VenueException;
import net.yanhl.venue.pojo.BusinessInfo;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

/**
 * <p><b>Title：</b> 场馆业务接口</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090528
 */
public interface VenueManager {
	
	/**
	 * 根据姓名和密码查找用户
	 * @param name	场馆用户名
	 * @param pass	场馆用户密码
	 * @return 	有匹配的用户和密码返回{@link net.yanhl.venue.pojo.VenueUser}对象，否则返回null
	 */
	VenueUser getUserByNameAndPass(String name,String pass);
	
	
	/**
	 * 检查指定场馆用户名是否已存在
	 * @param venueUserName	场馆用户名
	 * @return	存在true，不存在false
	 * @throws VenueException	数据查询时
	 * @throws EmptyValueException	给定用户名为空时
	 */
	boolean checkUserExist(String venueUserName) throws VenueException, EmptyValueException;
	
	/**
	 * 通过场馆名称查找匹配场馆信息
	 * @param venueName	场馆名称
	 * @return	有匹配结果返回{@link net.yanhl.venue.pojo.VenueInfo}对象，否则返回null
	 * @throws EmptyValueException venueName为空时
	 */
	VenueInfo getVenueByName(String venueName) throws EmptyValueException;
	
	/**
	 * 查找工商信息
	 * @param venueInfoId	场馆信息ID
	 * @return	有匹配的用户和密码返回{@link net.yanhl.venue.pojo.BusinessInfo}对象，否则返回null
	 */
	BusinessInfo loadBusinessInfo(long venueInfoId);
	
	/**
	 * 查询场馆的主图片
	 * @param venueInfoId
	 * @return
	 */
	String getVenuePicture(Long venueInfoId);

	/**
	 * 设置图片路径
	 * @param id
	 * @return
	 */
	int setVenuePicture(Long venueInfoId, String pictureRealName);
	
}
