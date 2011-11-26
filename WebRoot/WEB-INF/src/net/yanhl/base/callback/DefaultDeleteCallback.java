package net.yanhl.base.callback;

import java.io.Serializable;

import net.yanhl.base.service.DeleteManager;

/**
 * <p><b>Title：</b>基础删除回调处理类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090113
 */
public class DefaultDeleteCallback implements DeleteCallback {

	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteMulti(java.lang.Class, java.lang.String[])
	 */
	public void deleteMulti(DeleteManager deleteManager, Class<?> pojoClass, String[] splitPids) {
	}

	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteSingle(java.lang.Class, java.io.Serializable)
	 */
	public void deleteSingle(DeleteManager deleteManager, Class<?> pojoClass, Serializable id) {
	}

}
