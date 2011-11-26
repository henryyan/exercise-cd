package net.yanhl.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring工具类
 *
 * @author HenryYan
 *
 */
public class SpringUtil {

	private static ApplicationContext ctx = null;
	
	/**
	 * 获取{@link ApplicationContext}
	 * @param request
	 * @return
	 */
	public static ApplicationContext getCtx(HttpServletRequest request) throws RuntimeException {
		if (ctx == null) {
			ctx = WebApplicationContextUtils.getWebApplicationContext(request
				.getSession().getServletContext());
		}
		return ctx;
	}
	
}
