package net.yanhl.servlet;

import java.sql.Date;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.yanhl.field.engine.FieldActivityEngine;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class CreateActivity extends HttpServlet {

	Log log = LogFactory.getLog(this.getClass());

	protected FieldActivityEngine fieldActivityEngine;

	public void setFieldActivityEngine(FieldActivityEngine fieldActivityEngine) {
		this.fieldActivityEngine = fieldActivityEngine;
	}

	static Date currentDate = new Date(System.currentTimeMillis());

	public void init() throws ServletException {
		super.init();
		ApplicationContext ct = WebApplicationContextUtils.getWebApplicationContext(this
				.getServletContext());
		setFieldActivityEngine((FieldActivityEngine) ct.getBean("fieldActivityEngine"));
		System.out.println("开始创建场地活动列表，Manager对象=" + fieldActivityEngine);
		new CreateActivityThread(fieldActivityEngine).start();
	}

	/**
	 * <p><b>Title：</b>生成活动的线程服务</p>
	 * <p><b>Description：</b>第一次执行生成一次活动，之后根据配置文件设置的间隔时间定时启动一个TimerTask</p>
	 *
	 * @author 闫洪磊
	 */
	class CreateActivityThread extends Thread {

		private FieldActivityEngine fieldActivityEngine;

		CreateActivityThread(FieldActivityEngine fieldActivityEngine) {
			this.fieldActivityEngine = fieldActivityEngine;
		}

		public void run() {
			try {
				fieldActivityEngine.createActivityForServlet();
			} catch (Exception e) {
				log.error("生成场地活动时报错：" + e.getMessage(), e);
			}

			String systemProperties = "conf/system.properties";
			String timeKey = "field.issue.internal.time";
			int issueInternal = 0;
			try {
				PropertiesConfiguration config = new PropertiesConfiguration(systemProperties);
				issueInternal = config.getInt(timeKey);
				log.debug("从配置文件" + systemProperties + "中读取发布间隔时间" + issueInternal + "分钟");
			} catch (ConfigurationException e) {
				e.printStackTrace();
				log.warn("从配置文件" + systemProperties + "中读取发布间隔时间出错");
				issueInternal = 10;
			} catch (NoSuchElementException e) {
				e.printStackTrace();

				// 默认设置为10分钟
				issueInternal = 10;
				log.warn("从配置文件" + systemProperties + "中读取发布间隔时间出错，默认设置为 10 分钟");
			}

			CreateActivityTimerTask yourTask = new CreateActivityTimerTask(fieldActivityEngine);
			Timer timer = new Timer();
			timer.schedule(yourTask, 0, issueInternal);
		}

	}

	/**
	 * <p><b>Title：</b>生成活动循环任务</p>
	 * <p><b>Description：</b></p>
	 *
	 * @author 闫洪磊
	 */
	public static class CreateActivityTimerTask extends TimerTask {

		Log log = LogFactory.getLog(this.getClass());
		private FieldActivityEngine fieldActivityEngine;

		CreateActivityTimerTask(FieldActivityEngine fieldActivityEngine) {
			this.fieldActivityEngine = fieldActivityEngine;
		}

		public void run() {
			Date tempDate = new Date(System.currentTimeMillis());
			Calendar caTemp = Calendar.getInstance();
			caTemp.setTime(tempDate);
			
			Calendar caCurrent = Calendar.getInstance();
			caCurrent.setTime(currentDate);
			
			if (caTemp.get(Calendar.YEAR) == caCurrent.get(Calendar.YEAR)
					&& caTemp.get(Calendar.MONTH) == caCurrent.get(Calendar.MONTH)
					&& caTemp.get(Calendar.DAY_OF_MONTH) == caCurrent.get(Calendar.DAY_OF_MONTH)) {
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					fieldActivityEngine.createActivityForServlet();
				} catch (Exception e) {
					log.error("生成场地活动时报错：" + e.getMessage(), e);
				}
				currentDate = tempDate;
			}
		}

	}
}