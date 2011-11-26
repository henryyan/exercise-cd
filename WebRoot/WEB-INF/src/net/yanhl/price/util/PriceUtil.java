package net.yanhl.price.util;

import net.yanhl.field.exception.FieldException;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.price.pojo.badminton.BadmintoonBasePrice;
import net.yanhl.price.pojo.badminton.BadmintoonWeekendPrice;
import net.yanhl.price.pojo.football.FootballBasePrice;
import net.yanhl.price.pojo.football.FootballWeekendPrice;
import net.yanhl.price.pojo.tennis.TennisBasePrice;
import net.yanhl.price.pojo.tennis.TennisWeekendPrice;

import org.apache.commons.lang.StringUtils;

/**
 * 价格工具类
 * @author 闫洪磊
 *
 */
public class PriceUtil {

	/**
	 * 价格类型--非周末
	 */
	public static final String PRICE_TYPE_BASIC = "basic";
	
	/**
	 * 价格类型--周末
	 */
	public static final String PRICE_TYPE_WEEKEND = "weekend";
	
	/**
	 * 特殊策略价格类型
	 */
	public static final String PRICE_TYPE_TACTICS = "tactics";
	
	
	/**
	 * 获取所有可用的价格类型
	 * @return
	 */
	public static String[] getEnablePriceTypes() {
		return new String[] {PRICE_TYPE_BASIC, PRICE_TYPE_WEEKEND};
	}
	
	/**
	 * 获得字符串形式的场地价格POJO名称
	 * @param fieldType	场地类型
	 * @param priceType	价格类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getPricePojoName(String fieldType, String priceType) throws PriceException {
		if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(priceType)) {
			throw new PriceException("场地类型或价格类型不能为空");
		} else {
			// 羽毛球
			if (fieldType.equals(FieldUtil.FIELD_TYPE_BADMINTOON)) {
				if (priceType.equals(PriceUtil.PRICE_TYPE_BASIC)) {
					return BadmintoonBasePrice.class.getName();
				} else if (priceType.equals(PriceUtil.PRICE_TYPE_WEEKEND)) {
					return BadmintoonWeekendPrice.class.getName();
				} else {
					throw new PriceException("没有和场地类型：" + fieldType + "，价格类型：" + priceType + "匹配的价格");
				}
			}
			// 网球
			else if (fieldType.equals(FieldUtil.FIELD_TYPE_TENNIS)) {
				if (priceType.equals(PriceUtil.PRICE_TYPE_BASIC)) {
					return TennisBasePrice.class.getName();
				} else if (priceType.equals(PriceUtil.PRICE_TYPE_WEEKEND)) {
					return TennisWeekendPrice.class.getName();
				} else {
					throw new PriceException("没有和场地类型：" + fieldType + "，价格类型：" + priceType + "匹配的价格");
				}
			}
			// 足球
			else if (fieldType.equals(FieldUtil.FIELD_TYPE_FOOTBALL)) {
				if (priceType.equals(PriceUtil.PRICE_TYPE_BASIC)) {
					return FootballBasePrice.class.getName();
				} else if (priceType.equals(PriceUtil.PRICE_TYPE_WEEKEND)) {
					return FootballWeekendPrice.class.getName();
				} else {
					throw new PriceException("没有和场地类型：" + fieldType + "，价格类型：" + priceType + "匹配的价格");
				}
			}
			else {
				return "";
			}
		}
	}

	/**
	 * 获得对应场地类型的POJO的空对象
	 * @param fieldType	场地类型
	 * @param priceType	价格类型
	 * @return
	 * @throws ClassNotFoundException
	 * 			找不到fieldType对应的POJO
	 * @throws PriceException 找不到类型对应的值
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BasicPricePojo getPriceObject(String fieldType, String priceType)
			throws ClassNotFoundException, PriceException, InstantiationException, IllegalAccessException {
		if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(priceType)) {
			throw new PriceException("场地类型或价格类型不能为空");
		} else {
			Class<?> priceClass = getPriceClass(fieldType, priceType);
			return (BasicPricePojo) priceClass.newInstance();
		}
	}

	/**
	 * 获得对应场地类型的POJO的CLASS对象
	 * @param fieldType	场地类型
	 * @param priceType	价格类型
	 * @return
	 * @throws ClassNotFoundException
	 * 			找不到fieldType对应的POJO
	 * @throws PriceException 找不到类型对应的值
	 */
	public static Class<?> getPriceClass(String fieldType, String priceType)
			throws ClassNotFoundException, PriceException {
		if (StringUtils.isEmpty(fieldType)) {
			throw new PriceException("场地类型不能为空");
		} else {
			String pojoName = getPricePojoName(fieldType, priceType);
			return Class.forName(pojoName);
		}
	}
	
	/**
	 * 根据价格实体类的Class返回中文类型
	 * @param pojoClass	价格实体类Class
	 * @return
	 */
	public static String getPriceTypeByClass(Class<? extends BasicPricePojo> pojoClass) {
		if (pojoClass == BadmintoonBasePrice.class) {
			return "非周末";
		} else if (pojoClass == BadmintoonWeekendPrice.class) {
			return "周末";
		} else {
			return "未知";
		}
	}
	
}
