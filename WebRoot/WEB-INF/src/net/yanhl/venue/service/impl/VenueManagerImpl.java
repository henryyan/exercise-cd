package net.yanhl.venue.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.yanhl.base.exception.EmptyValueException;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.venue.dao.VenueDao;
import net.yanhl.venue.exception.VenueException;
import net.yanhl.venue.pojo.BusinessInfo;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;
import net.yanhl.venue.service.VenueManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 场馆业务管理实现类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090528
 */
@Service(value="venueManager")
@Transactional(rollbackFor=Exception.class)
public class VenueManagerImpl extends BaseManagerImpl implements VenueManager {
	
	@Resource
	protected VenueDao venueDao;

	@Transactional(readOnly = true)
	public VenueUser getUserByNameAndPass(String name, String pass) {
		return venueDao.findByNameAndPass(name, pass);
	}

	@Transactional(readOnly = true)
	public BusinessInfo loadBusinessInfo(long venueInfoId) {
		return venueDao.loadBusinessInfo(venueInfoId);
	}
	
	@Transactional(readOnly = true)
	public boolean checkUserExist(String venueUserName) throws VenueException, EmptyValueException {
		if(StringUtils.isEmpty(venueUserName)) {
			throw new EmptyValueException("场馆用户名不能为空");
		}
		List<VenueUser> findBy = findBy(VenueUser.class, "username", venueUserName);
		if(findBy.isEmpty()) {
			return false;
		}
		return true;
	}

	@Transactional(readOnly = true)
	public VenueInfo getVenueByName(String venueName) throws EmptyValueException {
		if(StringUtils.isEmpty(venueName)) {
			throw new EmptyValueException("场馆名称不能为空");
		}
		List<VenueInfo> findBy = findBy(VenueInfo.class, "venueName", venueName);
		if(!findBy.isEmpty()) {
			return findBy.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public String getVenuePicture(Long venueInfoId) {
		Object findBy = findBy(VenueInfo.class, "photoUrl", "id", venueInfoId);
		if (findBy == null) {
			return "";
		}
		return findBy.toString();
	}
	
	/**
	 * 设置图片路径
	 * @param id
	 * @return
	 */
	public int setVenuePicture(Long venueInfoId, String pictureRealName) {
		return venueDao.setVenuePicture(venueInfoId, pictureRealName);
	}

}
