package net.sf.json.processors;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;
import net.sf.json.util.PropertyFilter;
import net.yanhl.util.StringUtil;

/**
 * <p><b>Title：</b> 忽略属性</p>
 * <p><b>Description：</b>忽略JAVABEAN的指定属性、是否忽略集合类属性</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090630
 */
public class IgnoreFieldProcessorImpl implements PropertyFilter {
	
	Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 忽略的属性名称
	 */
	private String[] fields;
	
	/**
	 * 是否忽略集合
	 */
	private boolean ignoreColl = false;
	
	/**
	 * 空参构造方法<br/>
	 * 默认不忽略集合
	 */
	public IgnoreFieldProcessorImpl() {
		// empty
	}
	
	/**
	 * 构造方法
	 * @param fields 忽略属性名称数组
	 */
	public IgnoreFieldProcessorImpl(String[] fields) {
		this.fields = fields; 
	}
	
	/**
	 * 构造方法
	 * @param ignoreColl	是否忽略集合
	 * @param fields	忽略属性名称数组
	 */
	public IgnoreFieldProcessorImpl(boolean ignoreColl, String[] fields) {
		this.fields = fields;
		this.ignoreColl = ignoreColl; 
	}
	
	/**
	 * 构造方法
	 * @param ignoreColl 是否忽略集合
	 */
	public IgnoreFieldProcessorImpl(boolean ignoreColl) {
		this.ignoreColl = ignoreColl; 
	}

	public boolean apply(Object source, String name, Object value) {
		Field declaredField = null;
		try {
			if (source instanceof JSONObject) {
				return false;
			}
			declaredField = source.getClass().getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			log.warn("没有找到属性" + name);
//			e.printStackTrace();
		}
		
		// 忽略集合
		if (declaredField != null) {
			if(ignoreColl) {
				if(declaredField.getType() == Collection.class
						|| declaredField.getType() == Set.class
						|| declaredField.getType() == Map.class
						|| declaredField.getType() == List.class) {
					return true;
				}
			}
		}
		
		// 忽略设定的属性
		if(fields != null && fields.length > 0) {
			if(StringUtil.hasInArray(fields, name)) {
				return true;
			}
		}
		
		return false;
	}

	public String[] getFields() {
		return fields;
	}

	/**
	 * 设置忽略的属性
	 * @param fields
	 */
	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public boolean isIgnoreColl() {
		return ignoreColl;
	}

	/**
	 * 设置是否忽略集合类
	 * @param ignoreColl
	 */
	public void setIgnoreColl(boolean ignoreColl) {
		this.ignoreColl = ignoreColl;
	}

	
}
