package net.yanhl.report.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Title：</b> 每天的报表</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090919
 */
public class ReportDaily {

	private String id;
	private String period;
	private String total;
	private String sumPrice;
	private List<String> activities = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(String sumPrice) {
		this.sumPrice = sumPrice;
	}

	/**
	 * 索引位置<b>0</b>的值为<em>title</em>，[ID=序号,period=时段,total=运营场次,sumPrice=合计时段金额,activities=[1号羽毛球场地, 2号羽毛球场地]<br/>
	 * 索引位置<b>1</b>的值为<em>合计运营场次</em>，数量和 title的activities对应
	 * 索引位置<b>大于等于2</b>的值为<em>各个时段和场地状态及价格</em>
	 * @return
	 */
	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "ID=" + id + ",period=" + period + ",total=" + total + ",sumPrice=" + sumPrice
				+ ",activities=" + activities + "\n";
	}

}
