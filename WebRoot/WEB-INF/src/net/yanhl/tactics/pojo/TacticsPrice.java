package net.yanhl.tactics.pojo;

import net.yanhl.price.pojo.BasicPricePojo;

/**
 * <p><b>Title：</b>策略价格POJO</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class TacticsPrice extends BasicPricePojo implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Tactics tactics;

	// Constructors

	/** default constructor */
	public TacticsPrice() {
	}

	/** minimal constructor */
	public TacticsPrice(Long id) {
		this.id = id;
	}
	// Property accessors

	public Tactics getTactics() {
		return tactics;
	}

	public void setTactics(Tactics tactics) {
		this.tactics = tactics;
	}

	@Override
	public String getFieldType() {
		return tactics.getFieldType();
	}

}