package com.jeeframework.webdemo.web.controller;

import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.common.logging.LoggerUtil;
import com.jeeframework.util.format.DateFormat;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webdemo.biz.service.BossUserService;
import com.jeeframework.webdemo.biz.service.UserService;
import com.jeeframework.webdemo.integration.bo.User;
import com.jeeframework.webdemo.web.filter.UserFilter;
import com.jeeframework.webdemo.web.po.JQGridResponse;
import com.jeeframework.webdemo.web.po.UserPO;
import com.jeeframework.webdemo.util.WebUtil;
import com.jeeframework.webdemo.web.form.UserQueryRequest;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller("userController")
@Api(value = "用户登录注册，资料设置", description = "用户相关的访问接口", position = 1)
public class UserController {

    @Resource
    private BossUserService bossUserService;
    @Resource
    private UserService userService;


    @RequestMapping(value = "/userList.do")
    @ResponseBody
    @ApiOperation(value = "用户列表查询界面", position = 0)
    public ModelAndView userList(HttpServletRequest req, HttpServletResponse res) {

        try {
            Map<String, Object> retMap = new HashMap<String, Object>();


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
        return new ModelAndView("user/userList");
    }


    @RequestMapping(value = "/userListAjax.do")
    @ResponseBody
    @ApiOperation(value = "用户列表查询界面", position = 1)
    public Map<String, Object> userListAjax(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest req, HttpServletResponse res) {

        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            JQGridResponse response = new JQGridResponse();
//            page ： 当前页
//            rows ： 每页记录数
//            sidx ： sort index，排序字段名
//            sord ： sort direction，排序方式：asc， desc


            int currentPage = userQueryRequest.getPage();
            int rows = userQueryRequest.getRows();
            String sortColumnName = userQueryRequest.getSidx();
            String sortDirect = userQueryRequest.getSord();

            UserFilter userFilter = new UserFilter();

            String nickName = userQueryRequest.getNickName();

            if (!Validate.isEmpty(nickName)) {
                userFilter.setNickName(nickName.trim());
            }

            String createTimeTmp = userQueryRequest.getCreateTime();

            if (!Validate.isEmpty(createTimeTmp)) {

                Date createTimeDate = DateFormat.parseDate(createTimeTmp, DateFormat.DT_YYYY_MM_DD);
                userFilter.setGreatCreateTime(DateFormat.formatDate(createTimeDate, DateFormat.DT_YYYY_MM_DD_HHMMSS));

                Calendar cal = Calendar.getInstance();
                cal.setTime(createTimeDate);
                cal.add(Calendar.DATE, 1);

                userFilter.setLessCreateTime(DateFormat.formatDate(cal.getTime(), DateFormat.DT_YYYY_MM_DD_HHMMSS));
            }


            if (!Validate.isEmpty(sortColumnName)) {
                userFilter.setOrderBy(sortColumnName);

                if (!Validate.isEmpty(sortDirect)) {
                    userFilter.setOrderDirect(sortDirect);
                }
            }

            long rowCount = userService.getUserCount(userFilter);

            //设置分页参数
            int startRow = (currentPage - 1) * rows;
            userFilter.setStartRow(startRow);
            userFilter.setPageSize(rows);
            List<User> userList = userService.getUsers(userFilter);

            List<UserPO> userPOList = new ArrayList<UserPO>();
            for (User user : userList) {
                UserPO userPO = new UserPO();

                BeanUtils.copyProperties(userPO, user);

                String sexTmp = "未知";
                int sexInt = user.getSex();
                if (sexInt == User.SEX_MALE) {
                    sexTmp = "男";
                } else if (sexInt == User.SEX_FEMALE) {
                    sexTmp = "女";
                }

                userPO.setAvatar(WebUtil.getUserAvatar(user.getAvatar(), 120));
                userPO.setCreateTime(DateFormat.formatDate(user.getCreateTime(), DateFormat.DT_YYYY_MM_DD));
                userPO.setSex(sexTmp);
                userPOList.add(userPO);
            }

            int totalPage = (int) (rowCount + rows - 1) / rows; //total page
            response.setTotal(totalPage);
            int curPage = Math.min(totalPage, currentPage); //current page
            response.setPage(curPage);

            ArrayList<Object> objects = new ArrayList<Object>();
            objects.addAll(userPOList);
            response.setRecords(objects.size());
            response.setRows(objects);
            retMap.put("code", 0);
            retMap.put("result", response);

//            retMap.put("total", totalPage);
//            retMap.put("page", curPage);
//            retMap.put("records", objects.size());
//            retMap.put("rows", objects);

            return retMap;

        } catch (BizException e) {

            retMap.put("code", String.valueOf(e.getErrorCode()));
            String errorMessage = e.getErrorMessage();
            retMap.put("message", errorMessage);
            LoggerUtil.errorInFileTrace("bizError", errorMessage, e);

            req.setAttribute("error", retMap);
        } catch (Throwable e) {
            LoggerUtil.errorInFileTrace("sysError", e.toString(), e);
            LoggerUtil.errorTrace("user_userListAjax", e.getMessage(), e);
            retMap.put("code", String.valueOf(BaseException.DEFAULT_SYS_ERROR_CODE));
            retMap.put("message", "系统错误，请联系管理员！");

            req.setAttribute("error", retMap);
        }
        return retMap;
    }


}
