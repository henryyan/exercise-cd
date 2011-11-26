package junit.test.report.service;

import java.util.List;
import java.util.Map;

import junit.test.BaseTest;
import net.sf.json.JSONObject;
import net.yanhl.field.pojo.activity.AllFieldActivity;
import net.yanhl.report.ReportException;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.report.service.ReportManager;
import net.yanhl.report.util.ReportUtil;
import net.yanhl.util.DateUtil;
import net.yanhl.util.PaymentStyle;

import org.junit.Before;
import org.junit.Test;


public class ReportTest extends BaseTest {

	ReportManager reportManager;

	@Before
	public void setUp() throws Exception {
		reportManager = (ReportManager) context.getBean("reportManager");
	}

	@Test
	public void testReportDaily() {
		try {
			List<ReportDaily> reportDaily = reportManager.reportDaily(1, DateUtil.getSysdate(), "锻炼");
			//System.out.println(reportDaily);
			Map<String, Object> pareseReportDaily = ReportUtil.pareseReportDaily(reportDaily);
			System.out.println(pareseReportDaily);
			JSONObject jo = JSONObject.fromObject(pareseReportDaily);
			System.out.println(jo);
		} catch (ReportException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void orderItemList() {
		try {
			List<AllFieldActivity> orderItemList = reportManager.orderItemList(null);
			for (AllFieldActivity allFieldActivity : orderItemList) {
				System.out.println(allFieldActivity);
			}
		} catch (ReportException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sumProtocolFee() throws ReportException {
		List<Double> sumProtocolFee = reportManager.sumProtocolFee(5l, java.sql.Date.valueOf("2010-07-20"), java.sql.Date.valueOf("2010-07-21"), new PaymentStyle[] {PaymentStyle.快钱});
		System.out.println(sumProtocolFee);
	}

}
