package net.yanhl.util;

import org.apache.commons.lang.StringUtils;

/**
 * 文件工具类
 *
 * @author HenryYan
 *
 */
public class FileUtil {

	/**
	 * 获取文件扩展名
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return StringUtils.EMPTY;
		}
		String fileType = StringUtils.substringAfterLast(fileName, ".");
		return fileType;
	}
	
	/**
	 * 获取文件名，没有扩展名
	 * @return
	 */
	public static String getFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return StringUtils.EMPTY;
		}
		String fileType = StringUtils.substringBeforeLast(fileName, ".");
		return fileType;
	}
	
	public static void main(String[] args) {
		System.out.println(getFileType("sldfjdocs"));
	}
	
}
