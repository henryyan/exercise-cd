package net.yanhl.venue.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mlink.esms.SmsUtil;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.sf.json.util.PropertyFilter;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.exception.EmptyValueException;
import net.yanhl.field.service.FieldManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.mail.util.MailUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.exception.VenueException;
import net.yanhl.venue.pojo.BusinessInfo;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;
import net.yanhl.venue.service.VenueManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b>场馆Action</p>
 * <p><b>Description：</b>场馆用户的新增、修改以及报表</p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090624
 */
public class VenueAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected VenueManager venueManager;
	
	@Resource
	protected FieldManager fieldManager;
	
	@Resource
	protected MailUtil mailUtil;

	/**
	 * 检查场馆名称是否已存在
	 * 通过场馆名称查找，然后按照
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkVenueNameExist(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String venueName = StringUtil.getParameter("venueName", request);
		try {
			JSONObject result = new JSONObject();
			VenueInfo venueInfo = venueManager.getVenueByName(venueName);
			if(venueInfo == null) {
				result.accumulate("exist", false);
			} else {
				result.accumulate("exist", true);
				result.accumulate("infoId", venueInfo.getId());
			}
			print(result.toString(), response);
		} catch (EmptyValueException e) {
			log.error(e.getMessage(), e);
			print(e.getMessage(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return null;
	}

	/**
	 * 检查用户名是否已存在
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkVenueUserNameExist(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String venueUserName = StringUtil.getParameter("userName", request);
		try {
			boolean userExist = venueManager.checkUserExist(venueUserName);
			print(String.valueOf(userExist), response);
		} catch (VenueException e) {
			log.error(e.getMessage(), e);
			print(e.getMessage(), response);
		} catch (EmptyValueException e) {
			log.error(e.getMessage(), e);
			print(e.getMessage(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return null;
	}

	/**
	 * 注册新场馆管理员
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward registe(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userName = request.getParameter("userName");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		try {
			if (StringUtil.isEmpty(new String[] { userName, password1, password2 })) {
				String errorMsg = "用户注册--所有必填信息不能为空！";
				log.warn("用户注册，userName=" + userName + ",错误：" + errorMsg);
				request.setAttribute(RESPONSE_ERROR, errorMsg);
				throw new Exception(errorMsg);
			} else if (!password1.equals(password2)) {
				String errorMsg = "用户注册--密码不相同，请重新输入！";
				log.warn("用户注册，userName=" + userName + ",错误：" + errorMsg);
				request.setAttribute(RESPONSE_ERROR, errorMsg);
				throw new Exception(errorMsg);
			} else {
				VenueUser user = new VenueUser();
				user.setUsername(userName);
				user.setPassword(password1);
				getBaseManager().save(user);
				UserUtil.saveUser2Session(user, request);
				printSuccess(response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return null;
	}

	/**
	 * 用户登录
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public ActionForward login(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String name = StringUtil.getParameter("loginName", request);
		String pass = StringUtil.getParameter("password", request);
		try {
			VenueUser user = venueManager.getUserByNameAndPass(name, pass);
			if (user != null) {
				UserUtil.saveUser2Session(user, request);
				log.info("用户<" + user.getUsername() + ",ip=" + request.getRemoteAddr() + ">登录系统");

				// 判断页面走向：1、首页	2、场馆向导	3、场地设置向导
				VenueInfo venueInfo = user.getVenueInfo();
				Long countAllFields = fieldManager.countAllFields(venueInfo.getId());
				if(venueInfo == null) {
					print(UserUtil.FORWARD_VENUE, response);
				} else if(venueInfo.getVerification() == null
						|| venueInfo.getVerification() == false) {
					print(UserUtil.FORWARD_VERIFY, response);
				} else if(countAllFields == 0) {
					print(UserUtil.FORWARD_FIELD, response);
				} else {
					printSuccess(response);
				}
			} else {
				printErrorLabel(response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 用户退出
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			UserUtil.logoutUser(request);
			if (isAjaxRequest(request)) {
				printSuccess(response);
				return null;
			} else {
				return mapping.findForward("login");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 修改密码
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward changePwd(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			String userPwd = venueUser.getPassword();
			String oldPwd = jsonObject.getString("oldPwd");
			String newPwd1 = jsonObject.getString("newPwd1");
			String newPwd2 = jsonObject.getString("newPwd2");


			// 用户输入的是不是正确的密码
			if(userPwd.equals(oldPwd)) {
				if(newPwd1.equals(newPwd2)) {
					venueUser.setPassword(newPwd1);
					getBaseManager().update(venueUser);
					log.info("用户" + venueUser.getUsername() + "修改密码成功");
					printSuccess(response);
				} else {
					print("再次输入的密码不一致", response);
					log.info("用户" + venueUser.getUsername() + "再次输入的密码不一致");
					return null;
				}
			} else {
				print("原始密码不正确", response);
				log.info("用户" + venueUser.getUsername() + "原始密码不正确");
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 保存、更新场馆信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveOrUpdateVenue(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			boolean isNew = false;
			VenueInfo venueInfo = (VenueInfo) JSONObject.toBean(jsonObject, VenueInfo.class);
			if(venueInfo.getId() == null || venueInfo.getId() == 0) {
				venueInfo.setId(null);
				isNew = true;
			}

			// 设置开馆、闭馆时间
			if(!StringUtils.isEmpty(jsonObject.getString("openTimeHour"))
					&& !StringUtils.isEmpty(jsonObject.getString("openTimeMinute"))) {
				String openTime = jsonObject.getString("openTimeHour") + ":" + jsonObject.getString("openTimeMinute");
				venueInfo.setOpenTime(openTime);
			}

			if(!StringUtils.isEmpty(jsonObject.getString("closeTimeHour"))
					&& !StringUtils.isEmpty(jsonObject.getString("closeTimeMinute"))) {
				String closeTime = jsonObject.getString("closeTimeHour") + ":" + jsonObject.getString("closeTimeMinute");
				venueInfo.setCloseTime(closeTime);
			}
			if(isNew){
				// 生成验证码并设置验证状态为未验证
				String authenticode = FieldUtil.generateCode();
				venueInfo.setAuthenticode(authenticode);
				venueInfo.setVerification(false);
			}

			getBaseManager().insertOrUpdate(venueInfo);

			// 更新后重新绑定给场馆管理员对象
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			venueUser.setVenueInfo(venueInfo);

			if(isNew) {
				//新建用户需要更新绑定后的信息到数据库
				getBaseManager().update(venueUser);
				//发邮件通知客服开通
				String text = new StringBuffer()
					.append("新用户(ID:")
					.append(venueUser.getId())
					.append(" Name:")
					.append(venueUser.getUsername())
					.append(")注册了新场馆(ID:")
					.append(venueInfo.getId())
					.append(" Name:")
					.append(venueInfo.getVenueName())
					.append("),请尽快核实信息，并与客户取得联系！")
					.append("验证码是：")
					.append(venueInfo.getAuthenticode())
					.toString(); // save the text into the configuration file

				mailUtil.send("service@remote-click.com", 
						venueInfo.getEmail(), 
						"info@remote-click.com", 
						"新场馆注册-" + venueInfo.getVenueName(), text);
				// TODO save the text into the configuration file
			}
			print(RESPONSE_SUCCESS + "|" + venueInfo.getId(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 加载一个场馆信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadVenue(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			if(venueUser == null) {
				printErrorLabel(response);
			} else {

				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateToStringProcessorImpl());
				config.setExcludes(new String[] {"businessInfo", "venueInfoFieldTypeLinks"});

				VenueInfo venueInfo = venueUser.getVenueInfo();
				if(venueInfo == null) {
					venueUser = (VenueUser) getBaseManager().get(VenueUser.class, venueUser.getId());
					if(venueInfo == null) {
						print(JSONObject.fromObject(new VenueInfo(), config).toString(), response);
						return null;
					}
				}

				long longId = venueInfo.getId();

				Object object = getBaseManager().get(VenueInfo.class, longId);
				JSONObject fromObject = JSONObject.fromObject(object, config);
				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 保存场馆商务信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveBusinessInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			BusinessInfo info = (BusinessInfo) JSONObject.toBean(jsonObject, BusinessInfo.class);

			if(info.getId() != null) {
				BusinessInfo tinfo = (BusinessInfo) getBaseManager().get(BusinessInfo.class, info.getId());
				tinfo.setBank(info.getBank());
				tinfo.setBankAccount(info.getBankAccount());
				tinfo.setName(info.getName());
				tinfo.setOwner(info.getOwner());
				tinfo.setOwnerMobile(info.getOwnerMobile());
				info = tinfo;
			} else {
				info.setVenueInfo(UserUtil.getUserFromSession(request).getVenueInfo());
			}

			getBaseManager().insertOrUpdate(info);
			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 找回密码
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getBackPassword(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			String userName = jsonObject.getString("userName");
			if(StringUtils.isEmpty(userName)) {
				print("用户名不能为空", response);
			}

			List<VenueUser> users = getBaseManager().findBy(VenueUser.class, "username", userName);
			if(users.size() > 0) {
				VenueUser user = users.get(0);
				VenueInfo venueInfo = user.getVenueInfo();
				SmsUtil.sendSingleSms(venueInfo.getCell(), "你的密码为:" + user.getPassword());
				printSuccess(response);
			} else {
				print("没有此用户", response);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 加载一个场馆商务信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadBusinessInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			if(venueUser == null) {
				printErrorLabel(response);
			} else {
				if(venueUser.getVenueInfo() == null) {
					print("", response);
					return null;
				}

				BusinessInfo businessInfo = venueManager.loadBusinessInfo(venueUser.getVenueInfo().getId());

				if(businessInfo == null) {
					print("", response);
					return null;
				}

				JsonConfig config = new JsonConfig();
				config.setJsonPropertyFilter(new PropertyFilter(){
					public boolean apply(Object source, String name, Object value) {
						if(name.equals("venueInfo") || name.equals("fieldActivities")) {
							return true;
						} else {
							return false;
						}
					}

				});
				JSONObject fromObject = JSONObject.fromObject(businessInfo, config);
				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}
	
}
