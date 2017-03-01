/**
 * @project: jeeframework
 * @Title: ApiOperationOrder.java
 * @Package: com.jeeframework.webframework.controller
 *
 * Copyright (c) 2014-2017 Jeeframework Limited, Inc.
 * All rights reserved.
 *
 */
package com.jeeframework.webframework.controller;

/**
 * 针对API文档生成排序
 * <p>
 * @Description: 使用操作后面的position属性进行排序，按照升序的方式
 * @author lance
 * @version 1.0 2015-3-2 下午10:54:56
 */

import com.google.common.collect.Ordering;
import com.mangofactory.swagger.models.dto.ApiDescription;
//import springfox.documentation.service.ApiDescription;

public class ApiOperationOrder extends Ordering<ApiDescription> {
    @Override
    public int compare(ApiDescription apiDescription, ApiDescription other) {
        return apiDescription.getOperations().get(0).getPosition() - other.getOperations().get(0).getPosition();
    }
}
