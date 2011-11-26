package net.yanhl.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * <p><b>Title：</b> 基础DAO接口</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20080703
*/
public interface BaseDao {

	public HibernateTemplate hibernateTemplate = null;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate);

	public HibernateTemplate getHibernateTemplate();

	/**
	 * 根据类和id找到pojo对象
	 *
	 * @param pojoClass
	 *            Class pojo的类
	 * @param id
	 *            String 唯一标识
	 * @return Object pojo对象
	 * @throws RuntimeException
	 */
	public <T> Object loadById(Class<T> pojoClass, Serializable id) throws RuntimeException;

	/**
	 * 从数据库查询相应列表
	 *
	 * @param ql
	 *            查询语言
	 * @return Object pojo对象
	 * @throws RuntimeException
	 */
	public List<Object> find(String hql) throws RuntimeException;

	/**
	 * 创建新对象
	 *
	 * @param pojo
	 *            Object 新对象
	 * @throws RuntimeException
	 */
	public void save(Object pojo) throws RuntimeException;

	/**
	 * 更新已有对象
	 *
	 * @param pojo
	 *            Object 需要更新的对象
	 * @throws RuntimeException
	 */
	public void update(Object pojo) throws RuntimeException;

	/**
	 * 插入或更新已有对象
	 *
	 * @param pojo
	 *            Object 需要插入或更新的对象
	 * @throws RuntimeException
	 */
	public void insertOrUpdate(Object pojo) throws RuntimeException;

	/**
	 * 删除对象
	 *
	 * @param pojo
	 *            Object 需要删除的对象
	 * @throws RuntimeException
	 */
	public void delete(Object pojo) throws RuntimeException;

	/**
	 * 删除对象，根据id
	 *
	 * @param pojoClass		需要删除的对象类
	 * @param id			唯一标识
	 * @throws RuntimeException
	 */
	public void delete(Class<?> pojoClass, Serializable id) throws RuntimeException;

	/**
	 * 删除集合中的全部对象
	 * @param pojoName 	pojo映射名
	 * @param ids 		要删除的ID集合
	 * @throws RuntimeException
	 */
	public Integer deleteAll(Class<?> pojoName, java.lang.Long[] ids) throws RuntimeException;

	/**
	 * 根据对象属性删除
	 * @param pojoName 	pojo映射名
	 * @param property 	属性名称
	 * @param value		属性值
	 * @throws RuntimeException
	 */
	public Integer deleteAll(Class<?> pojoName, String property, Object value) throws RuntimeException;

	/**
	 * 根据对象属性删除
	 * @param pojoName 	pojo映射名
	 * @param property 	属性名称
	 * @param values	属性值数组
	 * @throws RuntimeException
	 */
	public Integer deleteAll(Class<?> pojoName, String property, Object[] values) throws RuntimeException;
	
	/**
	 * 根据对象属性删除
	 * @param pojoName 	pojo映射名
	 * @param property 	属性名称
	 * @param values	属性值数组
	 * @throws RuntimeException
	 */
	public Integer deleteAll(Class<?> pojoName, Map<String, Object> params) throws RuntimeException;

	/**
	 * 查找所有符合条件的记录
	 * @param listQuery 查询条件
	 * @return 查询结果
	 * @throws RuntimeException
	 */
	@SuppressWarnings("rawtypes")
	public List find(ListQuery listQuery) throws RuntimeException;

	/**
	 * 根据条件更新指定POJO
	 * @param <T>
	 * @param cls			要更新的POJO
	 * @param setKeys		要设置的字段名称
	 * @param setValues		要设置的字段内容
	 * @param filterKeys	条件字段名称
	 * @param filterValues	条件字段内容：由<字段名称， new Object[]{比较符号({@link BaseQuery}), 过滤内容}><br/>
	 * 						可以是多个参数值(数组形式)，使用<strong><em>in</em></strong>设置参数值，例如：id in (2,3 4)
	 * 例如：
	 * <pre>
	 * 更新<b>Information.class</b>的<b>infoLabel</b>内容为<em>abcdefg</em>，过滤条件为<b>id>3</b>
	 * 		Map<String, Object> setKVMap = new HashMap<String, Object>();
		setKVMap.put("infoLabel", "abcdefg");

		Map<String, Object[]> filterKVMap = new HashMap<String, Object[]>();
		filterKVMap.put("id", new Object[] {BaseQuery.GT, 3l});

		int update = baseDao.update(Information.class, setKVMap, filterKVMap);
		System.out.println(update);
	 * </pre>
	 * @return	更新成功条数
	 * @throws RuntimeException 更新失败时
	 * @see BaseQuery
	 */
	public int update(Class<?> cls, Map<String, Object> setKVMap,
			Map<String, Object[]> filterKVMap) throws RuntimeException;

}
