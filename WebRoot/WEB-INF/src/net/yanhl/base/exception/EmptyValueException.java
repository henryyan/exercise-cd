package net.yanhl.base.exception;

/**
 * <p><b>Title：</b> 空数据异常</p>
 * <p><b>Description：</b>当要使用的数据为空时抛出此异常</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090926
 */
public class EmptyValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmptyValueException() {
		super();
	}

	public EmptyValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyValueException(String message) {
		super(message);
	}

	public EmptyValueException(Throwable cause) {
		super(cause);
	}

}
