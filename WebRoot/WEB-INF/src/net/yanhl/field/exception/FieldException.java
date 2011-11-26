package net.yanhl.field.exception;

/**
 * 
 * <p><b>Title：</b> 场地异常</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090915
 */
public class FieldException extends Exception {

	private static final long serialVersionUID = 1L;

	public FieldException() {
		super();
	}

	public FieldException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldException(String message) {
		super(message);
	}

	public FieldException(Throwable cause) {
		super(cause);
	}


}
