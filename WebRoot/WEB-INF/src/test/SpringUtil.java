package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringUtil {

	public static ApplicationContext getContext() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] {
				"D:/runchain/projects/exercise/WebRoot/WEB-INF/config/applicationContext.xml" });
		return ctx;
	}

}
