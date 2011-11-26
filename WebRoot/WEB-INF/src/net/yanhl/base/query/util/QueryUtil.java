package net.yanhl.base.query.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * <p><b>Title：</b> 查询工具类</p>
 * <p><b>Description：</b> 把ListQuery对象解析为HQL并执行查询</p>
 * @author	闫洪磊
 * @version	1.0.0.20080615
*/
public class QueryUtil {

	Log log = LogFactory.getLog(this.getClass());
	private StringBuffer hqlQuery = new StringBuffer();
	private ListQuery listQuery = null;
	private List<String[]> dateKey = new ArrayList<String[]>();//DATE类型的查询字段
	
	private static Map<String, String> jqGridAndLogicMap = new HashMap<String, String>();
	
	static {
		jqGridAndLogicMap.put("bw", BaseQuery.LIKE);
		jqGridAndLogicMap.put("eq", BaseQuery.EQ);
		jqGridAndLogicMap.put("ne", BaseQuery.NE);
		jqGridAndLogicMap.put("lt", BaseQuery.LT);
		jqGridAndLogicMap.put("le", BaseQuery.LE);
		jqGridAndLogicMap.put("gt", BaseQuery.GT);
		jqGridAndLogicMap.put("ge", BaseQuery.GE);
		jqGridAndLogicMap.put("ew", BaseQuery.LIKE);
		jqGridAndLogicMap.put("cn", BaseQuery.LIKE);
	}

	public static Map<String, String> getJqGridAndLogicMap() {
		return jqGridAndLogicMap;
	}

	public ListQuery getListQuery() {
		return listQuery;
	}

	public void setListQuery(ListQuery listQuery) {
		this.listQuery = listQuery;
	}
	
	/**
	 * 解析单个HQL查询语句
	 * 
	 * @param paramName
	 *            请求参数名
	 * @param queryKey
	 *            请求参数名,{请求参数值,逻辑运算符号,比较符号}
	 * @return 解析完成的HQL查询语句
	 */
	private synchronized String paraseFilter(String paramName, String[] queryKey) {
		String value = queryKey[0];// 参数值
		String logic = queryKey[1];// 逻辑运算符
		String compare = queryKey[2];// 比较符号
		String type = queryKey[3];// 参数类型
		String tempFilter = paramName + compare + "'" + value + "'";
		
		//过滤DATE和DATETIME类型不设置value，在业务中手动设置
		if(type.equals(QueryType.DATE) || type.equals(QueryType.DATETIME)) {
			tempFilter = paramName + compare + ":" + paramName;
			dateKey.add(new String[] {paramName, value, type});
		}
		boolean needWhere = needWhere();
		if (logic.equals(BaseQuery.AND) || logic.equals(BaseQuery.OR)) {
			if (compare.equals(BaseQuery.LIKE)) {
				return LogicUtil.getLike(needWhere, paramName, logic, value);
			} else {
				if (needWhere) {
					return " " + tempFilter;
				} else {
					return " " + logic + " " + tempFilter;
				}
			}
		} else if(logic.equals(BaseQuery.NE)){
			if (needWhere) {
				return tempFilter;
			} else {
				return " " + logic + " " + tempFilter;
			}
		} else {
			return "";
		}
	}
	
	/**
	 * 通过请求参数键获得值
	 * 
	 * @param request
	 * @param queryFilter
	 * @return Map<请求参数名,{请求参数值,逻辑运算符号,比较符号,参数类型}>
	 */
	private synchronized Map<String, String[]> getParameterMap(ListQuery listQuery) {
		HttpServletRequest request = listQuery.getRequest();
		if (request == null) {
			return new HashMap<String, String[]>();
		}
		Map<String, String[]> queryFilter = listQuery.getQueryFilter();
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		Set<String> keys = queryFilter.keySet();
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
			String queryKey = iter.next();
			// 获得请求参数
			String paramKey = queryKey;
			if (paramKey.indexOf(".") != -1) {
				//paramKey = paramKey.split("\\.")[1];
				paramKey = paramKey.substring(paramKey.indexOf(".") + 1);
			}
			String paramValue = StringUtil.getParameter(paramKey, request);
			// 获得逻辑和比较运算符
			String[] tmpCompare = queryFilter.get(queryKey);
			// 请求参数名,{请求参数值,逻辑运算符号,比较符号,参数类型}
			//参数类型默认为string类型
			if(tmpCompare.length >= 3) {
				parameters.put(queryKey, new String[] { paramValue, tmpCompare[0], tmpCompare[1], tmpCompare[2]});
			} else {
				parameters.put(queryKey, new String[] { paramValue, tmpCompare[0], tmpCompare[1], QueryType.STRING});
			}
			// 设置请求参数到attribute
			request.setAttribute(paramKey, paramValue);
		}
		return parameters;
	}

	/**
	 * 根据条件集合queryTerm+hql查询列表结果
	 * 
	 * @param queryTerm
	 *            查询条件
	 * @param hqlQuery
	 *            原始hql语句，一般是from XXPojo
	 * @param session
	 *            hibernate.Session
	 * @return 返回查询后的结果集
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void setCommonQuery(Session session) {
		Map<String, String[]> queryTerm = getParameterMap(listQuery);
		Map<String, String> tableMap = listQuery.getTables();
		Set queryKeys = queryTerm.keySet();
		Iterator iterator = queryKeys.iterator();

		// 设置查询需要的表(pojo名字)
		String tables = getTables(queryTerm, tableMap);
		String masterPojoName = listQuery.getMasterPojo().getName();
		hqlQuery.append("from " + masterPojoName + " o" + tables);// 设置查询的基类

		// 增加创建人查询条件
		if (!StringUtil.isEmpty(listQuery.getOwnerLabel())) {
			needWhere();
			hqlQuery.append(" " + listQuery.getOwnerLabel()[0] + "=" + listQuery.getOwnerLabel()[1]);
		}

		// 增加主要查询条件
		iterator = queryKeys.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String[] keyValue = (String[]) queryTerm.get(key);
			if (!StringUtil.isEmpty(keyValue)) {
				// 获得查询字段及值
				String paraseFilter = paraseFilter(key, keyValue);
				hqlQuery.append(paraseFilter);
			}
		}
		
		// 自定义的查询条件
		if(!listQuery.getCustomFilter().isEmpty()) {
			Iterator<Object[]> customIter = listQuery.getCustomFilter().iterator();
			while (customIter.hasNext()) {
				Object[] customFilter = (Object[]) customIter.next();
				String operator = customFilter[1].toString();
				String paramName = customFilter[0].toString();
				String result = paramName + " " + operator;
				String paramValue = customFilter[2].toString();
				if(paramValue instanceof String) {
					if (operator.equals("like")) {
						result += " '%" + paramValue + "%'";
					} else {
						result += " '" + paramValue + "'";
					}
				}
				if(needWhere()) {
					hqlQuery.append(result);
				} else {
					hqlQuery.append(" and (" + result + ")");
				}
			}
		}
		
		// 结果查询
		if (!StringUtils.isEmpty(listQuery.getOrderBy())) {
			hqlQuery.append(" order by " + listQuery.getOrderBy());
		}
		log.debug("组装查询语句：\n\t" + hqlQuery.toString());
	}

	/**
	 * 创建并返回结果集总数的hibernate的查询对象Query
	 * 
	 * @param session
	 * @return hibernate.Query
	 */
	public Query setListCountQuery(Session session) {
		Query hibernateQuery = session.createQuery("select count(*) " + hqlQuery).setMaxResults(1);
		listQuery.setHibernateQuery(hibernateQuery);
		return hibernateQuery;
	}

	/**
	 * 获取查询列表的总条数
	 * 
	 * @param session
	 * @return
	 * @throws ParseException 
	 */
	public Long getListCount() throws ParseException {
		setQueryDate(listQuery);
		return (Long) listQuery.getHibernateQuery().uniqueResult();
	}

	/**
	 * 创建并返回结果集hibernate的查询对象Query
	 * 
	 * @param session
	 * @return hibernate.Query
	 */
	public Query setResultListQuery(Session session) {
		Query hibernateQuery = session.createQuery(hqlQuery.toString());
		listQuery.setHibernateQuery(hibernateQuery);
		return hibernateQuery;
	}

	/**
	 * 获得结果集
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	public Collection getResultColl() throws ParseException {
		// 设置分页信息
		int from = listQuery.getPageIndex() * listQuery.getPageSize();
		int to = (listQuery.getPageIndex() + 1) * listQuery.getPageSize();
		listQuery.getHibernateQuery().setFirstResult(from);
		listQuery.getHibernateQuery().setMaxResults(to);
		//日期查询
		setQueryDate(listQuery);
		// 合并总条数和分页后的记录
		List result = listQuery.getHibernateQuery().list();
		return result;
	}

	/**
	 * 从查询条件中解析表名
	 * 然后从tables集合中获得对应的POJO名称
	 * 
	 * @param listQuery
	 * @param queryTerm
	 * @param tableMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getTables(Map<String, String[]> queryTerm, Map<String, String> tableMap) {
		Set keys = queryTerm.keySet();
		Iterator iterator = keys.iterator();
		String tables = "";
		// 从主查询语句中解析POJO名称
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String[] keyValue = queryTerm.get(key);
			if (!StringUtil.isEmpty(keyValue)) {
				if (key.indexOf(".") != -1) {// 如果是以别名+属性名
					String queryAffix = key.split("\\.")[0];
					// 增加查询的表
					if (!queryAffix.equals("o")) {
						tables += "," + tableMap.get(queryAffix) + " " + queryAffix;
					}
				}
			}
		}
		//从自定义查询条件中分析应该增加的表
		iterator = listQuery.getCustomFilter().iterator();
		while (iterator.hasNext()) {
			Object[] customFilter = (Object[]) iterator.next();
			//逻辑比较符号左边
			if(customFilter[0].toString().indexOf(".") != -1) {// 如果是以别名+属性名
				String queryAffix = customFilter[0].toString().split("\\.")[0];
				// 增加查询的表
				if (!queryAffix.equals("o")) {
					if(tables.indexOf(tableMap.get(queryAffix)) == -1) {
						tables += "," + tableMap.get(queryAffix) + " " + queryAffix;
					}
				}
			}
			//逻辑比较符号右边
			if(customFilter[2].toString().indexOf(".") != -1) {// 如果是以别名+属性名
				String queryAffix = customFilter[2].toString().split("\\.")[0];
				// 增加查询的表
				if (!queryAffix.equals("o")) {
					if(tables.indexOf(tableMap.get(queryAffix)) == -1) {
						tables += "," + tableMap.get(queryAffix) + " " + queryAffix;
					}
				}
			}
		}
		return tables;
	}
	
	/**
	 * 设置日期类型查询
	 * @param listQuery
	 * @throws ParseException
	 */
	public void setQueryDate(ListQuery listQuery) throws ParseException {
		Query hibernatequery = listQuery.getHibernateQuery();
		for (Iterator<String[]> iterator = dateKey.iterator(); iterator.hasNext();) {
			String[] dateKey = (String[]) iterator.next();
			if(!StringUtils.isEmpty(dateKey[1])) {
				if(dateKey[2].equals(QueryType.DATE)) {
					SimpleDateFormat sfdate = new SimpleDateFormat(StringUtil.FORMAT_DATE);
					hibernatequery.setDate(dateKey[0], sfdate.parse(dateKey[1]));
				} else if(dateKey[2].equals(QueryType.DATETIME)) {
					SimpleDateFormat sftime = new SimpleDateFormat(StringUtil.FORMAT_DATETIME);
					hibernatequery.setTimestamp(dateKey[0], sftime.parse(dateKey[1]));
				}
			}
		}
	}

	/**
	 * 判断是否需要增加where关键字
	 * 
	 * @return 需要增加至查询语句并返回true，否则返回false
	 */
	private boolean needWhere() {
		if (hqlQuery.toString().indexOf("where") == -1) {// 增加where关键字
			hqlQuery.append(" where ");
			return true;
		}
		return false;
	}
}
