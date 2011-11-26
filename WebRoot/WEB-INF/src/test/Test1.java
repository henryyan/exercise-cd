package test;

import java.net.URLDecoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Test1 {

	/**
	 * @param args
	 */
	@SuppressWarnings({ "deprecation", "rawtypes"})
	public static void main(String[] args) {

		Calendar serverCa = Calendar.getInstance();
		serverCa.set(Calendar.SECOND, 0);
		Date serverTime = serverCa.getTime();

		// 开始时间
		Calendar ca1 = Calendar.getInstance();
		ca1.setTime(serverTime);
		ca1.set(Calendar.HOUR_OF_DAY, 11);
		ca1.set(Calendar.MINUTE, 0);
		ca1.set(Calendar.SECOND, 0);

		// 结束时间
		Calendar endCa = Calendar.getInstance();
		endCa.setTime(serverTime);
		endCa.set(Calendar.HOUR_OF_DAY, 19);
		endCa.set(Calendar.MINUTE, 0);
		endCa.set(Calendar.SECOND, 0);

		System.out.println(" 系统时间=" + serverTime.getTime() + "\t" + serverTime.toLocaleString());
		System.out.println("结束时间" + endCa.getTime().toLocaleString());
		System.out.println((endCa.getTimeInMillis() - serverTime.getTime()) / 1000);
		// System.out.println(ca1.getTimeInMillis() + "\t" +
		// ca2.getTimeInMillis());
		// System.out.println(ca1.getTime().toLocaleString() + "\t" +
		// ca2.getTime().toLocaleString());

		// long miss = ca2.getTimeInMillis() - serverTime.getTime();
		// double s1 = miss/1000;
		// System.out.println("ca1与ca2相差" + s1);
		System.out.println("09:29~10:38".split("~").length);

		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration("conf/sms.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		String smsContent1 = config.getString("sms.booking");
		System.out.println(smsContent1);

		String s = "您已预订上海外国语学院羽毛球馆2009/9/8,11:00~12:00的场地。请保留验证码ALQL并在锻炼时认证。[我要";
		System.out.println(s.length());
		
//		Time time = new Time(System.currentTimeMillis());
//		System.out.println(time.toString());
//		
//		Time valueOf = Time.valueOf("19:34:00");
//		System.out.println(valueOf.toString());
//		
//		System.out.println(time.getTime() > valueOf.getTime());
		
		Properties props=System.getProperties();
		Iterator iter=props.keySet().iterator();
		while(iter.hasNext())
		{
		String key=(String)iter.next();
		System.out.println(key+" = "+ props.get(key));
		} 
		Time time = new Time(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		String format = sf.format(time);
		System.out.println(format);
		
		Long[] aa = new Long[] {2l,3l,4l};
		System.out.println(aa instanceof Object[]);
		
		
		System.out.println(URLDecoder.decode("%E7%BD%91%E7%90%83"));
	}

}
