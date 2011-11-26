package net.yanhl.config.exception;

/**
 * <p><b>Title：</b>解析配置XML异常类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public class ConfigReadXmlException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigReadXmlException() {
		super();
	}

	public ConfigReadXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigReadXmlException(String message) {
		super(message);
	}

	public ConfigReadXmlException(Throwable cause) {
		super(cause);
	}

}
