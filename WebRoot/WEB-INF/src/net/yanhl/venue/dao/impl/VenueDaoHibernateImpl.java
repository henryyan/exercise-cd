package net.yanhl.venue.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.venue.dao.VenueDao;
import net.yanhl.venue.pojo.BusinessInfo;
import net.yanhl.venue.pojo.VenueUser;

/**
 * <p><b>Title：</b> </p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
@SuppressWarnings("unchecked")
@Repository(value="venueDao")
public class VenueDaoHibernateImpl extends BaseDaoHibernate implements VenueDao{

	
	public VenueUser findByNameAndPass(String name, String pass) {
		String[] args = {name,pass};
		List<VenueUser> users = getHibernateTemplate().find("from VenueUser where username=? and password=?",args);
		if(users.size() >=1) {
			return users.get(0);
		} else {
			return null;
		}
	}

	public BusinessInfo loadBusinessInfo(long venueInfoId) {
		Long[] args = {venueInfoId};
		List<BusinessInfo> infos = getHibernateTemplate().find("from BusinessInfo where venueInfo.id=?",args);
		if(infos.size() >=1) {
			return infos.get(0);
		} else {
			return null;
		}
	}
	
	public int setVenuePicture(final Long venueInfoId, final String pictureRealName) {
		Object execute = super.getHibernateTemplate().execute(new HibernateCallback() {
			String hql = "update VenueInfo set photoUrl=? where id=?";
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setString(0, pictureRealName).setLong(1, venueInfoId);
				return query.executeUpdate();
			}
		});
		return Integer.parseInt(StringUtils.defaultIfEmpty(execute.toString(), "0"));
	}
	
}
