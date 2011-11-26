package net.yanhl.field.exception;

/**
 * <p><b>Title：</b>场地类型异常</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public class FieldTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public FieldTypeException() {
		super();
	}

	public FieldTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldTypeException(String message) {
		super(message);
	}

	public FieldTypeException(Throwable cause) {
		super(cause);
	}

}
