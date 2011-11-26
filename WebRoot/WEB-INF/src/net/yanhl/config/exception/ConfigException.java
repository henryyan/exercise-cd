package net.yanhl.config.exception;

/**
 * <p><b>Title：</b>配置读取异常类</p>
 * <p><b>Description：</b>读取XML数据异常类</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091226
 */
public class ConfigException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

}
