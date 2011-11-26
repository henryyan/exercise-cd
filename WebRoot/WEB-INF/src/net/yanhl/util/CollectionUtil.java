package net.yanhl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合、数组工具
 * @author HenryYan
 *
 */
public class CollectionUtil {

	/**
	 * 数组转换成List
	 * @param <T>	数组对象的类型
	 * @param array	数组对象
	 * @return	包含了数组对象内容的ArrayList
	 */
	public static <T> List<T> arrayToList(T... array) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
}
