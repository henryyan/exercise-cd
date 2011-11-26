package net.yanhl.base.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.query.util.QueryUtil;
import net.yanhl.base.service.BaseManager;
import net.yanhl.util.SysConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.actions.MappingDispatchAction;

/**
 * <p><b>Title：</b> 系统架构基础Action类</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20090211
*/
public class BaseAction extends MappingDispatchAction {

	static Log log = LogFactory.getLog(BaseAction.class);

	public static final String RESPONSE_ERROR = SysConstants.RESPONSE_ERROR;
	public static final String RESPONSE_SUCCESS = SysConstants.RESPONSE_SUCCESS;
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_SUCCESS = "success";
	public static final String RESULT_COLLECT = "resultList";
	
	protected BaseManager baseManager;
	
	public BaseManager getBaseManager() {
		return baseManager;
	}

	@Resource
	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	/** *********************************设置分页-开始*********************************** */

	/**
	 * 1、设置页码<br/>
	 * 2、设置排序<br/>
	 * 3、设置查询条件
	 * @param request
	 * @param listQuery		查询对象
	 * @param listResult	查询结果
	 * @throws Exception
	 */
	public static void queryFilter(HttpServletRequest request, ListQuery listQuery, JSONObject listResult) throws Exception {

		JSONObject params = readJson(request);
		/*
		 * 设置页码
		 */
		int page = params.getInt("page");
		int perPageNum = params.getInt("rows");
		listQuery.setPageIndex(page - 1);
		if (!listResult.containsKey("page")) {
			listResult.accumulate("page", page);
		}
		listQuery.setPageSize(perPageNum);

		// 设置排序
		String sidx = params.getString("sidx");
		String sord = params.getString("sord");
		if (StringUtils.isNotEmpty(sidx) && StringUtils.isNotEmpty(sord)) {
			listQuery.getOrderByMap().put(sidx, sord);
		}

		boolean search = params.getBoolean("_search");
		if (search) {

			/*
			 * 设置查询条件
			 */

			// 是否为多个查询条件
			boolean multiFilter = false;
			Object searchFieldObj = params.get("searchField");
			Object filtersObj = params.get("filters");
			if (filtersObj != null) {
				multiFilter = true;
			}

			// 处理多个查询条件
			if (multiFilter) {
				JSONObject filtersJson = params.getJSONObject("filters");
				// TODO 添加jqGrid的AND或者OR的关系设置
//				String groupOp = filtersJson.getString("groupOp");
				JSONArray rules = filtersJson.getJSONArray("rules");
				for (Object object : rules) {
					JSONObject rule = (JSONObject) object;

					String searchField = rule.getString("field");
					String isos = java.net.URLDecoder.decode(rule.getString("data"), "ISO8859-1");
					String searchString = java.net.URLDecoder.decode(isos, "UTF-8");
					String searchOper = rule.getString("op");

					String queryOperation = QueryUtil.getJqGridAndLogicMap().get(searchOper);
					listQuery.getCustomFilter().add(new Object[] {"o." + searchField, queryOperation, searchString});
				}
			}
			// 处理单个查询条件
			else if (searchFieldObj != null){
				String searchField = searchFieldObj.toString();
				String searchString = params.getString("searchString");
				String searchOper = params.getString("searchOper");

				String queryOperation = QueryUtil.getJqGridAndLogicMap().get(searchOper);
				listQuery.getCustomFilter().add(new Object[] {"o." + searchField, queryOperation, searchString});
			}

		}

	}

	/**
	 * 保存分页-总数和结果集
	 *
	 * @param request
	 * @param result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void savePagination(HttpServletRequest request, List result, Class resultObj) {
		if (result == null || result.isEmpty()
				|| result.get(0) == null) {
			return;
		}
		request.setAttribute("resultSize", new Long((Long) result.get(0)).intValue());// 将总记录数保存成Intger实例保存在request中
		List details = (List) result.get(1);// 结果集
		List newList = new ArrayList();// 最终结果
		for (Iterator iter = details.iterator(); iter.hasNext();) {
			Object obj1 = iter.next();
			if(obj1.getClass().getName().equals(resultObj.getName())) {
				request.setAttribute(RESULT_COLLECT, details);//只从一个表中查询数据
				return;
			} else {//从多个表查询数据
				Object[] obj2 = (Object[]) obj1;
				newList.add(obj2[0]);
			}
		}
		request.setAttribute(RESULT_COLLECT, newList);// 把结果存入request
	}

	/**
	 * 保存分页-总数和结果集
	 * @param request		{@link HttpServletRequest}
	 * @param listQuery		查询对象
	 * @param result		需要保存的结果集
	 * @param resultObj		POJO类型
	 * @param listResult	JSON对象
	 * @param config		JSON输出配置，如果为NULL使用默认的
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void saveSearchResult(ListQuery listQuery, List result, JSONObject listResult, JsonConfig config) {
		if (result == null || result.isEmpty()
				|| result.get(0) == null) {
			return;
		}

		// 总页数
		Long recordTotal = (Long) result.get(0);
		if (!listResult.containsKey("total")) {

			int pageTotal = (int) (recordTotal.longValue()/listQuery.getPageSize());
			if (recordTotal.longValue() % listQuery.getPageSize() > 0) {
				pageTotal++;
			}
			listResult.accumulate("total", pageTotal);
		}

		// 记录总数
		listResult.accumulate("records", recordTotal);

		List details = (List) result.get(1);// 结果集
		List newList = new ArrayList();// 最终结果
		for (Iterator iter = details.iterator(); iter.hasNext();) {
			Object obj1 = iter.next();
			if(obj1.getClass().getName().equals(listQuery.getMasterPojo().getName())) {
				//request.setAttribute(RESULT_COLLECT, details);//只从一个表中查询数据
				newList = details;
			} else {//从多个表查询数据
				Object[] obj2 = (Object[]) obj1;
				newList.add(obj2[0]);
			}
		}
		if (config == null) {
			config = new JsonConfig();
			config.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
			config.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());
		}
		JSONArray fromObject = JSONArray.fromObject(newList, config);

		// 当前页结果集
		listResult.accumulate("rows", fromObject);
	}

	/** *********************************设置分页-结束*********************************** */

	/**
	 * 设置Attribute
	 *
	 * @param request
	 * @param name
	 * @param obj
	 */
	protected void setAttribute(String name, Object obj, HttpServletRequest request) {
		request.setAttribute(name, obj);
	}

	/**
	 * 用URL参数中值设置成请求属性
	 * @param name
	 * @param request
	 * @throws UnsupportedEncodingException 	当编码不被支持时
	 */
	protected void setAttributeFromParameter(String name, HttpServletRequest request) throws UnsupportedEncodingException {
		String parameter = java.net.URLDecoder.decode(request.getParameter(name), "UTF-8");
		request.setAttribute(name, parameter);
	}

	/**
	 * 输出到客户端
	 *
	 * @param response
	 * @param info
	 * @throws IOException
	 */
	protected void print(String info, HttpServletResponse response) throws IOException {
		try {
			response.getWriter().print(info);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 输出成功信息
	 *
	 * @param response
	 * @throws IOException
	 */
	protected void printSuccess(HttpServletResponse response) throws IOException {
		try {
			response.getWriter().print(RESPONSE_SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 输出错误信息标志
	 *
	 * @param response
	 * @throws IOException
	 */
	protected void printErrorLabel(HttpServletResponse response) throws IOException {
		try {
			response.getWriter().print(RESPONSE_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 输出错误信息内容
	 *
	 * @param response
	 * @throws IOException
	 */
	protected void printErrorInfo(String errorInfo, HttpServletResponse response) throws IOException {
		print(errorInfo, response);
	}

	/**
	 * 读取请求参数解析为JSON数据格式
	 *
	 * @param request
	 * @return json格式的String对象
	 * @throws Exception
	 */
	public static JSONObject readJson(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = request.getParameterMap();
			Iterator<String> paIter = parameterMap.keySet().iterator();
			while (paIter.hasNext()) {
				String key = paIter.next().toString();
				String[] values = (String[])parameterMap.get(key);
				if(values.length == 1) {
					jsonObject.accumulate(key, java.net.URLDecoder.decode(values[0], "UTF-8"));
				} else if(values.length > 1) {
					jsonObject.accumulate(key, values);
				}
			}
			log.debug("从客户端获得json=" + jsonObject.toString());
		} catch (Exception e) {
			log.error("获取json数据出错，错误信息如下：\n\t" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonObject;
	}

	/**
	 * 根据响应结果返回JSON对象
	 * @param handleResult
	 * 			结构：[响应状态，错误信息]
	 */
	public JSONObject getJsonByHandleResult(String[] handleResult) {
		JSONObject result = new JSONObject();
		result.accumulate(SysConstants.RESPONSE_KEY, handleResult[0]);
		result.accumulate(SysConstants.RESPONSE_KEY_ERROR, handleResult[1]);
		return result;
	}

	/**
	 * 判断是否为Ajax请求
	 * @param request	HttpServletRequest
	 * @return	是true, 否false
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if(requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}

}