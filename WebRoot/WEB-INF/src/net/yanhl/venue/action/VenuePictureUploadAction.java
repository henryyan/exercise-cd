package net.yanhl.venue.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.yanhl.base.service.BaseManager;
import net.yanhl.upload.UploadFile;
import net.yanhl.upload.form.MultiUploadForm;
import net.yanhl.util.DateUtil;
import net.yanhl.util.FileUtil;
import net.yanhl.venue.pojo.VenuePicture;
import net.yanhl.venue.service.VenueManager;
import net.yanhl.venue.util.VenuePictureUtil;
import net.yanhl.venue.util.VenueUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * 场馆上传Action
 *
 * @author HenryYan
 *
 */
public class VenuePictureUploadAction extends Action {

	Log log = LogFactory.getLog(this.getClass());

	@Resource BaseManager baseManager;
	
	@Resource
	VenueManager venueManager;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		MultiUploadForm multiUploadForm = (MultiUploadForm) form;
		List<UploadFile> myFiles = multiUploadForm.getMyFiles();
		for (int i = 0; i < myFiles.size(); i++) {
			UploadFile uploadFile = (UploadFile) myFiles.get(i);
			FormFile file = uploadFile.getFile();
			if (file == null) {
				System.out.println("file  is  null");
			} else {
				String currentVenueId = request.getParameter("venueId");
				if (StringUtils.isBlank(currentVenueId)) {
					response.setHeader("ContentType", "text/html;charset=UTF-8");
					try {
						PrintWriter out = response.getWriter();
						out.write("登录超时，请重新登录！");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
				
				String picturesPath = VenueUtil.getVenuePicturePath(currentVenueId, request);

				File pictureFilePath = new File(picturesPath);
				if (!pictureFilePath.exists()) {
					pictureFilePath.mkdirs();
				}

				String fileType = FileUtil.getFileType(file.getFileName());
				String realName = DateUtil.getSysdate() + "-" + System.currentTimeMillis() + "." + fileType;
				realName = realName.toLowerCase();
				log.debug("场馆图片上传，由：" + file.getFileName() + "， 改名为：" + realName);

				String fullPathName = picturesPath + "/" + realName;
				FileOutputStream fileOutputStream = null;
				try {
					InputStream inputStream = file.getInputStream();
					fileOutputStream = new FileOutputStream(fullPathName);
					IOUtils.copy(inputStream, fileOutputStream);
					
					/*
					 * 生成缩略图
					 */
					String thumbnailsPath = picturesPath + "/thumbnails";
					File scaleDir = new File(thumbnailsPath);
					if (!scaleDir.exists()) {
						scaleDir.mkdir();
					}
					
					List<Integer[]> thumbnailsSizes = VenuePictureUtil.getThumbnailsSizes();
					for (Integer[] sizes : thumbnailsSizes) {
						// 创建存放不同大小的图片文件夹，以宽度为名称
						String strThumFloder = thumbnailsPath + "/" + sizes[0];
						File thumFloder = new File(strThumFloder);
						if (!thumFloder.exists()) {
							thumFloder.mkdir();
						}

						String thumbnailsFilePath = strThumFloder + "/" + realName;
						Thumbnails.of(new File(fullPathName)).size(sizes[0], sizes[1])
								.outputFormat(fileType)
								.toFile(new File(thumbnailsFilePath));
					}
					
				} catch (FileNotFoundException e) {
					log.error("没有找到文件：" + fullPathName, e);
				} catch (IOException e) {
					log.error("复制场馆图片文件：" + fullPathName, e);
				} finally {
					if (fileOutputStream != null) {
						try {
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				VenuePicture picture = new VenuePicture();
				Long venueId = new Long(currentVenueId);
				picture.setVenueId(venueId);
				picture.setPictureRealName(realName);
				picture.setUploadDate(new Date());
				picture.setPictureSize(new Long(file.getFileSize()));

				String venuePicture = venueManager.getVenuePicture(venueId);
				if (StringUtils.isBlank(venuePicture)) {
					venueManager.setVenuePicture(venueId, realName);
				}
				
				baseManager.save(picture);

			}
		}
		// 返回文本
		try {
			PrintWriter out = response.getWriter();
			out.print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}