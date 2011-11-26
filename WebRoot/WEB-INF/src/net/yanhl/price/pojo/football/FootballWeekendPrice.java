package net.yanhl.price.pojo.football;

import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.pojo.BasicPricePojo;

/**
 * <p><b>Title：</b>羽毛球周末价格</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class FootballWeekendPrice extends BasicPricePojo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	/** default constructor */
    public FootballWeekendPrice() {
    }

    // Property accessors
    public String getFieldType() {
    	return FieldUtil.FIELD_TYPE_BADMINTOON;
    }
}