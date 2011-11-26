package net.yanhl.tactics.callback;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.yanhl.base.callback.BaseDeleteCallback;
import net.yanhl.base.service.DeleteManager;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsPrice;

/**
 * <p><b>Title：</b>策略模块删除记录后的回调处理类</p>
 * <p><b>Description：</b>删除记录后执行的动作</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090114
 */
public class TacticsDeleteCallBack extends BaseDeleteCallback {
	
	Log log = LogFactory.getLog(this.getClass());

	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteMulti(net.yanhl.base.service.BaseManager, java.lang.Class, java.lang.String[])
	 */
	public void deleteMulti(DeleteManager deleteManager, Class<?> pojoClass, String[] splitPids) {
	}
	
	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.DeleteCallback#deleteSingle(net.yanhl.base.service.BaseManager, java.lang.Class, java.io.Serializable)
	 */
	/* (non-Javadoc)
	 * @see net.yanhl.base.callback.BaseDeleteCallback#deleteSingle(net.yanhl.base.service.DeleteManager, java.lang.Class, java.io.Serializable)
	 */
	public void deleteSingle(DeleteManager deleteManager, Class<?> pojoClass, Serializable id) {
		
		/**
		 * 删除策略价格时更新策略的状态为【已修改】
		 */
		if (pojoClass == TacticsPrice.class) {
			
			Object findBy = deleteManager.findBy(TacticsPrice.class, "tactics", "id", id);
			if (findBy != null) {
				Tactics tactics = (Tactics) findBy;
				if (!tactics.getIsModify()) {
					log.debug("更新策略[" + tactics + "]的修改状态为true");
					tactics.setIsModify(true);
					deleteManager.update(tactics);
				}
			}
			
		}
		
	}

}
