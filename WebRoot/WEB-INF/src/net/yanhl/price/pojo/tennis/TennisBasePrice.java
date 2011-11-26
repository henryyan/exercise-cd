package net.yanhl.price.pojo.tennis;

import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.pojo.BasicPricePojo;

/**
 * <p><b>Title：</b>羽毛球普通价格--工作日</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class TennisBasePrice extends BasicPricePojo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	/** default constructor */
    public TennisBasePrice() {
    }

    // Property accessors
    public String getFieldType() {
    	return FieldUtil.FIELD_TYPE_BADMINTOON;
    }

}