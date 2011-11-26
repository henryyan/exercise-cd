package net.yanhl.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ReadProperties {
	
	/**
	 * 读取系统配置文件中的值
	 * @param key	键
	 * @return	在系统配置文件中key对应的value
	 * @throws ConfigurationException	不能创建apache的PropertiesConfiguration对象时
	 */
	public static String getSystemValue(String key) throws ConfigurationException {
		PropertiesConfiguration config = new PropertiesConfiguration("conf/system.properties");
		String fullPojoName = config.getString(key);
		return fullPojoName;
	}
	
}