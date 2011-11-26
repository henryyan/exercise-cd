package net.yanhl.member.pojo;

/**
 * <p><b>Title：</b>基础会员卡对象</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100206
 */
public abstract class BaseMemberCard implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected String cardStatus;
	
	/**
	 * 根据有效期判断状态，分别为[正常、过期、未知]
	 * @return	中文状态
	 */
	public abstract String getCardStatus();
}
