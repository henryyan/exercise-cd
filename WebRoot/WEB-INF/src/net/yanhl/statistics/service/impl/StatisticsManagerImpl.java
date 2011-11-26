package net.yanhl.statistics.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.dao.FieldDao;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.exception.FieldException;
import net.yanhl.member.dao.MemberDao;
import net.yanhl.report.dao.ReportDao;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.report.util.ReportUtil;
import net.yanhl.statistics.service.StatisticsManager;
import net.yanhl.tactics.exception.TacticsException;
import net.yanhl.tactics.service.TacticsManager;
import net.yanhl.util.DateUtil;

/**
 * <p><b>Title：</b>首页统计业务</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
@Service(value="statisticsManager")
@Transactional(rollbackFor=Exception.class)
public class StatisticsManagerImpl extends BaseManagerImpl implements StatisticsManager {

	@Resource
	protected ReportDao reportDao;
	
	@Resource
	protected MemberDao memberDao;
	
	@Resource
	protected FieldDao fieldDao;
	
	@Resource
	protected TacticsManager tacticsManager;
	
	/**
	 * 场地类型DAO接口
	 */
	@Resource
	protected FieldTypeDao fieldTypeDao;

	@Transactional(readOnly = true)
	public Map<String, Object> getStatisticsDatas(Long venueId) throws Exception {
		Map<String, Object> datas = new HashMap<String, Object>();
		java.sql.Date sysDate = new java.sql.Date(System.currentTimeMillis());

		// 1、当日运营
		List<ReportDaily> reportDaily = reportDao.reportDaily(venueId, DateUtil.getSysdate(), "锻炼");
		Map<String, Object> pareseReportDaily = ReportUtil.pareseReportDaily(reportDaily);
		datas.put("operation", pareseReportDaily);

		// 2、会员中心
		Map<String, Long> memberAmount = new HashMap<String, Long>();

			// 会员卡总数
			Long countMemberCard = memberDao.countMemberCard(venueId);
	
			// 有效会员卡
			Long countEnableMemberCard = memberDao.countEnableMemberCard(venueId);
			
			// 有预定活动的会员
			Long countMemberHasOrder = fieldDao.countMemberHasOrder(venueId, sysDate);
			
			// 添加会员卡统计结果
			memberAmount.put("countMemberCard", countMemberCard);
			memberAmount.put("countEnableMemberCard", countEnableMemberCard);
			memberAmount.put("countMemberHasOrder", countMemberHasOrder);
			datas.put("memeber", memberAmount);

		
		Map<String, HashMap<String, Long>> tempFieldActivityDatas = new HashMap<String, HashMap<String, Long>>();
			
		// 3、统计活动数据
		String[] enableFieldTypes = fieldTypeDao.getEnableFieldTypes(venueId);
		for (String fieldType : enableFieldTypes) {
			Map<String, Long> activityDatas = activityDatas(venueId, fieldType, sysDate);
			tempFieldActivityDatas.put(fieldType, (HashMap<String, Long>) activityDatas);
		}
		datas.put("fields", tempFieldActivityDatas);

		return datas;
	}

	/**
	 * 根据活动类型统计活动数据
	 * @param venueId
	 * @param fieldType
	 * @param sysDate
	 * @throws FieldException
	 * @throws TacticsException
	 * @throws ClassNotFoundException
	 */
	@Transactional(readOnly = true)
	private Map<String, Long> activityDatas(Long venueId, String fieldType, java.sql.Date sysDate) throws FieldException,
			TacticsException, ClassNotFoundException {
		
		Map<String, Long> datas = new HashMap<String, Long>();
		
		// 3、待处理列表
		Long countVerificationStatusActivity = fieldDao.countVerificationStatusActivity(venueId,
				fieldType, sysDate, false);
		Long countInvalidActivity = fieldDao.countInvalidActivity(venueId, fieldType, sysDate);
		
		// 待验证
		datas.put("verification", countVerificationStatusActivity);
		
		// 作废
		datas.put("invalid", countInvalidActivity);

		// 3.1、策略待处理
		List<Object> tacticsActivityList = tacticsManager.tacticsActivityList(venueId, fieldType);
		Object tacticsActivityAmount = tacticsActivityList.get(0) == null ? "0" : tacticsActivityList.get(0);
		datas.put("tacticsActivity", Long.parseLong(tacticsActivityAmount.toString()));
		return datas;
	}

}
