package net.yanhl.test;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.yanhl.util.DateUtil;

public class Test {
	public static void main(String[] args) {
		JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
        java.sql.Date d = new java.sql.Date(System.currentTimeMillis());

        // 直接序列化
        JsonConfig config = new JsonConfig();
        JSONObject json = beanProcessor.processBean(d, config);
        System.out.println(json.toString());
        
        System.out.println(DateUtil.getWeekDay("2009-06-07"));
        
        String startDate = DateUtil.getSysdate();
        Date dateAdd = DateUtil.dateAdd(startDate, 0);
        System.out.println(DateUtil.format(dateAdd, "date"));
        
        System.out.println("15:20:00".substring(0, 5));
        
        
	}
}
