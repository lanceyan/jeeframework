/**
 * @project: Jeeframework
 * @Title: MySwaggerConfig.java
 * @Package: com.jeeframework.webframework.controller
 * <p>
 * Copyright (c) 2014-2017 Jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webframework.controller;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.ResponseMessage;
import com.mangofactory.swagger.models.dto.builder.ResponseMessageBuilder;
import com.mangofactory.swagger.ordering.ResourceListingPositionalOrdering;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

/**
 * 文档生成的配置
 * <p>
 *
 * @author lance
 * @version 1.0 2015-2-25 下午05:36:40
 * @Description: 配置文档页面显示的内容、工程名、工程URL、作者，在子类里配置 @Configuration  @EnableSwagger 生效文档生成器
 */

public abstract class AbstractSwaggerConfig {

    private SpringSwaggerConfig springSwaggerConfig;

    /**
     * Required to autowire SpringSwaggerConfig
     */
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    /**
     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc framework - allowing for multiple swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {

//        ModelRef errorModel = new ModelRef("RestApiExceptionModel");

        List<ResponseMessage> responseMessages = Arrays.asList(new ResponseMessageBuilder().code(200).message("http协议status为200时才会返回结果数据， <br> <br><b>调用正常</b>返回为 {\"code\":0, \"data\":{}}， <br><b>调用出错</b>返回为 {\"code\":9, \"message\":\"错误消息\"} ").build());
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages).apiListingReferenceOrdering(new ResourceListingPositionalOrdering()).apiDescriptionOrdering(new ApiOperationOrder())
                .apiInfo(apiInfo()).includePatterns(".*?");
    }

    protected abstract ApiInfo apiInfo();
}
