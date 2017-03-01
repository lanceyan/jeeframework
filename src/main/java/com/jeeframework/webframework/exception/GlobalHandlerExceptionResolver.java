package com.jeeframework.webframework.exception;


import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.controller.DataResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * GlobalHandlerExceptionResolver
 * 定义全局异常
 *
 * @author lance
 * @date 2016/3/22 0021
 */
@Component
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private SystemCode systemCode;

    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {

//        String methodName = ((HandlerMethod) handler).getMethod().getName();
//        LoggerUtil.debugTrace("Exception method name: " + methodName);

        resp.setCharacterEncoding("UTF-8"); // 避免乱码

        String uri = req.getRequestURI();
        String lowerCaseUri = uri.toLowerCase();

        String acceptHeader = req.getHeader("accept");
        if (!((!Validate.isEmpty(acceptHeader) && acceptHeader.indexOf("application/json") > -1) || (req
                .getHeader("X-Requested-With") != null && req
                .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1) || (lowerCaseUri.endsWith(".json") || lowerCaseUri.contains("ajax.")))) {
            // 如果不是异步请求
            ModelAndView mv = new ModelAndView("error");
            if (ex instanceof WebException) {
                mv.addObject("webError", ex);
            } else {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                mv.addObject("stackTrace", sw.toString());
            }
            return mv;
        } else {// JSON格式返回
            try {
                DataResponse dataResponse = new DataResponse();
                if (ex instanceof WebException) {//系统业务异常  >>>>>>>   只将该级别的异常进行返回显示处理
                    dataResponse.setCode(((WebException) ex).getErrorCode());
                    dataResponse.setMessage(systemCode.getSystemCodeMessageByCode((((WebException) ex).getErrorCode())));
                } else {//未处理的异常
                    dataResponse.setCode(SystemCode.SYS_CONTROLLER_EXCEPTION);
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    dataResponse.setMessage(SystemCode.getSystemCodeMessageByCode(SystemCode.SYS_CONTROLLER_EXCEPTION) + "  " + sw.toString());
                    pw.close();
                    sw.close();
                }

                resp.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType

                ModelAndView mav = new ModelAndView();
                mav.setView(new MappingJackson2JsonView());
                mav.addObject("code", dataResponse.getCode());
                mav.addObject("message", dataResponse.getMessage());
//                PrintWriter writer = resp.getWriter();
//                writer.write(dataResponse.dataResponseToJsonStr());
//                writer.flush();
//                writer.close();
//                jm.writeValue(resp.getOutputStream(), dataResponse);
                return mav;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    public void setSystemCode(SystemCode systemCode) {
        this.systemCode = systemCode;
    }
}
