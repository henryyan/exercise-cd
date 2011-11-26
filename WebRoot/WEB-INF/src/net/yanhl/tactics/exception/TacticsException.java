package net.yanhl.tactics.exception;

/**
 * <p><b>Title：</b> 策略异常</p>
 * <p><b>Description：</b>制定策略出现异常</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091205
 */
public class TacticsException extends Exception {

	private static final long serialVersionUID = 1L;

	public TacticsException() {
		super();
	}

	public TacticsException(String message, Throwable cause) {
		super(message, cause);
	}

	public TacticsException(String message) {
		super(message);
	}

	public TacticsException(Throwable cause) {
		super(cause);
	}

}
