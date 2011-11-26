package net.yanhl.config;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import net.yanhl.config.exception.ConfigException;
import net.yanhl.config.exception.ConfigReadXmlException;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * <p><b>Title：</b> 加载POJO对象时的配置</p>
 * <p><b>Description：</b>文件名称<b>conf/load_pojo_config.xml</b>，位于classes目录下面</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public class LoadPojoConfig extends AbstractFileConfiguration {

	private String name;
	private boolean ignoreCollection = false;
	private boolean convertDate = true;
	private String[] ignoreAttributes = null;
	
	private static String configFileName = "conf/load_pojo_config.xml";

	private static LoadPojoConfig instance = null;
	
	private LoadPojoConfig(String pojoName) throws ConfigException, ConfigReadXmlException, ConfigurationException {
		super(configFileName);
		readConfig(pojoName);
	}

	/**
	 * 唯一获得实例的方法
	 * @param pojoName	POJO简写名称(在pojo.properties文件中配置)
	 * @return	配置对象实例，在<i><b>conf/load_pojo_config.xml</b></i>文件中存在<b>pojoName</b>
	 * @throws ConfigException 读取数据时
	 * @throws ConfigReadXmlException 解析XML时
	 * @throws ConfigurationException 
	 */
	public static synchronized LoadPojoConfig getInstance(String pojoName) throws ConfigException, ConfigReadXmlException, ConfigurationException {
		if (instance == null) {
			instance = new LoadPojoConfig(pojoName);
		}
		return instance;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 是否忽略集合，默认为false
	 */
	public boolean isIgnoreCollection() {
		return ignoreCollection;
	}

	/**
	 * 是否忽略集合，默认为false
	 */
	public void setIgnoreCollection(boolean ignoreCollection) {
		this.ignoreCollection = ignoreCollection;
	}

	/**
	 * 是否转换日期，默认为true
	 */
	public boolean isConvertDate() {
		return convertDate;
	}

	/**
	 * 是否转换日期，默认为true
	 */
	public void setConvertDate(boolean convertDate) {
		this.convertDate = convertDate;
	}

	public String[] getIgnoreAttributes() {
		return ignoreAttributes;
	}

	public void setIgnoreAttributes(String[] ignoreAttributes) {
		this.ignoreAttributes = ignoreAttributes;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	@SuppressWarnings("static-access")
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	@SuppressWarnings("unchecked")
	protected void readConfig(String pojoName) throws ConfigReadXmlException, ConfigException {

		Document document = null;
		try {
			//document = DomParseUtil.read(configFileName);
			SAXReader reader = new SAXReader();
			document = reader.read(this.getFile());
		} catch (DocumentException e) {
			throw new ConfigReadXmlException(e);
		}
		
		try {
			List<Element> selectNodes = document.selectNodes("//pojo[@name='" + pojoName + "']");
			Element element = null;
			if (selectNodes.size() > 0) {
				element = selectNodes.get(0);
			}
			
			this.setName(pojoName);
			
			// 是否忽略集合
			Node selectSingleNode = element.selectSingleNode("ignoreCollection");
			this.setIgnoreCollection(Boolean.parseBoolean(StringUtils.deleteWhitespace(selectSingleNode.getText())));
			
			// 是否转换日期
			selectSingleNode = element.selectSingleNode("convertDate");
			this.setConvertDate(Boolean.parseBoolean(StringUtils.deleteWhitespace(selectSingleNode.getText())));
			
			// 忽略属性集合
			List<Element> selectNodes2 = element.selectNodes("ignoreAttributes");
			for (Element element2 : selectNodes2) {
				List<Element> selectNodes3 = element2.selectNodes("attribute");
				ignoreAttributes = new String[selectNodes3.size()];
				for (int i = 0; i < selectNodes3.size(); i++) {
					ignoreAttributes[i] = selectNodes3.get(i).getTextTrim();
				}
			}
		} catch (Exception e) {
			new ConfigException(e);
		}
			
	}

	@Override
	public String toString() {
		return super.toString() + "[convertDate=" + convertDate + "]";
	}

	public void load(Reader in) throws ConfigurationException {
	}

	public void save(Writer out) throws ConfigurationException {
	}
	
	public static void main(String[] args) {
		try {
			LoadPojoConfig instance2 = LoadPojoConfig.getInstance("target.master");
			System.out.println(instance2);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (ConfigException e) {
			e.printStackTrace();
		} catch (ConfigReadXmlException e) {
			e.printStackTrace();
		}
	}

}
