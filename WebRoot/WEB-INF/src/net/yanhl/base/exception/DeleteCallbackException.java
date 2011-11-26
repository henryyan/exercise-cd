package net.yanhl.base.exception;

/**
 * <p><b>Title：</b>删除回调异常类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090109
 */
public class DeleteCallbackException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeleteCallbackException() {
		super();
	}

	public DeleteCallbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeleteCallbackException(String message) {
		super(message);
	}

	public DeleteCallbackException(Throwable cause) {
		super(cause);
	}

}
