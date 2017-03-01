package com.jeeframework.webframework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

/**
 * DataResponse
 * 数据返回对象
 *
 * @author lance
 * @date 2016/3/21 0021
 */
@ApiModel(value = "SuccessResponse")
public class DataResponse {

    @ApiModelProperty(value = "返回的系统编码 0 正常 其他失败  >20000 系统级异常", required = true)
    private int code = 0;

    @ApiModelProperty(value = "错误提示信息", required = true)
    private String message = "";

    @ApiModelProperty(value = "返回的数据结果", required = true)
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 获取该对象的json数据结构
     *
     * @return
     */
    public String dataResponseToJsonStr() {

        String dataResponseJsonStr = "";
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            dataResponseJsonStr = objectMapper.writeValueAsString(this);
            LoggerUtil.debugTrace("Change Object to JSON String: " + dataResponseJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResponseJsonStr;
    }
}
