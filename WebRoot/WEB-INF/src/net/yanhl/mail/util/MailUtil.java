package net.yanhl.mail.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailUtil extends JavaMailSenderImpl {
	
	Log log = LogFactory.getLog(this.getClass());

	/**
	 * Send simple message
	 * @param from	发送方
	 * @param to	接收方
	 * @param cc	转发方，如果多个用逗号分隔
	 * @param subject	邮件主题
	 * @param text	邮件内容
	 * @return
	 */
	public boolean send(String from, String to, String cc, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();

		try {
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			if (cc != null) {
				String[] ccs = cc.split(",");
				if (ccs.length > 1) {
					message.setCc(ccs);
				} else {
					message.setCc(cc);
				}
			}
			message.setText(text);
			this.send(message);
			log.info("发送邮件：from=" + from + ", to=" + to + ", cc=" + cc + ", subject=" + subject + ", text=" + text);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	/**
	 * Send MIME message
	 * @param from	发送方
	 * @param to	接收方
	 * @param cc	转发方
	 * @param filePath	文件路径
	 * @param subject	邮件主题
	 * @return
	 */
	public boolean sendFile(String from, String to, String cc, String filePath, String subject) {
		MimeMessage mailMessage = this.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			if (cc != null) {
				messageHelper.setCc(cc);
			}
			messageHelper.setText(getMailContent(filePath), true);
			this.send(mailMessage);
			log.info("从文件读取发送邮件：from=" + from + ", to=" + to + ", cc=" + cc + ", subject=" + subject + ", filePath=" + filePath);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取文件内容
	 * @param filePath
	 * @return
	 */
	private String getMailContent(String filePath) {
		StringBuffer content = new StringBuffer();
		FileReader fr = null;
		try {
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				content.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (Exception e) {
			}
		}

		return content.toString();
	}

	public static void main(String[] args) {

		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"D:/runchain/projects/exercise/WebRoot/WEB-INF/src/conf/applicationContext.xml");
		MailUtil mailUtil = (MailUtil) ctx.getBean("mailSender");
//		mailUtil.sendFile("service@remote-click.com", "yanhonglei@gmail.com", "d:/temp/navicat_mysql_8.1-sn.txt",
//				"just test");

		mailUtil.send("service@remote-click.com", "yanhonglei@gmail.com", "13764984102@139.com,576525789@qq.com", "新场馆注册343", "text22");

	}

}
