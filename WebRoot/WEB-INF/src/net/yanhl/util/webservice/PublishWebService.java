package net.yanhl.util.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public class PublishWebService {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(PublishWebService.class);
	
	protected static Map<String, String> services = new HashMap<String, String>();
	
	/**
	 * WebService列表
	 */
	static {
		services.put("venue", "venueServer");
	}

	/**
	 * 发布WebService
	 * @param request
	 * @param ctx
	 * @throws ServletException
	 * @throws IOException 
	 */
	public static void publish(ApplicationContext ctx, String hostIp, String port) throws ServletException {
		Set<Entry<String, String>> entrySet = services.entrySet();
		log.info("WebService列表：" + entrySet);
		for (Entry<String, String> entry : entrySet) {
			Object bean = ctx.getBean(entry.getValue());
			publishSingle(hostIp, port, entry.getKey(), bean);
		}
	}

	/**
	 * 发布单个WebService工具
	 * @param hostIp
	 * @param port
	 */
	private static String publishSingle(String hostIp, String port, String serviceName, Object serverObj) {
		String host = "http://" + hostIp + ":" + port;
		String address = host + "/axis.exercise/" + serviceName;
		log.debug("开始发布单个WebService：" + address);
		Endpoint.publish(address, serverObj);
		log.debug("发布单个WebService成功：" + address + "，bean=" + serverObj);
		return address;
	}

}
