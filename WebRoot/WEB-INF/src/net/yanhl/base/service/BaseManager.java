package net.yanhl.base.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <p><b>Title：</b> 基础管理类</p>
 * <p><b>Description：</b> 对数据库进行CRUD操作</p>
 * @author	闫洪磊
 * @version	1.0.0.20080601
*/
public interface BaseManager {

	/**
	 * 以对象类型和对象编号为标示,获取相对应的对象
	 *
	 * @param pojoClass
	 *            对象类型
	 * @param id
	 *            对象编号
	 * @return 获取的简单对象
	 * @exception RuntimeException
	 */
	public <T> Object get(Class<T> pojoClass, Serializable id) throws RuntimeException;

	/**
	 * 创建新对象
	 *
	 * @param pojo
	 *            Object 新对象
	 * @throws RuntimeException
	 */
	public void save(Object pojo) throws RuntimeException;

	/**
	 * 保存一个集合
	 * @param pojoColls
	 * @throws RuntimeException
	 */
	public void saveCollection(Collection<?> pojoColls) throws RuntimeException;

	/**
	 * 更新已有对象
	 *
	 * @param pojo
	 *            Object 需要更新的对象
	 * @throws RuntimeException
	 */
	public void update(Object pojo) throws RuntimeException;

	/**
	 * 以对象类型和对象编号为标示,删除相对应的对象
	 *
	 * @param pojoClass
	 *            对象类型
	 * @param id
	 *            对象编号
	 * @exception RuntimeException
	 */
	public <T> void delete(Class<T> pojo, Serializable id) throws RuntimeException;

	/**
	 * 删除集合中的全部对象
	 * @param pojo pojo
	 * @param ids 要删除的ID集合
	 * @throws RuntimeException
	 */
	public <T> void deleteAll(Class<T> pojo, String[] ids) throws RuntimeException;

	/**
	 * 根据对象属性删除
	 * @param pojoName 	pojo映射名
	 * @param property 	属性名称
	 * @param values	属性值数组
	 * @throws RuntimeException
	 */
	public Integer deleteAll(Class<?> pojoName, String property, Object[] values) throws RuntimeException;

	/**
	 * 插入或者更新对象
	 *
	 * @param pojo
	 *            Object 目标pojo对象
	 * @throws RuntimeException
	 */
	public void insertOrUpdate(Object pojo) throws RuntimeException;

	/**
	 * 查找所有符合条件的记录
	 * @param listQuery 查询条件
	 * @return 查询结果
	 * @throws RuntimeException
	 */
	public <E> List<E> find(ListQuery listQuery) throws RuntimeException;


	/**
	 * 查找符合条件的记录
	 * @param <T>
	 * @param cls pojo类
	 * @param key 属性
	 * @param value 值
	 * @return
	 */
	public <T> List<T> findBy(Class<T> cls, String key, Object value) throws RuntimeException;

	/**
	 * 查找符合条件的记录
	 * @param <T>
	 * @param cls pojo类
	 * @param property 获取字段
	 * @param key	属性
	 * @param value 值
	 * @return
	 * @throws RuntimeException
	 */
	public <T> Object findBy(Class<T> cls, String property, String key, Object value) throws RuntimeException;

	/**
	 * 查找符合条件的记录
	 * @param <T>
	 * @param cls pojo类
	 * @param keys	属性
	 * @param values 值
	 * @return
	 * @throws RuntimeException
	 */
	public <T> List<T> findBy(Class<T> cls, String[] keys, Object[] values) throws RuntimeException;

	/**
	 * 根据条件更新指定POJO
	 * @param <T>
	 * @param cls			要更新的POJO
	 * @param setKVMap		要设置的字段名称和字段内容
	 * @param filterKVMap	条件字段内容：由<字段名称， new Object[]{比较符号({@link BaseQuery}), 过滤内容}><br/>
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

	/**
	 * 获取当前的Hibernate Session
	 * @return	当前的Hibernate Session
	 * @see org.hibernate.Session
	 */
	public Session getCurrentSession();

	/**
	 * 获取当前的Hibernate Transaction
	 * @return	当前的Hibernate Transaction
	 * @see org.hibernate.Transaction
	 */
	public Transaction getCurrentTransaction();

}
