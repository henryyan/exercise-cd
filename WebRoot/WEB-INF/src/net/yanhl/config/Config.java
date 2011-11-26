package net.yanhl.config;

import net.yanhl.config.exception.ConfigException;
import net.yanhl.config.exception.ConfigReadXmlException;

/**
 * <p><b>Title：</b> 系统配置基类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public abstract class Config {
	
	/**
	 * 读取配置文件并设置到配置对象中
	 * @param pojoName	POJO名称，在<b><i>conf/load_pojo_config.xml</i></b>中
	 * @throws ConfigReadXmlException	解析XML时
	 * @throws ConfigException	读取数据时
	 */
	protected abstract void readConfig(String pojoName) throws ConfigReadXmlException, ConfigException;
	
}
