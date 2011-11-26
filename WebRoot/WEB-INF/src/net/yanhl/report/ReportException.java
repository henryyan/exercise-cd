package net.yanhl.report;

/**
 * <p><b>Title：</b> 报表异常类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090919
 */
public class ReportException extends Exception {

	private static final long serialVersionUID = 1L;

	public ReportException() {
		super();
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

}
