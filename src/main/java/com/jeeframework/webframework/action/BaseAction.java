package com.jeeframework.webframework.action;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.logicframework.integration.error.ErrorInfo;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.exception.FieldValidateException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Map;

/**
 * web层的action基类。 开发Action必须继承这个类。
 * <p>
 * 类里封装了获取HttpSession、HttpServletRequest、HttpServletResponse log记录的函数
 * 
 * @author lanceyan（最新修改者）
 * @version 1.0（新版本号）
 * @see BaseAction
 */

public abstract class BaseAction extends ActionSupport {
	private HttpServletRequest httpServletRequest = null;

	private HttpServletResponse httpServletResponse = null;

	private HttpSession httpSession = null;

	private Map mapSession = null; // 取得struts的sessionMap

	private long invokeBeginTime = 0; // 方法调用开始时间

	private long invokeEndTime = 0; // 方法调用结束时间

	private BeanFactory context = null;

	public static final String ERRORMESSAGE = "errorMessage";
	public static final String SYSTEMERROR = "系统出错，请联系管理员";

	public static final String ERRORJSON = "errorJSON";
	/**
	 * 返回到成功页面，用法为 return BaseAction.SUCCESS
	 */
	public static final String SUCCESS = "success";

	/**
	 * 返回到空页面，用法为 return BaseAction.NONE
	 */
	public static final String NONE = "none";

	/**
	 * 返回到错误页面，用法为 return BaseAction.ERROR
	 */
	public static final String ERROR = "error";

	/**
	 * 返回到输入页面，用法为 return BaseAction.INPUT
	 */
	public static final String INPUT = "input";

	/**
	 * 返回到登陆页面，用法为 return BaseAction.LOGIN
	 */
	public static final String LOGIN = "login";

	/**
	 * 获取HttpServletRequest对象
	 * 
	 * @return 返回一个HttpServletRequest对象
	 * @see HttpServletRequest
	 */
	protected HttpServletRequest getRequest() {
		if (httpServletRequest != null) {
			return httpServletRequest;
		}

		return ServletActionContext.getRequest();

	}

	/**
	 * 得到beanfactory的context对象
	 * 
	 * @return
	 */
	public BeanFactory getContext() {
		return WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
	}

	/**
	 * 获取HttpServletResponse对象
	 * 
	 * @return 返回一个HttpServletResponse对象
	 * @see HttpServletResponse
	 */
	protected HttpServletResponse getResponse() {
		if (httpServletResponse != null) {
			return httpServletResponse;
		}

		return ServletActionContext.getResponse();

	}

	/**
	 * 获取HttpSession对象
	 * 
	 * @return 返回一个HttpSession对象
	 * @see HttpSession
	 */
	protected HttpSession getSession() {
		if (httpSession != null) {
			return httpSession;
		}

		return getRequest().getSession();

	}

	/**
	 * 将请求中得参数映射为Map（key、value）对
	 * 
	 * @return 返回一个Map对象
	 * @see Map
	 */
	protected Map getMapSession() {
		if (mapSession != null) {
			return mapSession;
		}

		ActionContext ctx = ActionContext.getContext();
		return ctx.getSession();

	}

	/**
	 * 设置基类的HttpServletRequest对象。
	 * 
	 * @param httpServletRequest
	 *            用户自定义的httpServletRequest封装
	 * @see HttpServletRequest
	 */
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	/**
	 * 设置基类的HttpServletResponse对象。
	 * 
	 * @param httpServletResponse
	 *            用户自定义的httpServletResponse封装
	 * @see HttpServletResponse
	 */
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	/**
	 * 设置基类的HttpSession对象。
	 * 
	 * @param httpSession
	 *            用户自定义的httpSession封装
	 * @see HttpSession
	 */
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	/**
	 * 设置基类的mapSession对象。
	 * 
	 * @param mapSession
	 *            用户自定义的mapSession封装
	 * @see Map
	 */
	public void setMapSession(Map mapSession) {
		this.mapSession = mapSession;
	}

	/**
	 * 抽象类，子类实现获取当前子类实例的个数。
	 * 
	 * 之所以是抽象方法，是因为要用于在子类实现，获取具体子类的个数。
	 * 
	 * @return 返回当前实现类的具体的个数。
	 */
	public abstract int getObjCount();

	/**
	 * 获取struts的ActionContext。 为了使代码与struts解耦，不建议使用。
	 * 
	 * @return ActionContext
	 * @see ActionContext
	 */
	public ActionContext getStrutsContext() {
		return ActionContext.getContext();
	}

	/**
	 * 获得ServletContext
	 * 
	 * @return ServletContext
	 * @see ServletContext
	 */
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	/**
	 * 获取struts的ValueStack。 为了使代码与struts解耦，不建议使用。
	 * 
	 * @return ValueStack
	 * @see ValueStack
	 */
	public ValueStack getValueStack(HttpServletRequest req) {
		return ServletActionContext.getValueStack(req);
	}

	/**
	 * 获取struts的ActionMapping。 为了使代码与struts解耦，不建议使用。
	 * 
	 * @return ActionMapping
	 * @see ActionMapping
	 */
	public ActionMapping getActionMapping() {
		return ServletActionContext.getActionMapping();
	}

	/**
	 * 获得PageContext
	 * 
	 * @return PageContext
	 * @see PageContext
	 */
	public PageContext getPageContext() {
		return ServletActionContext.getPageContext();
	}

	/**
	 * 统一定义的错误处理机制方法。 如果errorInfo里有信息则展示errorinfo里的信息。
	 * 没有则从显示配置中心里的默认信息。这样做主要是为了大家格式化消息后传入。
	 * 
	 * @param errorInfo
	 *            后台传递到web层的错误信息
	 * @return ERROR 错误页面
	 */
	public String processError(ErrorInfo errorInfo) {
		if (errorInfo.getErrorCode() == 0) {
			return BaseAction.SUCCESS;
		}
		getStrutsContext().put("errorCode", errorInfo.getErrorCode());

		if (errorInfo.getErrorMessage() != null
				&& !errorInfo.getErrorMessage().trim().equals("")) {
			// 业务开发在程序中设置了错误info 显示程序中的错误信息 因为这个错误信息需要格式化！%s + 参数
			getStrutsContext().put("errorMessage", errorInfo.getErrorMessage());
		} else {
			// // 没有设置，则取默认的错误信息
			// String errorMessage =
			// ErrorInfoConfigAgentUtil.getErrorMessage(errorInfo.getErrorCode());
			// // 从缓存中取错误消息，如果message不为空直接显示
			// if (errorMessage != null && !errorMessage.trim().equals("")) {
			// // 如果errorMessage不为空则直接显示出来
			// getStrutsContext().put("errorMessage", errorMessage);
			// }
		}

		return BaseAction.ERROR;
	}



	public void validateFieldEmptyEx(String field, String message) {
		if (Validate.isEmpty(field)) {
			throw new FieldValidateException(message);
		}
	}

	public boolean validateFieldEmpty(String field, String message) {
		if (Validate.isEmpty(field)) {
			getRequest().setAttribute("errorMessage", message);
			return true;
		}
		return false;
	}

	public boolean validateObjectNull(Object object, String message) {
		if (object == null) {
			getRequest().setAttribute("errorMessage", message);
			return true;
		}
		return false;
	}

	public boolean validateFieldNumeric(String field, String message) {
		if (!Validate.isNumeric(field)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldDate(String field, String message) {
		if (!Validate.isDate(field, null)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldInRange(String field, int min, int max,
			String message) {
		if (!Validate.isNumeric(field)) {
			getRequest().setAttribute("errorMessage", "当前输入不是数字");
			return false;
		}

		Integer iField = Integer.valueOf(field);

		if (!Validate.isInRange(iField, min, max)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldEmail(String field, String message) {
		if (!Validate.isEmail(field)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldExpression(String field, String expression,
			String message) {
		if (!Validate.validExpression(field, expression)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldUrl(String field, String message) {
		if (!Validate.isUrl(field)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldByteLength(String field, int maxLen,
			String message) {
		if (!Validate.validateByteLength(field, maxLen)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldMaxLength(String field, int maxLen,
			String message) {
		if (!Validate.maxLength(field, maxLen)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldMinValue(String field, int min, String message) {
		if (!Validate.isNumeric(field)) {
			getRequest().setAttribute("errorMessage", "当前输入不是数字");
			return false;
		}

		Integer iField = Integer.valueOf(field);

		if (!Validate.minValue(iField, min)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public boolean validateFieldMaxValue(String field, int max, String message) {
		if (!Validate.isNumeric(field)) {
			getRequest().setAttribute("errorMessage", "当前输入不是数字");
			return false;
		}

		Integer iField = Integer.valueOf(field);

		if (!Validate.maxValue(iField, max)) {
			getRequest().setAttribute("errorMessage", message);
			return false;
		}
		return true;
	}

	public String genAjaxErrorStr(String errorCode, String errorDesc) {
		String str = "{\"code\":\"" + errorCode + "\",\"msg\":\"" + errorDesc
				+ "\"}";
		try {
			getResponse().getOutputStream().write(
					str.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			LoggerUtil.errorTrace("[BaseAction][genAjaxErrorStr][String,String]输出流出现异常");
		}
		return this.NONE;
	}

	public String genAjaxDataStr(String errorCode, String data) {
		String str = "{\"code\":\"" + errorCode + "\",\"data\":" + data + "}";
		try {
			getResponse().getOutputStream().write(
					str.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			LoggerUtil.errorTrace("[BaseAction][genAjaxDataStr][String,String]输出流出现异常");
		}
		return this.NONE;
	}

	public String genAjaxErrorStr(int errorCode, String errorDesc) {
		String str = "{\"code\":\"" + errorCode + "\",\"msg\":\"" + errorDesc
				+ "\"}";
		try {
			getResponse().getOutputStream().write(
					str.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			LoggerUtil.errorTrace("[BaseAction][genAjaxErrorStr][int,String]输出流出现异常");
		}
		return this.NONE;
	}

	public String genAjaxDataStr(int errorCode, String data) {
		String str = "{\"code\":\"" + errorCode + "\",\"data\":" + data + "}";
		try {
			getResponse().getOutputStream().write(
					str.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			LoggerUtil.errorTrace("[BaseAction][genAjaxDataStr][int,String]输出流出现异常");
		}
		return this.NONE;
	}

}
