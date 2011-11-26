package net.yanhl.tactics.exception;

/**
 * <p><b>Title：</b> 策略日期异常</p>
 * <p><b>Description：</b>设置策略日期出现异常</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100317
 */
public class TacticsDateException extends Exception {

	private static final long serialVersionUID = 1L;

	public TacticsDateException() {
		super();
	}

	public TacticsDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TacticsDateException(String message) {
		super(message);
	}

	public TacticsDateException(Throwable cause) {
		super(cause);
	}

}
