package net.yanhl.report.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.yanhl.report.pojo.ReportDaily;

/**
 * <p><b>Title：</b>报表工具类</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class ReportUtil {

	public static Map<String, Object> pareseReportDaily(List<ReportDaily> listReportDaily) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Integer> operCounter = new HashMap<String, Integer>();
		operCounter.put("运营", 0);
		operCounter.put("未开始", 0);
		operCounter.put("闲置", 0);
		operCounter.put("保留", 0);

		double income = 0;

		for (int i = 2; i < listReportDaily.size(); i++) {
			ReportDaily reportDaily = listReportDaily.get(i);
			List<String> activities = reportDaily.getActivities();
			for (String activity : activities) {
				String[] split = activity.split(",");
				String status = split[0];
				double parseDouble = Double.parseDouble(StringUtils.defaultIfEmpty(split[1], "0"));
				if ("未预订".equals(status)) {
					Integer counter = operCounter.get("闲置");
					operCounter.put("闲置", ++counter);
				} else if ("未开始".equals(status)) {
					Integer counter = operCounter.get("未开始");
					operCounter.put("未开始", ++counter);
				} else if ("锻炼".equals(status)) {
					Integer counter = operCounter.get("运营");
					operCounter.put("运营", ++counter);
					income += parseDouble;
				} else if ("保留".equals(status)) {
					Integer counter = operCounter.get(status);
					operCounter.put("保留", ++counter);
				}

			}
		}

		result.put("counter", operCounter);
		result.put("income", income);

		return result;
	}

}
