package net.yanhl.member.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.member.dao.MemberDao;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;

/**
 * <p><b>Title：</b> </p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 */
@SuppressWarnings("unchecked")
@Repository(value = "memberDao")
public class MemberDaoHibernateImpl extends BaseDaoHibernate implements MemberDao {

	public List<MemberCard> memberCardList(Long venueId) {
		String hql = "from MemberCard where venueId=?";
		List<MemberCard> result = getHibernateTemplate().find(hql, new Object[] { venueId });
		return result;
	}

	public List<MemberCard> memberCardList(Long venueId, String mobilePhone) {
		String hql = "from MemberCard where venueId=? and mobilePhone=?";
		List<MemberCard> result = getHibernateTemplate().find(hql, new Object[] { venueId, mobilePhone });
		return result;
	}

	public List<CardType> getCardTypes(Long venueId) {
		return getHibernateTemplate().find("from CardType c where venueId = ?", new Object[] { venueId });
	}

	public List<CardUsageRecord> cardUsageRecordList(long venueId, long cardId) {
		String hql = "from CardUsageRecord c where venue_id=? and cardId=? order by usageDate desc,usageTimeSlice";
		return getHibernateTemplate().find(hql, new Object[] { venueId, cardId });
	}

	public Long countMemberCard(Long venueId) {
		String hql = "select count(*) from MemberCard where venueId = ?";
		List<Long> find = getHibernateTemplate().find(hql, new Object[] { venueId });
		return find.get(0);
	}

	public Long countEnableMemberCard(Long venueId) {
		String hql = "select count(*) from MemberCard m where m.venueId = ? and m.periodValidity >= ?";
		List<Long> find = getHibernateTemplate().find(hql, new Object[] { venueId, new java.util.Date() });
		return find.get(0);
	}
}
