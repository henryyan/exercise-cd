package junit.test.member.dao;


import junit.test.BaseTest;
import net.yanhl.member.dao.MemberDao;

import org.junit.Before;
import org.junit.Test;

public class MemberDaoTest extends BaseTest {

	MemberDao dao;

	@Before
	public void setUp() throws Exception {
		dao = (MemberDao) context.getBean("memberDao");
	}

	@Test
	public void countMemberCard() {
		Long countMemberCard = dao.countMemberCard(1l);
		System.out.println(countMemberCard);
	}

	@Test
	public void countEnableMemberCard() {
		Long countMemberCard = dao.countEnableMemberCard(1l);
		System.out.println(countMemberCard);
	}

}
