package net.yanhl.price.exception;

/**
 * 价格异常
 * @author HenryYan
 *
 */
public class PriceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PriceException() {
		super();
	}

	public PriceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PriceException(String message) {
		// TODO Auto-generated constructor stub
	}

	public PriceException(Throwable cause) {
		super(cause);
	}

}
