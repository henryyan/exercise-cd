package net.yanhl.venue.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p><b>Title：场馆工具类</b></p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public class VenueUtil {

	/**
	 * 场馆.未验证标志
	 */
	public static final String NO_VALIDATED = "0";
	
	/**
	 * 获取场馆图片路径
	 * @param venueId	场馆ID
	 * @param servletContext
	 * @return	图片目录的绝对路径
	 */
	public static String getVenuePicturePath(String venueId, HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String picturesPath = servletContext.getRealPath("/venue/pictures/" + venueId);
		return picturesPath;
	}
	
}
