package net.yanhl.base.query;

import javax.servlet.http.HttpServletRequest;

/**
 * <p><b>Title：</b> 列表查询对象</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20080702
*/
public class ListQuery extends BaseQuery {

	private int pageIndex = 0;// 当前页
	private int pageSize = 20;// 每页显示的条数

	@SuppressWarnings("rawtypes")
	public ListQuery(Class masterPojo) {
		super.setMasterPojo(masterPojo);
	}
	
	@SuppressWarnings("rawtypes")
	public ListQuery(Class masterPojo, HttpServletRequest request) {
		super.setMasterPojo(masterPojo);
		super.setRequest(request);
	}

	/**
	 * 每页显示的条数
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页显示的条数
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 当前页
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * 设置当前页
	 * @param pageIndex
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

}
