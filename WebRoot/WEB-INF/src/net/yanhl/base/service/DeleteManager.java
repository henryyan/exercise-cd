package net.yanhl.base.service;

import java.io.Serializable;

import net.yanhl.base.callback.DeleteCallback;

/**
 * <p><b>Title：</b>删除业务接口定义</p>
 * <p><b>Description：</b>删除POJO接口，主要应用在列表中的删除</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090114
 */
public interface DeleteManager extends BaseManager {

	void deleteSingle(Class<?> pojoClass, Serializable id, DeleteCallback deleteCallback);
	
	void deleteMulti();
	
}
