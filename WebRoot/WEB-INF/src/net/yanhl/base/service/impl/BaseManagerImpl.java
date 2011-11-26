package net.yanhl.base.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.BaseManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 基础业务接口实现类</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
*/
@Service(value="baseManager")
@Transactional(rollbackFor=Exception.class)
public class BaseManagerImpl implements BaseManager {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected BaseDao baseDao;

	public <T> void delete(Class<T> pojoClass, Serializable id) throws RuntimeException {
		baseDao.delete(pojoClass, id);
	}

	@Transactional(readOnly = true)
	public <T> Object get(Class<T> pojoClass, Serializable id) throws RuntimeException {
		return baseDao.loadById(pojoClass, id);
	}

	public void insertOrUpdate(Object pojo) throws RuntimeException {
		baseDao.insertOrUpdate(pojo);
	}

	public void save(Object pojo) throws RuntimeException {
		baseDao.save(pojo);
	}

	public void update(Object pojo) throws RuntimeException {
		baseDao.update(pojo);
	}

	public <T> void deleteAll(Class<T> pojoName, String[] ids) throws RuntimeException {
		Long[] longIds = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			longIds[i] = Long.parseLong(ids[i]);
		}
		Integer deleteAll = baseDao.deleteAll(pojoName, longIds);
		log.debug("成功删除" + deleteAll + "个[" + pojoName.getSimpleName() + "]对象");
	}

	public Integer deleteAll(Class<?> pojoName, String property, Object[] values)
			throws RuntimeException {
		return baseDao.deleteAll(pojoName, property, values);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <E> List<E> find(ListQuery listQuery) throws RuntimeException {
		return baseDao.find(listQuery);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findBy(Class<T> cls, String key, Object value) {
		return (List<T>) baseDao.find("from " + cls.getSimpleName() + " t where t." + key + "='" + value + "'");
	}

	@Transactional(readOnly = true)
	public <T> Object findBy(Class<T> cls, String property, String key, Object value)
			throws RuntimeException {
		List<Object> find = baseDao.find("select t." + property + " from " + cls.getSimpleName() + " t where t." + key + "='" + value + "'");
		if(find.size() > 0) {
			return find.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findBy(Class<T> cls, String[] keys, Object[] values) throws RuntimeException {
		String hql = "from " + cls.getSimpleName() + " t where 1 = 1";
		for (int i = 0; i < keys.length; i++) {
			hql += " and t." + keys[i] + " = ?";
		}
		List<T> result = baseDao.getHibernateTemplate().find(hql, values);
		return result;
	}

	public int update(Class<?> cls, Map<String, Object> setKVMap, Map<String, Object[]> filterKVMap)
			throws RuntimeException {
		return baseDao.update(cls, setKVMap, filterKVMap);
	}

	public void saveCollection(Collection<?> pojoColls) throws RuntimeException {
		for (Iterator<?> iterator = pojoColls.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			save(object);
		}
	}

	public Session getCurrentSession() {
		if (baseDao != null) {
			return baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		}
		return null;
	}

	public Transaction getCurrentTransaction() {
		Session currentSession = getCurrentSession();
		if (currentSession != null) {
			return currentSession.getTransaction();
		}
		return null;
	}

}
