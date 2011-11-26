package junit.test.member.service;


import junit.test.BaseTest;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.member.service.MemberManager;

import org.junit.Before;
import org.junit.Test;

public class MemberTest extends BaseTest {
	
	MemberManager memberManager;

	@Before
	public void setUpBefore() throws Exception {
		memberManager = (MemberManager) context.getBean("memberManager");
	}

	@Test
	public void addRecord() {
		try {
			memberManager.recharge(1l, 59l, 100);
//			CardUsageRecord record = new CardUsageRecord();
			//record.setId(499l);
//			memberManager.save(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addMemberCard() {
		MemberManager memberManager = (MemberManager) context.getBean("memberManager");
//		String jt = "{'oper':'add','cardNumber':'wef','mobilePhone':'wefw','balance':'ef','cardType':{name:'aaa'},'name':'wef','id':'_empty'}";
		String jt = "{'oper':'add','cardNumber':'wef','mobilePhone':'wefw','balance':'ef','cardType.typeName':'3','name':'wef','id':'_empty'}";
		boolean mayBeJSON = JSONUtils.mayBeJSON(jt);
		System.out.println(mayBeJSON);
//		if (true) {
//			return;
//		}
		JSONObject jsonObject = JSONObject.fromObject(jt);
		Object typeId = jsonObject.get("cardType.typeName");
		jsonObject.remove("cardType.typeName");
		jsonObject.accumulate("cardType", memberManager.get(CardType.class, Long.parseLong(typeId.toString())));
		MemberCard memberCard = (MemberCard) JSONObject.toBean(jsonObject, MemberCard.class);
		System.out.println(memberCard.getCardType());
		memberManager.insertOrUpdate(memberCard);
	}

}
