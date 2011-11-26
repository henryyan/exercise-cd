package net.yanhl.base.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.query.util.QueryUtil;
import net.yanhl.util.CollectionUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * <p><b>Title：</b> 基础DAO实现类</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20080703
*/
@Repository(value = "baseDao")
public class BaseDaoHibernate extends HibernateDaoSupport implements BaseDao {

	Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "sessionFactory")
	public void setSessionFactory0(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void delete(Object pojo) throws RuntimeException {
		getHibernateTemplate().delete(pojo);
	}

	public void delete(Class<?> pojoClass, Serializable id) throws RuntimeException {
		Object loadById = loadById(pojoClass, id);
		getHibernateTemplate().delete(loadById);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List find(String hql) throws RuntimeException {
		return getHibernateTemplate().find(hql);
	}

	public void save(Object pojo) throws RuntimeException {
		getHibernateTemplate().save(pojo);
	}

	public <T> Object loadById(Class<T> pojoClass, Serializable id) throws RuntimeException {
		return getHibernateTemplate().get(pojoClass, id);
	}

	public void update(Object pojo) throws RuntimeException {
		getHibernateTemplate().update(pojo);
	}

	public void insertOrUpdate(Object pojo) throws RuntimeException {
		getHibernateTemplate().saveOrUpdate(pojo);
	}

	public Integer deleteAll(final Class<?> pojoName, final java.lang.Long[] ids) throws RuntimeException {
		if (ids.length == 0) {
			return 0;
		}
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "delete from " + pojoName.getName() + " where id in(:ids)";
				Query deleteQuery = session.createQuery(hql);
				deleteQuery.setParameterList("ids", ids);
				int dels = deleteQuery.executeUpdate();
				return dels;
			}

		});
		return (Integer) execute;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List find(final ListQuery listQuery) throws RuntimeException {
		final QueryUtil queryUtil = new QueryUtil();
		queryUtil.setListQuery(listQuery);
		final List result = new ArrayList();
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					queryUtil.setCommonQuery(session);
					queryUtil.setListCountQuery(session);
					result.add(queryUtil.getListCount());
					queryUtil.setResultListQuery(session);
					result.add(queryUtil.getResultColl());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return result;
			}
		});
	}

	public Integer deleteAll(final Class<?> pojoName, final String property, final Object value)
			throws RuntimeException {
		if (StringUtils.isEmpty(property) || (value == null || StringUtils.isEmpty(value.toString()))) {
			return 0;
		}
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query deleteQuery = session.createQuery("delete from " + pojoName.getSimpleName() + " where "
						+ property + "=?");
				int dels = deleteQuery.setParameter(0, value).executeUpdate();
				return dels;
			}

		});
		return (Integer) execute;
	}

	public Integer deleteAll(final Class<?> pojoName, final String property, final Object[] values)
			throws RuntimeException {
		if (values.length == 0) {
			return 0;
		}
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "delete from " + pojoName.getName() + " where " + property + " in(:vals)";
				Query deleteQuery = session.createQuery(hql);
				deleteQuery.setParameterList("vals", values);
				int dels = deleteQuery.executeUpdate();
				return dels;
			}

		});

		return (Integer) execute;
	}

	public int update(final Class<?> cls, final Map<String, Object> setKVMap, final Map<String, Object[]> filterKVMap)
			throws RuntimeException {

		Object updateCount = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("update " + cls.getSimpleName() + " o set");

				Set<String> setSets = setKVMap.keySet();
				Set<String> filterSets = filterKVMap.keySet();

				// 设置参数列表
				for (String setKey : setSets) {
					if (hql.toString().endsWith("set")) {
						hql.append(" o." + setKey + "=:" + setKey);
					} else if (hql.toString().endsWith(",")) {
						hql.append(", o." + setKey + "=:" + setKey);
					}
				}

				// 恒等式
				hql.append(" where 1=1");

				Map<String, Object> filterValues = new HashMap<String, Object>();
				for (String filterKey : filterSets) {
					Object[] filter = filterKVMap.get(filterKey);
					String operation = BaseQuery.EQ;
					Object filterValue = null;
					if (filter.length == 1) {
						filterValue = filter[0];
					} else if (filter.length == 2) {
						operation = filter[0].toString();
						filterValue = filter[1];
					}

					filterValues.put(filterKey, filterValue);

					// 多个值
					if (filterValue instanceof Object[]) {
						hql.append(" and o." + filterKey + " in(:" + filterKey + ")");
					} else {
						// 单个值
						hql.append(" and o." + filterKey + operation + ":" + filterKey);
					}
				}

				Query updateQuery = session.createQuery(hql.toString());
				updateQuery.setProperties(setKVMap);
				updateQuery.setProperties(filterValues);

				return updateQuery.executeUpdate();
			}

		});
		Integer intUpdateCount = (Integer) updateCount;
		return intUpdateCount.intValue();
	}

	public Integer deleteAll(final Class<?> pojoName, final Map<String, Object> params) throws RuntimeException {
		Object execute = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Set<Entry<String, Object>> entrySet = params.entrySet();
				StringBuffer sb = new StringBuffer();
				for (Entry<String, Object> entry : entrySet) {
					String qkey = entry.getKey().replaceAll("\\.", "");
					if (entry.getValue().getClass() == Object[].class) {
						sb.append(" and " + entry.getKey() + " in(:" + qkey + ")");
					} else {
						sb.append(" and " + entry.getKey() + "=:" + qkey);
					}
				}

				String hql = "delete from " + pojoName.getName() + " where 1=1 " + sb.toString();
				Query deleteQuery = session.createQuery(hql);
				for (Entry<String, Object> entry : entrySet) {
					String qkey = entry.getKey().replaceAll("\\.", "");
					if (entry.getValue().getClass() == Object[].class) {
						List<Object> arrayToList = CollectionUtil.arrayToList((Object[]) entry.getValue());
						deleteQuery.setParameterList(qkey, arrayToList);
					} else {
						deleteQuery.setParameter(qkey, entry.getValue());
					}
				}
				int dels = deleteQuery.executeUpdate();
				return dels;
			}

		});

		return (Integer) execute;
	}

}