package net.yanhl.venue.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenuePicture;
import net.yanhl.venue.service.VenueManager;
import net.yanhl.venue.util.VenuePictureUtil;
import net.yanhl.venue.util.VenueUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 场馆图片处理Action
 *
 * @author HenryYan
 *
 */
public class VenuePictureAction extends BaseAction {
	
	Log log = LogFactory.getLog(getClass());
	
	@Resource
	VenueManager venueManager;

	/**
	 * 场馆图片列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward venuePictureList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			ListQuery listQuery = new ListQuery(VenuePicture.class);
			listQuery.setOwnerLabel(new String[] { "o.venueId", UserUtil.getCurrentVenueId(request) });
			List<VenuePicture> pictureList = getBaseManager().find(listQuery);
			request.setAttribute("pictures", pictureList);
			
			VenueInfo venueInfo = UserUtil.getCurrentVenueInfo(request);
			request.setAttribute("majorPicture", venueInfo.getPhotoUrl());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("pictureList");
	}
	
	/**
	 * 删除场馆图片
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deletePicture(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strId = request.getParameter("id");
		Long pictureId = new Long(strId);
		Object object = getBaseManager().get(VenuePicture.class, pictureId);
		if (object != null) {
			VenuePicture venuePicture = (VenuePicture) object;
			getBaseManager().delete(VenuePicture.class, pictureId);
			
			String currentVenueId = UserUtil.getCurrentVenueId(request);
			String picturesPath = VenueUtil.getVenuePicturePath(currentVenueId, request);
			String pictureRealName = venuePicture.getPictureRealName();
			File file = new File(picturesPath + "/" + pictureRealName);
			if (file.exists()) {
				file.delete();
			}
			
			// 删除缩略图
			List<Integer[]> thumbnailsSizes = VenuePictureUtil.getThumbnailsSizes();
			for (Integer[] sizes : thumbnailsSizes) {
				File tempThumFile = new File(picturesPath + "/thumbnails/" + sizes[0] + "/" + pictureRealName);
				if (tempThumFile.exists()) {
					FileUtils.deleteQuietly(tempThumFile);
				}
			}

			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			String strVenuePicture = venueManager.getVenuePicture(venueLongId);
			if (strVenuePicture.equals(pictureRealName)) {
				venueManager.setVenuePicture(venueLongId, null);
			}
			
			print("success", response);
		}
		return null;
	}
	
	/**
	 * 设置场馆主图片
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setMajorPicture(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long venueId = UserUtil.getCurrentVenueLongId(request);
		String pictureRealName = request.getParameter("pictureRealName");
		try {
			venueManager.setVenuePicture(venueId, pictureRealName);
			VenueInfo currentVenueInfo = UserUtil.getCurrentVenueInfo(request);
			currentVenueInfo.setPhotoUrl(pictureRealName);
			print("success", response);
		} catch (Exception e) {
			log.error("设置场馆主图片：venueId=" + venueId + ",pictureRealName=" + pictureRealName, e);
		}
		return null;
	}
	
	/**
	 * 更新图片信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updatePictureInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strId = request.getParameter("id");
		String type = request.getParameter("type");
		Long pictureId = new Long(strId);
		try {
			Object object = getBaseManager().get(VenuePicture.class, pictureId);
			if (object != null) {
				VenuePicture picture = (VenuePicture) object;
				String newInfoValue = request.getParameter("value");
				if ("name".equals(type)) {
					picture.setPictureName(newInfoValue);
				} else if ("remark".equals(type)) {
					picture.setPictureRemark(newInfoValue);
				}
				getBaseManager().update(picture);
			}
			print("success", response);
		} catch (Exception e) {
			log.error("更新图片信息：pictureId=" + strId, e);
		}
		return null;
	}
	
}
