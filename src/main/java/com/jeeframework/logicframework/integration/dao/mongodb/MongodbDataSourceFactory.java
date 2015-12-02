package com.jeeframework.logicframework.integration.dao.mongodb;

import com.mongodb.DB;



public class MongodbDataSourceFactory extends AbstractMongoRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		// return DataSourceChoicer.getCurrentDataSource();
		if (MongodbDataSourceChoicer.getCurrentDataSource() != null) {
			// DataSourceChoicer.getCurrentDataSource() + " "+
			// DataSourceRouterManager.getResource(DataSourceChoicer.getCurrentDataSource()));
			// return
			// DataSourceRouterManager.getResource(DataSourceChoicer.getCurrentDataSource());
			return MongodbDataSourceChoicer.getCurrentDataSource();
		}
		return null;
	}
	
	@Override
	public DB getDB() {
		
		MongodbDataSource dataSource = (MongodbDataSource)determineTargetDataSource();
		
		return dataSource.getDB();
		
	}

}
