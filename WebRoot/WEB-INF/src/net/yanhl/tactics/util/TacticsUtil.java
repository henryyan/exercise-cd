package net.yanhl.tactics.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsDate;
import net.yanhl.util.DateUtil;

/**
 * <p><b>Title：</b> 价格策略工具类</p>
 * <p><b>Description：</b>提供基于策略的工具方法</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091219
 */
public class TacticsUtil {
	
	static Log log = LogFactory.getLog(TacticsUtil.class);

	/**
	 * 判断指定日期是否在价格策略范围之内
	 * @param tacticsList	价格策略列表
	 * @param usableDate	指定日期
	 * @return	在范围内true，否则false
	 */
	public static boolean betweenTacticsDate(List<Tactics> tacticsList, java.sql.Date usableDate) {
		if (usableDate == null) {
			return false;
		}
		
		for (Tactics tactics : tacticsList) {
			if (tactics.getTacticsDates().isEmpty()) {
				continue;
			} else {
				Set<TacticsDate> tacticsDates = tactics.getTacticsDates();
				for (TacticsDate tacticsDate : tacticsDates) {
					boolean betweenDays = DateUtil.betweenDays(tacticsDate.getFromDate(), tacticsDate.getToDate(), usableDate);
					if (betweenDays) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 判断指定日期是否在价格策略范围之内
	 * @param tacticsList	价格策略列表
	 * @param usableDate	指定日期
	 * @return	在范围内true，否则false
	 */
	public static List<BasicPricePojo> getBetweenTacticsPrice(List<Tactics> tacticsList, java.sql.Date usableDate) {
		List<BasicPricePojo> prices = new ArrayList<BasicPricePojo>();
		if (usableDate == null) {
			return prices;
		}
		
		for (Tactics tactics : tacticsList) {
			if (tactics.getTacticsDates().isEmpty()) {
				continue;
			} else {
				Set<TacticsDate> tacticsDates = tactics.getTacticsDates();
				for (TacticsDate tacticsDate : tacticsDates) {
					boolean betweenDays = DateUtil.betweenDays(tacticsDate.getFromDate(), tacticsDate.getToDate(), usableDate);
					if (betweenDays) {
						prices.addAll(tactics.getTacticsPrices());
						log.debug("场地类型：<" + tactics.getFieldType() + ">，日期：<" + usableDate + ">的价格策略[" + tactics.getTacticsName() + "]范围内");
					}
				}
			}
		}
		
		return prices;
	}
	
}
