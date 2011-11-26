package net.yanhl.price.pojo.badminton;

import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.pojo.BasicPricePojo;

/**
 * <p><b>Title：</b>羽毛球普通价格--工作日</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class BadmintoonBasePrice extends BasicPricePojo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	/** default constructor */
    public BadmintoonBasePrice() {
    }

    // Property accessors
    public String getFieldType() {
    	return FieldUtil.FIELD_TYPE_BADMINTOON;
    }

}