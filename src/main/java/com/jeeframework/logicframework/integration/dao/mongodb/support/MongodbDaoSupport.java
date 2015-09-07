/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeeframework.logicframework.integration.dao.mongodb.support;

import org.springframework.dao.support.DaoSupport;

import com.jeeframework.logicframework.integration.dao.mongodb.DataSource;
import com.jeeframework.logicframework.integration.dao.mongodb.MongodbTemplate;

/**
 * 
 * @author ake
 *
 */
public abstract class MongodbDaoSupport extends DaoSupport {
	
	private MongodbTemplate mongodbTemplate = new MongodbTemplate();

//	private boolean externalTemplate = false;

	public final void setDataSource(DataSource dataSource) {
	  this.mongodbTemplate.setDataSource(dataSource);
	}

	public final DataSource getDataSource() {
		return (this.mongodbTemplate != null ? this.mongodbTemplate.getDataSource() : null);
	}

	public final void setMongodbTemplate(MongodbTemplate mongodbTemplate) {
		if (mongodbTemplate == null) {
			throw new IllegalArgumentException("Cannot set mongodbTemplate to null");
		}
		this.mongodbTemplate = mongodbTemplate;
//		this.externalTemplate = true;
	}

	public final MongodbTemplate getMongodbTemplate() {
	  return mongodbTemplate;
	}

	protected final void checkDaoConfig() {
//		if (!this.externalTemplate) {
//			this.mongodbTemplate.afterPropertiesSet();
//		}
	}

}
