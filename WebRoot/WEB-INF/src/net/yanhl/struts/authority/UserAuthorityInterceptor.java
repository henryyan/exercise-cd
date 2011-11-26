package net.yanhl.struts.authority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueUser;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b> 判断用户是不是已经登陆</p>
 * <p><b>Description：</b></p>
 * <p>Copyright: Copyright (c) 2008</p>
 * 
 * @author 闫洪磊
 * @version 1.0.0.20080523
 */
public class UserAuthorityInterceptor implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		ActionMapping mapping = null;
		Object[] args = invocation.getArguments();
		//转换各种对象
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
			} else if (args[i] instanceof HttpServletResponse) {
				response = (HttpServletResponse) args[i];
			} else if (args[i] instanceof ActionMapping) {
				mapping = (ActionMapping) args[i];
			}
		}
		if (UserUtil.isLogin(request)) {
			
			/*
			 * 没有通过客服认证的跳转到需要客服认证页面
			 * 根据请求类型判断：
			 * 1、如果是AJAX的输出一个login字符串
			 * 2、如果是普通页面直接跳转到登录页面
			 */
			VenueUser user = UserUtil.getUserFromSession(request);
			if(user.getVenueInfo() != null && (user.getVenueInfo().getVerification() == null 
					|| user.getVenueInfo().getVerification() == false)) {
				String requestType = request.getHeader("X-Requested-With");
				if(requestType != null && requestType.equals("XMLHttpRequest")) {
					response.getWriter().print(UserUtil.FORWARD_VERIFY);
					return null;
				} else {
					return mapping.findForward(UserUtil.FORWARD_VERIFY);
				}
			}
			return invocation.proceed();
			
		} else {
			/* 
			 * 判断是哪种类型的请求(普通URL、AJAX)
			 * 如果是AJAX的输出一个login字符串
			 * 如果是普通页面直接跳转到登录页面
			*/
			String requestType = request.getHeader("X-Requested-With");
			if(requestType != null && requestType.equals("XMLHttpRequest")) {
				response.getWriter().print(UserUtil.FORWARD_LOGIN);
				return null;
			} else {
				return mapping.findForward(UserUtil.FORWARD_LOGIN);
			}
		}
	}
}
