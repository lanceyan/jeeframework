package com.jeeframework.logicframework.integration.dao.mongodb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.mongodb.DB;


public abstract class AbstractMongoRoutingDataSource implements InitializingBean,DataSource {

	private Map targetDataSources;

	private Object defaultTargetDataSource;

	private Map resolvedDataSources;

	private DataSource resolvedDefaultDataSource;

	/**
	 * Specify the map of target DataSources, with the lookup key as key. The
	 * mapped value can either be a corresponding {@link javax.sql.DataSource}
	 * instance or a data source name String (to be resolved via a
	 * {@link #setDataSourceLookup DataSourceLookup}).
	 * <p>
	 * The key can be of arbitrary type; this class implements the generic
	 * lookup process only. The concrete key representation will be handled by
	 * {@link #resolveSpecifiedLookupKey(Object)} and
	 * {@link #determineCurrentLookupKey()}.
	 */
	public void setTargetDataSources(Map targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	/**
	 * Specify the default target DataSource, if any. The mapped value can
	 * either be a corresponding {@link javax.sql.DataSource} instance or a data
	 * source name String (to be resolved via a {@link #setDataSourceLookup
	 * DataSourceLookup}).
	 * <p>
	 * This DataSource will be used as target if none of the keyed
	 * {@link #setTargetDataSources targetDataSources} match the
	 * {@link #determineCurrentLookupKey()} current lookup key.
	 */
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		this.defaultTargetDataSource = defaultTargetDataSource;
	}

	public void afterPropertiesSet() {
		if (this.targetDataSources == null) {
			throw new IllegalArgumentException("targetDataSources is required");
		}
		this.resolvedDataSources = new HashMap(this.targetDataSources.size());
		for (Iterator it = this.targetDataSources.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
			DataSource dataSource = resolveSpecifiedDataSource(entry.getValue());
			this.resolvedDataSources.put(lookupKey, dataSource);
		}
		if (this.defaultTargetDataSource != null) {
			this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource);
		}
	}

	/**
	 * Resolve the specified data source object into a DataSource instance.
	 * <p>
	 * The default implementation handles DataSource instances and data source
	 * names (to be resolved via a {@link #setDataSourceLookup DataSourceLookup}
	 * ).
	 * 
	 * @param dataSource
	 *            the data source value object as specified in the
	 *            {@link #setTargetDataSources targetDataSources} map
	 * @return the resolved DataSource (never <code>null</code>)
	 * @throws IllegalArgumentException
	 *             in case of an unsupported value type
	 */
	protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
		if (dataSource instanceof DataSource) {
			return (DataSource) dataSource;
		} else {
			throw new IllegalArgumentException("Illegal data source value - only [com.google.code.morphia.Datastore] and String supported: " + dataSource);
		}
	}
	
	public DB getDB() {
		return determineTargetDataSource().getDB();
	}

	/**
	 * Retrieve the current target DataSource. Determines the
	 * {@link #determineCurrentLookupKey() current lookup key}, performs a
	 * lookup in the {@link #setTargetDataSources targetDataSources} map, falls
	 * back to the specified {@link #setDefaultTargetDataSource default target
	 * DataSource} if necessary.
	 * 
	 * @see #determineCurrentLookupKey()
	 */
	protected DataSource determineTargetDataSource() {
		Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = (DataSource) this.resolvedDataSources.get(lookupKey);
		if (dataSource == null) {
			dataSource = this.resolvedDefaultDataSource;
		}
		if (dataSource == null) {
			throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
		}
		return dataSource;
	}

	/**
	 * Resolve the given lookup key object, as specified in the
	 * {@link #setTargetDataSources targetDataSources} map, into the actual
	 * lookup key to be used for matching with the
	 * {@link #determineCurrentLookupKey() current lookup key}.
	 * <p>
	 * The default implementation simply returns the given key as-is.
	 * 
	 * @param lookupKey
	 *            the lookup key object as specified by the user
	 * @return the lookup key as needed for matching
	 */
	protected Object resolveSpecifiedLookupKey(Object lookupKey) {
		return lookupKey;
	}

	/**
	 * Determine the current lookup key. This will typically be implemented to
	 * check a thread-bound transaction context.
	 * <p>
	 * Allows for arbitrary keys. The returned key needs to match the stored
	 * lookup key type, as resolved by the {@link #resolveSpecifiedLookupKey}
	 * method.
	 */
	protected abstract Object determineCurrentLookupKey();

}
