package net.yanhl.base.callback;

import java.io.Serializable;

import net.yanhl.base.exception.DeleteCallbackException;
import net.yanhl.base.service.DeleteManager;

/**
 * <p><b>Title：</b>基础删除回调类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090114
 */
public abstract class BaseDeleteCallback implements DeleteCallback {
	
	public BaseDeleteCallback() {
		super();
	}

	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteMulti(net.yanhl.base.service.BaseManager, java.lang.Class, java.lang.String[])
	 */
	public abstract void deleteMulti(DeleteManager deleteManager, Class<?> pojoClass, String[] splitPids) throws DeleteCallbackException;

	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteSingle(net.yanhl.base.service.BaseManager, java.lang.Class, java.io.Serializable)
	 */
	public abstract void deleteSingle(DeleteManager deleteManager, Class<?> pojoClass, Serializable id) throws DeleteCallbackException;

}
