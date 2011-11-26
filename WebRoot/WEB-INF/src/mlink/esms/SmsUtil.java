package mlink.esms;

import java.util.NoSuchElementException;

import net.yanhl.util.StringUtil;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><b>Title：</b> 短信发送工具类</p>
 * <p><b>Description：</b>提供发送短信功能</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090805
 */
public class SmsUtil {

	static Log log = LogFactory.getLog(SmsUtil.class);
	
	/**
	 * 发送短信结果标志
	 */
	public static final String SEND_SMS_ERROR_LABLE = "sendSms";
	
	/**
	 * 短信发送失败信息
	 */
	public static final String SEND_SMS_ERROR_INFO = "smsError";

	/**
	 * @param mobilePhone	手机号码
	 * @param smsContent	短信内容
	 * @throws ConfigurationException 读取短信配置文件时
	 * @throws SmsException 发送短信失败时
	 */
	public static boolean sendSingleSms(String mobilePhone, String smsContent) throws SmsException {
		String phoneReg = "^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$";
		if (StringUtil.getValue(mobilePhone).matches(phoneReg)) {
			try {
				int maxLength = 60;

				// 处理超过一条长度的情况
				if (smsContent.length() > maxLength) {

					// 去掉页数信息
					maxLength -= 5;

					double smsNumber = smsContent.length() / (maxLength * 1.0);
					int intSmsNumber = (int) smsNumber;

					if (smsNumber > intSmsNumber) {
						intSmsNumber++;
					}

					log.debug("本次要发送" + intSmsNumber + "条短信");
					for (int i = 0; i < intSmsNumber; i++) {
						String singleMms = "(" + (i + 1) + "/" + intSmsNumber + ")";

						if ((i + 1) * maxLength < smsContent.length()) {
							int end = maxLength * (i + 1);
							singleMms += smsContent.substring(i * maxLength, end);
						} else {
							singleMms += smsContent.substring(i * maxLength);
						}
						System.out.println(singleMms + "\t" + singleMms.length());
						SendMessage.sendSingle(singleMms, "86" + mobilePhone);

					}
				} else {
					log.debug("没有超出一条短信长度");
					SendMessage.sendSingle(smsContent, "86" + mobilePhone);
				}
				log.debug("短信发送成功: 手机：" + mobilePhone + "，内容：\n\t" + smsContent);
				return true;
			} catch (SmsException e) {
				log.error("短信发送失败，手机：" + mobilePhone + "，内容：\n\t" + smsContent);
				e.printStackTrace();
				throw e;
			}
		} else {
			log.error("手机号码：[" + mobilePhone + "]，格式不正确，取消发送短信，要发送的内容为：\n\t" + smsContent);
			throw new SmsException("手机号码格式不正确");
		}
	}

	/**
	 * 从配置文件sms.properties中读取短信开关
	 * @return	启用true,未启用false
	 * @deprecated	使用场馆信息中的send_sms代替
	 */
	public static boolean smsOnOff() {
		PropertiesConfiguration config = null;
		boolean onOrOff = false;
		try {
			config = new PropertiesConfiguration("conf/sms.properties");
			onOrOff = config.getBoolean("sms.on-off");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return onOrOff;
	}

}
