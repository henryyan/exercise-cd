package net.yanhl.base.callback;

import java.io.Serializable;

import net.yanhl.base.exception.DeleteCallbackException;
import net.yanhl.base.service.DeleteManager;

/**
 * <p><b>Title：</b>删除回调接口</p>
 * <p><b>Description：</b>通过{@link net.yanhl.base.action.DeleteAction}
 * 删除记录后回调的接口，删除单个或者多个记录是都调用</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
public interface DeleteCallback {

	/**
	 * 删除单个记录回调接口
	 * @param baseManager	基础业务接口
	 * @param pojoClass		POJO对象的Class
	 * @param id			主键ID
	 * @throws DeleteCallbackException	执行回调实体类出错时
	 */
	void deleteSingle(DeleteManager deleteManager, Class<?> pojoClass, Serializable id) throws DeleteCallbackException;
	
	/**
	 * 删除多个记录回调接口
	 * @param baseManager	基础业务接口
	 * @param pojoClass		POJO对象的Class
	 * @param splitPids		主键ID，用逗号分隔
	 * @throws DeleteCallbackException	执行回调实体类出错时
	 */
	void deleteMulti(DeleteManager deleteManager, Class<?> pojoClass, String[] splitPids) throws DeleteCallbackException;
	
}
