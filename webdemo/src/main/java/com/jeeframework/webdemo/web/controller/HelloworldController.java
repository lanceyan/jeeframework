//package com.webdemo.web.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import BossUserService;
//import BossUser;
//import com.wordnik.swagger.annotations.Api;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;
//
//@Controller
//@Api(value = "helloWorld", description = "Greeting API", position = 0)
//public class HelloworldController {
//    @Resource
//    private BossUserService userService;
//
////    @RequestMapping(value = "/helloworld.do", method = RequestMethod.GET)
////    @ApiOperation(value = "你好世界" , position = 0)
////    public ModelAndView doRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        // 向HttpServletRequest中加入一个名为message值为HelloWorld的对象
////        request.setAttribute("message", "顶顶22222顶顶 HelloWorld");
////
////        List<BossUser> userList = userService.searchUser("1");
////
////        System.out.println(userList);
////        // 返回一个ModelAndView对象通过viewResolver的处理
////        // 页面跳转至/WEB-INF/jsp/helloworld.jsp
////        return new ModelAndView("layout/helloworld");
////    }
////
//    @RequestMapping(value = "/validataUser.json", method = RequestMethod.GET)
//    @ResponseBody
////    @ApiOperation(value = "验证用户" )
//    public Map<String, Object> validataUser(@RequestParam String userName) {
//        //            LoggerUtil.infoTrace(" validata user : {}",userName);  
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("code", true);
//        return map;
//    }
//
////    @ResponseBody
////    @RequestMapping(value = "addBossUser", method = RequestMethod.GET)
////    @ApiOperation(value = "添加用户" ,notes = "add user", position = 1)
////    public String addBossUser(@ApiParam(required = true, name = "postData", value = "用户信息json数据") @RequestParam(value = "postData") String postData, HttpServletRequest request) {
////
////        return "";
////    }
//}
