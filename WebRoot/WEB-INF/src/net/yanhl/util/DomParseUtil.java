package net.yanhl.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * <p><b>Title：</b> 解析XML文件</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public class DomParseUtil {

	/**
	 * 从文件读取XML，输入文件名，返回XML文档
	 * @param fileName  XML文件名称 
	 * @return	解析后的org.dom4j.Document对象
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * @see org.dom4j.Document
	 */
	public static Document read(String fileName) throws MalformedURLException, DocumentException {
		URL systemResource = null;
		try {
			DomParseUtil.class.getClassLoader();
			systemResource = ClassLoader.getSystemResources(fileName).nextElement();
		} catch (IOException e) {
			e.printStackTrace();
		}//ClassLoader.getSystemResource(fileName);
		String fullFileName = systemResource.getFile();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fullFileName));
		return document;
	}

	/**
	 * 通过知道路径和文件名获得文档对象
	 * @param filePath  自定义的文件路径
	 * @param fileName  XML文件名
	 * @return	解析后的org.dom4j.Document对象
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * @see org.dom4j.Document
	 */
	public static Document read(String filePath, String fileName) throws MalformedURLException,
			DocumentException {
		String fullFileName = "";
		if (filePath == null) {
			return null;
		}
		if (filePath.endsWith("/")) {
			fullFileName = filePath + fileName;
		} else {
			fullFileName = filePath + "/" + fileName;
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fullFileName));
		return document;
	}

	/**
	 * 获得根节点
	 * @param doc
	 * @return	org.dom4j.Element
	 */
	public static Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	/**
	 * 通过文件名载入并返回根节点
	 * @param fileName
	 * @return	org.dom4j.Element
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public static Element readAndGetRootElement(String fileName) throws MalformedURLException,
			DocumentException {
		return read(fileName).getRootElement();
	}

	/**
	 * 通过文件名载入并返回根节点
	 * @param fileName
	 * @param filePath 自定义的文件路径
	 * @return	org.dom4j.Element
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public static Element readAndGetRootElement(String filePath, String fileName)
			throws MalformedURLException, DocumentException {
		return read(fileName).getRootElement();
	}
	
	public static void main(String[] args) {
	}
	
}
