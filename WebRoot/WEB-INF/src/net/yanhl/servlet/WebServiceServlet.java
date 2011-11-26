package net.yanhl.servlet;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.yanhl.util.ReadProperties;
import net.yanhl.util.webservice.PublishWebService;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 发布WebService
 *
 * @author HenryYan
 *
 */
public class WebServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(WebServiceServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebServiceServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		log.info("启动WebService发布系统");
		WebApplicationContext wctx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

		String hostAddress = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostAddress = addr.getHostAddress();
		} catch (UnknownHostException e) {
			log.error("发布WebService获取WebService地址出错：" + e.getMessage(), e);
		}

		log.info("发布WebService，本机IP：" + hostAddress);
		String serverPort = "";
		try {
			serverPort = ReadProperties.getSystemValue("webservice.port");
		} catch (ConfigurationException e) {
			log.error("发布WebService获取WebService端口出错：" + e.getMessage(), e);
		}
		log.info("发布WebService，服务端口：" + serverPort);
		
		PublishWebService.publish(wctx, hostAddress, serverPort);

		log.info("WebService发布结束");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
	}

}
