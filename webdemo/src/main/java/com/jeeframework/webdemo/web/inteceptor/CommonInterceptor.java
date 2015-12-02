/**
 * @project: apptest2
 * @Title: CommonInterceptor.java
 * @Package: com.webdemo.web.inteceptor
 * <p/>
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.web.inteceptor;

/**
 * TODO 简单描述
 * <p/>
 *
 * @Description: TODO 详细描述
 * @author TODO
 * @version 1.0 2015-2-26 上午09:39:15
 */

import com.jeeframework.util.cookie.CookieHelper;
import com.jeeframework.util.encrypt.BASE64Util;
import com.jeeframework.util.encrypt.MD5Util;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webdemo.biz.service.BossUserService;
import com.jeeframework.webdemo.constant.Constants;
import com.jeeframework.webdemo.integration.bo.BossUser;
import com.jeeframework.webdemo.util.WebUtil;
import com.jeeframework.webdemo.web.filter.BossUserFilter;
import org.apache.struts2.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CommonInterceptor implements HandlerInterceptor {

    public CommonInterceptor() {
        // TODO Auto-generated constructor stub  
    }

    @Resource
    private BossUserService bossUserService;

    private String mappingURL;//利用正则映射到需要拦截的路径    

    public void setMappingURL(String mappingURL) {
        this.mappingURL = mappingURL;
    }

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * <p/>
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO Auto-generated method stub  
        String url = request.getRequestURL().toString();
//        if(mappingURL==null || url.matches(mappingURL)){    
//            request.getRequestDispatcher("/msg.jsp").forward(request, response);  
//            return false;   
//        }

        String currentUri = getUri(request);

        HttpSession session = request.getSession();

        BossUser userBO = null;

        try {
            // 获取登录用户，用于显示到页面的头部上
            // boss菜单上的menuId，回写在页面参数里
//            String menuId = (String) request.getParameter("menuId");
//            if (Validate.isEmpty(menuId)) {
//                menuId = CookieHelper.getCookieValue(request, Constants.BOSS_MENU_ID);
//                if (Validate.isEmpty(menuId)) {
//                    menuId = "1";
//                }
//            }
//
//            if (!menuId.equals("1")) {
//                CookieHelper.setCookie(response, Constants.BOSS_MENU_ID, menuId, null, "/", Constants.COOKIE_ONE_YEAR_AGE); // 设置了自动登录，cookie在客户端保存2年
//            }

            request.setAttribute("currentUri", currentUri);

            userBO = (BossUser) session.getAttribute(Constants.WITH_SESSION_USER);
            if (userBO != null) {
//                request.getRequestDispatcher("/dashboard.do").forward(request, response);
                return true;
            }


            String userInfoCookie = CookieHelper.getCookieValue(request, Constants.LOGIN_COOKIE_SIGN);
            if (!Validate.isEmpty(userInfoCookie)) {
                userInfoCookie = new String(BASE64Util.decode(userInfoCookie));
                String[] loginInfo = userInfoCookie.split(":");

                String userId = loginInfo[0];
                String validTime = loginInfo[1];
                String cookieValueWithMd5 = loginInfo[2];

                BossUserFilter bossUserFilter = new BossUserFilter();
                bossUserFilter.setUid(Long.valueOf(userId));
                BossUser userObj = bossUserService.getBossUser(bossUserFilter);

                if (userObj != null) {
                    String compareMd5 = MD5Util.encrypt(userObj.getUid() + ":" + userObj.getPasswd() + ":" + validTime + ":" + Constants.LOGIN_KEY);

                    if (cookieValueWithMd5.equals(compareMd5)) {
                        session.setAttribute(Constants.WITH_SESSION_USER, userObj);
                        //判断是否是访问boss后台，如果是则跳转到boss后台访问页面
//                        if (currentUri.startsWith("/index.xhtml")) {
//                            request.getRequestDispatcher("/dashboard.do").forward(request, response);
//                            return false;
//                        } else {
                        return true;
//                        }
                    }
                }
            }

            request.getRequestDispatcher("/login.do").forward(request, response);
            return false;
        } catch (Exception e) {
            request.setAttribute("errorMsg", e.getMessage());
//            return "ERRORMESSAGE";
            request.getRequestDispatcher("/login.do").forward(request, response);
        } finally {
            if (request != null) {
                // 清除用户的角色信息
                request.setAttribute(Constants.REQUEST_USERBO, userBO);


            }

        }
        return true;
    }

    //在业务处理器处理请求执行完成后,生成视图之前执行的动作   
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        String staticServer = WebUtil.getStaticServerByEnv();

        request.setAttribute("staticServer", staticServer);
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用
     * <p/>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub  
    }

    public static String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.
        String uri = (String) request
                .getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }

        uri = RequestUtils.getServletPath(request);
        if (uri != null && !"".equals(uri)) {
            return uri;
        }

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

} 