package net.yanhl.base.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.yanhl.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * <p><b>Title：</b> 查询基类</p>
 * <p><b>Description：</b> 临时保存查询条件</p>
 * @author	闫洪磊
 * @version	1.0.0.20020080613
*/
public class BaseQuery {
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String EQ = "=";
	public static final String NE = "!=";
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String LE = "<=";
	public static final String GE = ">=";
	public static final String LIKE = "like";

	public static final String ORDER_ASEC = "asec";
	public static final String ORDER_DSEC = "desc";

	private Class<?> masterPojo;// 查询主类
	private String[] ownerLabel;// 创建人ID标识
	private HttpServletRequest request;
	private Query hibernateQuery = null;// hibernate的查询对象
	private Map<String, String> orderByMap = new HashMap<String, String>();// 排序的字段
	private Map<String, String> tables = new HashMap<String, String>();
	private Map<String, String[]> queryFilter = new HashMap<String, String[]>();// 查询条件--自动从request中获取值
	private List<Object[]> customFilter = new ArrayList<Object[]>();// 自定义查询条件


	/**
	 * 自定义查询条件
	 * @return	自定义查询条件对象
	 */
	public List<Object[]> getCustomFilter() {
		return customFilter;
	}

	/**
	 * Hibernate查询对象
	 * @return
	 */
	public Query getHibernateQuery() {
		return hibernateQuery;
	}

	public void setHibernateQuery(Query hibernateQuery) {
		this.hibernateQuery = hibernateQuery;
	}

	public Class<?> getMasterPojo() {
		return masterPojo;
	}

	public void setMasterPojo(Class<?> masterPojo) {
		this.masterPojo = masterPojo;
	}

	public Map<String, String> getTables() {
		return tables;
	}

	public Map<String, String[]> getQueryFilter() {
		return queryFilter;
	}

	public String[] getOwnerLabel() {
		return ownerLabel;
	}

	public void setOwnerLabel(String[] ownerLabel) {
		this.ownerLabel = ownerLabel;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * <b>参数：</b><排序字段, 排序类型[升序,降序]><br/>
	 * {@link #ORDER_ASEC}
	 * {@link #ORDER_DSEC}
	 * @return	排序MAP
	 */
	public Map<String, String> getOrderByMap() {
		return orderByMap;
	}

	/**
	 * 组合排序字段
	 *
	 * @return 符合hql语言的排序字段
	 */
	public String getOrderBy() {
		String orderBy = "";
		Set<String> keySet = orderByMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			orderBy += key + " " + StringUtils.defaultIfEmpty(orderByMap.get(key), "") + ",";
		}
		return StringUtil.deleteComma(orderBy);
	}
}
