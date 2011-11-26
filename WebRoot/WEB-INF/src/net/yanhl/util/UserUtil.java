package net.yanhl.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

/**
 * <p><b>Title：</b> 用户工具类</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20080504
*/
public class UserUtil {
	
	/**
	 * 保存到session中用户对象的name
	 */
	public static final String LOGIN_USER = "user";
	
	/**
	 * 登录标志
	 */
	public static final String FORWARD_LOGIN = "login";
	
	/**
	 * 场馆向导页面
	 */
	public static final String FORWARD_VENUE = "wizard_venue";
	
	/**
	 * 场馆客服认证页面
	 */
	public static final String FORWARD_VERIFY = "wizard_verify";
	
	/**
	 * 场地向导
	 */
	public static final String FORWARD_FIELD = "wizard_field";
	
	
	/**
	 * 把用户保存到Session
	 * 
	 * @param user
	 * @param request
	 */
	public static void saveUser2Session(VenueUser user, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_USER, user);
	}
	
	/**
	 * 登出系统
	 * 
	 * @param user
	 * @param request
	 */
	public static void logoutUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(LOGIN_USER);
	}

	/**
	 * 用户是否登录
	 * 
	 * @param request
	 * @return boolean
	 */
	public static boolean isLogin(HttpServletRequest request) {
		VenueUser user = getUserFromSession(request);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 从Session范围取用户
	 * 
	 * @param request
	 * @return user
	 */
	public static VenueUser getUserFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		VenueUser user = null;
		user = (VenueUser) session.getAttribute(LOGIN_USER);
		return user;
	}
	
	/**
	 * 得到当前登录用户的ID
	 * @param request
	 * @return
	 */
	public static String getCurrentUserId(HttpServletRequest request) {
		return String.valueOf(getUserFromSession(request).getId());
	}
	
	/**
	 * 得到当前登录用户的姓名
	 * @param request
	 * @return
	 */
	public static String getCurrentUserName(HttpServletRequest request) {
		return String.valueOf(getUserFromSession(request).getUsername());
	}
	
	/**
	 * 得到当前场馆
	 * @param request
	 * @return
	 */
	public static VenueInfo getCurrentVenueInfo(HttpServletRequest request) {
		VenueUser userFromSession = getUserFromSession(request);
		if(userFromSession == null || userFromSession.getVenueInfo() == null) {
			return null;
		}
		
		return userFromSession.getVenueInfo();
	}
	
	/**
	 * 得到当前场馆ID
	 * @param request
	 * @return
	 */
	public static String getCurrentVenueId(HttpServletRequest request) {
		VenueUser userFromSession = getUserFromSession(request);
		if(userFromSession == null || userFromSession.getVenueInfo() == null) {
			return "";
		}
		
		String venueInfoId = userFromSession.getVenueInfo().getId().toString();
		return venueInfoId;
	}
	
	/**
	 * 得到当前场馆ID
	 * @param request
	 * @return
	 */
	public static Long getCurrentVenueLongId(HttpServletRequest request) {
		VenueUser userFromSession = getUserFromSession(request);
		if(userFromSession == null || userFromSession.getVenueInfo() == null) {
			return 0l;
		}
		
		Long venueInfoId = userFromSession.getVenueInfo().getId();
		return venueInfoId;
	}
	
	
}
