/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ConnectionHolderWrapper.java
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-7-24           Create	
 */

package com.jeeframework.logicframework.integration.dao.datasource;

import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleConnectionHandle;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/** 
 * ����
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class ConnectionHolderWrapper extends ConnectionHolder
{
    public static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";


    private ConnectionHandle connectionHandle;

    private Connection currentConnection;

    private boolean transactionActive = false;

    private Boolean savepointsSupported;

    private int savepointCounter = 0;


    /**
     * Create a new ConnectionHolderWrapper for the given ConnectionHandle.
     * @param connectionHandle the ConnectionHandle to hold
     */
    public ConnectionHolderWrapper(ConnectionHandle connectionHandle) {
        super(connectionHandle);
        Assert.notNull(connectionHandle, "ConnectionHandle must not be null");
        this.connectionHandle = connectionHandle;
    }

    /**
     * Create a new ConnectionHolderWrapper for the given JDBC Connection,
     * wrapping it with a {@link SimpleConnectionHandle},
     * assuming that there is no ongoing transaction.
     * @param connection the JDBC Connection to hold
     * @see SimpleConnectionHandle
     * @see #ConnectionHolderWrapper(java.sql.Connection, boolean)
     */
    public ConnectionHolderWrapper(Connection connection) {
        super(connection);
        this.connectionHandle = new SimpleConnectionHandle(connection);
    }

    /**
     * Create a new ConnectionHolderWrapper for the given JDBC Connection,
     * wrapping it with a {@link SimpleConnectionHandle}.
     * @param connection the JDBC Connection to hold
     * @param transactionActive whether the given Connection is involved
     * in an ongoing transaction
     * @see SimpleConnectionHandle
     */
    public ConnectionHolderWrapper(Connection connection, boolean transactionActive) {
        this(connection);
        this.transactionActive = transactionActive;
    }


    /**
     * Return the ConnectionHandle held by this ConnectionHolderWrapper.
     */
    public ConnectionHandle getConnectionHandle() {
        return this.connectionHandle;
    }

    /**
     * Return whether this holder currently has a Connection.
     */
    protected boolean hasConnection() {
        return (this.connectionHandle != null);
    }

    /**
     * Set whether this holder represents an active, JDBC-managed transaction.
     * @see DataSourceTransactionManager
     */
    protected void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }

    /**
     * Return whether this holder represents an active, JDBC-managed transaction.
     */
    protected boolean isTransactionActive() {
        return this.transactionActive;
    }


    /**
     * Override the existing Connection handle with the given Connection.
     * Reset the handle if given <code>null</code>.
     * <p>Used for releasing the Connection on suspend (with a <code>null</code>
     * argument) and setting a fresh Connection on resume.
     */
    protected void setConnection(Connection connection) {
        if (this.currentConnection != null) {
            this.connectionHandle.releaseConnection(this.currentConnection);
            this.currentConnection = null;
        }
        if (connection != null) {
            this.connectionHandle = new SimpleConnectionHandle(connection);
        }
        else {
            this.connectionHandle = null;
        }
    }

    /**
     * Return the current Connection held by this ConnectionHolderWrapper.
     * <p>This will be the same Connection until <code>released</code>
     * gets called on the ConnectionHolderWrapper, which will reset the
     * held Connection, fetching a new Connection on demand.
     * @see ConnectionHandle#getConnection()
     * @see #released()
     */
    public Connection getConnection() {
        Assert.notNull(this.connectionHandle, "Active Connection is required");
        if (this.currentConnection == null) {
            this.currentConnection = this.connectionHandle.getConnection();
        }
        return this.currentConnection;
    }

    /**
     * Return whether JDBC 3.0 Savepoints are supported.
     * Caches the flag for the lifetime of this ConnectionHolderWrapper.
     * @throws SQLException if thrown by the JDBC driver
     */
    public boolean supportsSavepoints() throws SQLException {
        if (this.savepointsSupported == null) {
            this.savepointsSupported = new Boolean(getConnection().getMetaData().supportsSavepoints());
        }
        return this.savepointsSupported.booleanValue();
    }

    /**
     * Create a new JDBC 3.0 Savepoint for the current Connection,
     * using generated savepoint names that are unique for the Connection.
     * @return the new Savepoint
     * @throws SQLException if thrown by the JDBC driver
     */
    public Savepoint createSavepoint() throws SQLException {
        this.savepointCounter++;
        return getConnection().setSavepoint(SAVEPOINT_NAME_PREFIX + this.savepointCounter);
    }

    /**
     * Releases the current Connection held by this ConnectionHolderWrapper.
     * <p>This is necessary for ConnectionHandles that expect "Connection borrowing",
     * where each returned Connection is only temporarily leased and needs to be
     * returned once the data operation is done, to make the Connection available
     * for other operations within the same transaction. This is the case with
     */
    public void released() {
        super.released();
        if (this.currentConnection != null) {
            this.connectionHandle.releaseConnection(this.currentConnection);
            this.currentConnection = null;
        }
    }


    public void clear() {
        super.clear();
        this.transactionActive = false;
        this.savepointsSupported = null;
        this.savepointCounter = 0;
    }
}


