package com.talanlabs.guiceunit.extensions.dbunit;

import java.sql.Connection;

public interface DbSessionManager {

    /**
     * Start session
     */
    void start();

    /**
     * Rollback and close session
     */
    void rollbackAndClose();

    /**
     * @return get a connection
     */
    Connection getConnection();

}
