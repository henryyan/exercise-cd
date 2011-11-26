package net.yanhl.venue.exception;

/**
 * <p><b>Title：</b> 场馆异常类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.200909126
 */
public class VenueException extends Exception {

	private static final long serialVersionUID = 1L;

	public VenueException() {
		super();
	}

	public VenueException(String message, Throwable cause) {
		super(message, cause);
	}

	public VenueException(String message) {
		super(message);
	}

	public VenueException(Throwable cause) {
		super(cause);
	}

}
