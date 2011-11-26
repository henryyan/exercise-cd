package net.yanhl.field.engine;

import java.sql.Date;

import net.yanhl.base.service.BaseManager;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.price.exception.PriceException;

/**
 * <p><b>Title：</b>场地活动业务接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public interface FieldActivityEngine extends BaseManager {

	/**
	 * 为系统启动时的Servlet服务,生成<b>所有场馆</b>开启的场地类型的所以场地活动<br/>
	 * 内部调用{@link #createActivity(Long, Date)}
	 * @throws FieldActivityException 通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void createActivityForServlet() throws FieldException, FieldActivityException, PriceException;

	/**
	 * 生成场地活动，发布的范围<b>指定场馆</b>全部已启用的场地，<b>价格策略</b>包括：<em>非周末、周末、特殊价格策略</em>
	 * @param venueId	指定场馆ID，如果为null或者0则默认为全部场地
	 * @param toCreateDate
	 * 			生成从今天到toCreateDate指定的日期的场地活动
	 * 			如果为null则按照正常规则生成(即今天+可预订天数)
	 * @throws FieldActivityException 通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void createActivity(Long venueId, Date toCreateDate) throws FieldException, FieldActivityException, PriceException;

	/**
	 * 生成场地活动，发布的范围<b>指定场馆</b>全部已启用的场地，<b>价格策略</b>包括：<em>非周末、周末、特殊价格策略</em>
	 * @param venueId	指定场馆ID，如果为null或者0则默认为全部场地
	 * @param fieldType	场地类型
	 * @param toCreateDate
	 * 			生成从今天到toCreateDate指定的日期的场地活动
	 * 			如果为null则按照正常规则生成(即今天+可预订天数)
	 * @throws FieldActivityException 通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void createActivity(Long venueId, String fieldType, Date toCreateDate) throws FieldException,
			FieldActivityException, PriceException;

	/**
	 * 使用特殊价格策略生成场地活动，发布范围：所有全部启用的各种场地
	 * @param venueId	指定场馆ID，如果为null或者0则默认为全部场地
	 * @param fromCreateDate	开始生成日期
	 * @param toCreateDate
	 * 			生成从今天到toCreateDate指定的日期的场地活动，
	 * 			如果为null则按照正常规则生成(即今天+可预订天数)
	 * @throws FieldActivityException	通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void insertActivityUseTactics(Long venueId, Date fromCreateDate, Date toCreateDate) throws FieldException,
			FieldActivityException, PriceException;

	/**
	 * 使用特殊价格策略生成场地活动，发布范围：指定场地类型的全部已启用的场地，价格范围：周末、非周末、特殊策略价格
	 * @param venueId	指定场馆ID，如果为null或者0则默认为全部场地
	 * @param fieldType	场地类型
	 * @param fromCreateDate	开始生成日期
	 * @param toCreateDate
	 * 			生成从今天到toCreateDate指定的日期的场地活动，
	 * 			如果为null则按照正常规则生成(即今天+可预订天数)
	 * @throws FieldActivityException	通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void insertActivityUseTactics(Long venueId, String fieldType, Date fromCreateDate, Date toCreateDate)
			throws FieldException, FieldActivityException, PriceException;

	/**
	 * 使用默认策略生成活动，发布范围：<b>指定场馆</b>下全部已启用的场地，<b>价格策略</b>包括：<em>非周末、周末</em>
	 * @param venueId			指定场馆ID
	 * @param fromDate	开始生成日期
	 * @param toDate
	 * 			生成从今天到toCreateDate指定的日期的场地活动
	 * 			如果为null则按照正常规则生成(即今天+可预订天数)
	 * @throws FieldException
	 * @throws FieldActivityException	通过场地类型设置活动对象时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void insertActivityUseDefault(Long venueId, Date fromDate, Date toDate) throws FieldException,
			FieldActivityException, PriceException;

}
