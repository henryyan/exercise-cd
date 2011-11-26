package mlink.esms;

/**
 * <p><b>Title：</b> 短信异常</p>
 * <p><b>Description：</b>发送短信错误时抛出，如网络异常、短信内容、参数错误</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090801
 */
public class SmsException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SmsException() {
	}

	/**
	 * @param arg0
	 */
	public SmsException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public SmsException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SmsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
