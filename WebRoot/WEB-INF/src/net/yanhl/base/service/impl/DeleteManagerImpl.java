package net.yanhl.base.service.impl;

import java.io.Serializable;

import net.yanhl.base.callback.DeleteCallback;
import net.yanhl.base.exception.DeleteCallbackException;
import net.yanhl.base.service.DeleteManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b>删除业务实现类</p>
 * <p><b>Description：</b>删除数据库调用回调处理类</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090114
 */
@Service(value="deleteManager")
@Transactional(rollbackFor=Exception.class)
public class DeleteManagerImpl extends BaseManagerImpl implements DeleteManager {
	
	Log log = LogFactory.getLog(this.getClass());

	public void deleteMulti() {
	}

	public void deleteSingle(Class<?> pojoClass, Serializable id, DeleteCallback deleteCallback) {
		
		// 删除回调
		try {
			if (deleteCallback != null) {
				log.debug("class=[" + pojoClass.getName() + "]，回调类=" + deleteCallback.getClass().getName());
				deleteCallback.deleteSingle(this, pojoClass, id);
			}
		} catch (DeleteCallbackException e) {
			e.printStackTrace();
			log.error("删除回调：" + e.getMessage());
		}
		
		super.delete(pojoClass, id);
		
	}

}
