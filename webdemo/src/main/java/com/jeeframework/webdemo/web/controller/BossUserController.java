package com.jeeframework.webdemo.web.controller;

import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.common.logging.LoggerUtil;
import com.jeeframework.util.cookie.CookieHelper;
import com.jeeframework.util.encrypt.BASE64Util;
import com.jeeframework.util.encrypt.MD5Util;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webdemo.biz.service.BossUserService;
import com.jeeframework.webdemo.biz.service.UserService;
import com.jeeframework.webdemo.constant.Constants;
import com.jeeframework.webdemo.integration.bo.BossUser;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller("bossUserController")
@Api(value = "用户登录注册，资料设置", description = "用户相关的访问接口", position = 1)
public class BossUserController {

    @Resource
    private BossUserService bossUserService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login.do")
    @ResponseBody
    @ApiOperation(value = "后台用户登录接口", position = 0)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
//        @RequestParam("mobile")
        String userName = req.getParameter("userName");
        String passwd = req.getParameter("password");


        try {

            if (Validate.isEmpty(userName)) {
                throw new BizException("请输入用户名");
            }
            if (Validate.isEmpty(passwd)) {
                throw new BizException("请输入密码");
            }

            userName = userName.trim();

            BossUser bossUserFilter = new BossUser();
            bossUserFilter.setUserName(userName);
            bossUserFilter.setPasswd(passwd);

            BossUser bossUser = bossUserService.getBossUserByPasswd(bossUserFilter);

            if (bossUser == null) {
                throw new BizException("用户名密码不对");
            }

            String remember = req.getParameter("remember");
            if (!Validate.isEmpty(remember)) {
                StringBuffer cookieLogin = new StringBuffer();
                cookieLogin.append(bossUser.getUid());

                long validTime = System.currentTimeMillis() + (Constants.COOKIE_MAX_AGE * 1000);
                // MD5加密用户详细信息
                String cookieValueWithMd5 = MD5Util.encrypt(bossUser.getUid() + ":" + bossUser.getPasswd() + ":" + validTime + ":" + Constants.LOGIN_KEY);
                // 将要被保存的完整的Cookie值
                String cookieValue = bossUser.getUid() + ":" + validTime + ":" + cookieValueWithMd5;
                // 再一次对Cookie的值进行BASE64编码
                String cookieValueBase64 = new String(BASE64Util.encode(cookieValue.getBytes()));
                // 是自动登录则设置cookie
                CookieHelper.setCookie(res, Constants.LOGIN_COOKIE_SIGN, cookieValueBase64, null, "/", Constants.COOKIE_ONE_YEAR_AGE); // 设置了自动登录，cookie在客户端保存2年
            }
            req.getSession().setMaxInactiveInterval(2 * 3600); // Session保存两小时
            req.getSession().setAttribute(Constants.WITH_SESSION_USER, bossUser);

            //            String randomString = StringUtils.getRandomString(6);
            //
            //            Map<String, String> result = new HashMap<String, String>();
            //            result.put("checkCode", randomString);
            //            SessionUtils.putCheckCode(mobile, randomString);

            req.setAttribute("user", bossUser);

            return new ModelAndView("redirect:/dashboard.do");

        } catch (BizException e) {
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("code", String.valueOf(e.getErrorCode()));
            String errorMessage = e.getErrorMessage();
            retMap.put("message", errorMessage);
            LoggerUtil.errorInFileTrace("bizError", errorMessage, e);

            req.setAttribute("error", retMap);
        } catch (Throwable e) {
            Map<String, Object> retMap = new HashMap<String, Object>();
            LoggerUtil.errorInFileTrace("sysError", e.toString(), e);
            LoggerUtil.errorTrace("user_validateMobile", e.getMessage(), e);
            retMap.put("code", String.valueOf(BaseException.DEFAULT_SYS_ERROR_CODE));
            retMap.put("message", "系统错误，请联系管理员！");

            req.setAttribute("error", retMap);
        }
        return new ModelAndView("user/login");
    }

    @RequestMapping(value = "/dashboard.do")
    @ResponseBody
    @ApiOperation(value = "后台用户综合访问界面", position = 1)
    public ModelAndView dashboard(HttpServletRequest req, HttpServletResponse res) {

        try {
            Map<String, Object> retMap = new HashMap<String, Object>();

            long userCount = userService.getUserCount(null);

            retMap.put("userCount", userCount);

            req.setAttribute("result", retMap);

        } catch (BizException e) {
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("code", String.valueOf(e.getErrorCode()));
            String errorMessage = e.getErrorMessage();
            retMap.put("message", errorMessage);
            LoggerUtil.errorInFileTrace("bizError", errorMessage, e);

            req.setAttribute("error", retMap);
        } catch (Throwable e) {
            Map<String, Object> retMap = new HashMap<String, Object>();
            LoggerUtil.errorInFileTrace("sysError", e.toString(), e);
            LoggerUtil.errorTrace("user_validateMobile", e.getMessage(), e);
            retMap.put("code", String.valueOf(BaseException.DEFAULT_SYS_ERROR_CODE));
            retMap.put("message", "系统错误，请联系管理员！");

            req.setAttribute("error", retMap);
        }
        return new ModelAndView("user/dashboard");
    }

    @RequestMapping(value = "/logout.do")
    @ResponseBody
    @ApiOperation(value = "后台用户退出", position = 2)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {

        CookieHelper.setCookie(res, Constants.LOGIN_COOKIE_SIGN, "", null, "/", 0);

        req.getSession().removeAttribute(Constants.WITH_SESSION_USER);

        req.getSession().invalidate();

        return new ModelAndView("user/login");
    }

}
