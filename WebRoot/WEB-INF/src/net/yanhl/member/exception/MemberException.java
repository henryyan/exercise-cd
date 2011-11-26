package net.yanhl.member.exception;

/**
 * <p><b>Title：</b> 会员卡异常</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090919
 */
public class MemberException extends Exception {

	private static final long serialVersionUID = 1L;

	public MemberException() {
		super();
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemberException(String message) {
		super(message);
	}

	public MemberException(Throwable cause) {
		super(cause);
	}
	
}
