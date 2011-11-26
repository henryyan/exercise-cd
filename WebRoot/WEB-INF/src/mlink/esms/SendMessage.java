package mlink.esms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendMessage {
	
	private static Log log = LogFactory.getLog(SendMessage.class);

	private static String mtUrl = "http://esms.etonenet.com/sms/mt";
	
	//SP编号、SP密码，必填参数
	
	static String spid = "7891";
	static String sppassword = "123123";

	// sp服务代码，可选参数，默认为 00
	static String spsc = "00";

	// 下行内容以及编码格式，必填参数
	static int dc = 15;
	static String ecodeform = "GBK";

	/**
	 * 单条下行实例
	 * @param msg	短信内容
	 * @param da	手机号码 ，必填参数
	 * @throws Exception
	 */
	public static boolean sendSingle(String msg, String da) throws SmsException {
		
		 // 操作命令
		String command = "MT_REQUEST";
		
		// 源号码，可选参数
		String sa = "10657109053657";
		
		String sm = "";
		try {
			sm = new String(Hex.encodeHex(msg.getBytes(ecodeform)));
		} catch (UnsupportedEncodingException e) {
			new SmsException("内部错误");
			return false;
		}

		// 组成url字符串
		StringBuilder smsUrl = new StringBuilder();
		smsUrl.append(mtUrl);
		smsUrl.append("?command=" + command);
		smsUrl.append("&spid=" + spid);
		smsUrl.append("&sppassword=" + sppassword);
		smsUrl.append("&spsc=" + spsc);
		smsUrl.append("&sa=" + sa);
		smsUrl.append("&da=" + da);
		smsUrl.append("&sm=" + sm);
		smsUrl.append("&dc=" + dc);
		
		log.debug("发送短信URL：" + smsUrl);

		// 发送http请求，并接收http响应
		String resStr = doGetRequest(smsUrl.toString());
		log.debug("接收响应URL：" + resStr);

		// 解析响应字符串
//		HashMap<String, String> pp = parseResStr(resStr);
//		System.out.println(pp.get("command"));
//		System.out.println(pp.get("spid"));
//		System.out.println(pp.get("mtmsgid"));
//		System.out.println(pp.get("mtstat"));
//		System.out.println(pp.get("mterrcode"));
		return true;
	}

	/**
	 * 将普通字符串转换成Hex编码字符串
	 * 
	 * @param dataCoding 编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
	 * @param realStr 普通字符串
	 * @return Hex编码字符串
	 * @throws UnsupportedEncodingException 
	 */
	public static String encodeHexStr(int dataCoding, String realStr) {
		String hexStr = null;
		if (realStr != null) {
			try {
				if (dataCoding == 15) {
					hexStr = new String(Hex.encodeHex(realStr.getBytes("GBK")));
				} else if ((dataCoding & 0x0C) == 0x08) {
					hexStr = new String(Hex.encodeHex(realStr.getBytes("UnicodeBigUnmarked")));
				} else {
					hexStr = new String(Hex.encodeHex(realStr.getBytes("ISO8859-1")));
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.toString());
			}
		}
		return hexStr;
	}

	/**
	 * 将Hex编码字符串转换成普通字符串
	 * 
	 * @param dataCoding 反编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
	 * @param hexStr Hex编码字符串
	 * @return 普通字符串
	 */
	public static String decodeHexStr(int dataCoding, String hexStr) {
		String realStr = null;
		try {
			if (hexStr != null) {
				if (dataCoding == 15) {
					realStr = new String(Hex.decodeHex(hexStr.toCharArray()), "GBK");
				} else if ((dataCoding & 0x0C) == 0x08) {
					realStr = new String(Hex.decodeHex(hexStr.toCharArray()), "UnicodeBigUnmarked");
				} else {
					realStr = new String(Hex.decodeHex(hexStr.toCharArray()), "ISO8859-1");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return realStr;
	}

	/**
	 * 发送http GET请求，并返回http响应字符串
	 * 
	 * @param urlstr 完整的请求url字符串
	 * @return
	 */
	public static String doGetRequest(String urlstr) {
		String res = null;
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getParams().setIntParameter("http.socket.timeout", 10000);
		client.getParams().setIntParameter("http.connection.timeout", 5000);

		HttpMethod httpmethod = new GetMethod(urlstr);
		try {
			int statusCode = client.executeMethod(httpmethod);
			if (statusCode == HttpStatus.SC_OK) {
				res = httpmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpmethod.releaseConnection();
		}
		return res;
	}

	/**
	 * 发送http POST请求，并返回http响应字符串
	 * 
	 * @param urlstr 完整的请求url字符串
	 * @return
	 */
	public static String doPostRequest(String urlstr) {
		String res = null;
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getParams().setIntParameter("http.socket.timeout", 10000);
		client.getParams().setIntParameter("http.connection.timeout", 5000);

		HttpMethod httpmethod = new PostMethod(urlstr);
		try {
			int statusCode = client.executeMethod(httpmethod);
			if (statusCode == HttpStatus.SC_OK) {
				res = httpmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpmethod.releaseConnection();
		}
		return res;
	}

	/**
	 * 将 短信下行 请求响应字符串解析到一个HashMap中
	 * @param resStr
	 * @return
	 */
	public static HashMap<String, String> parseResStr(String resStr) {
		HashMap<String, String> pp = new HashMap<String, String>();
		try {
			String[] ps = resStr.split("&");
			for (int i = 0; i < ps.length; i++) {
				int ix = ps[i].indexOf("=");
				if (ix != -1) {
					pp.put(ps[i].substring(0, ix), ps[i].substring(ix + 1));
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return pp;
	}

}
