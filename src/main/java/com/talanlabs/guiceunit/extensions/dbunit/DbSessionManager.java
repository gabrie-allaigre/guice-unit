package com.talanlabs.guiceunit.extensions.dbunit;

import java.sql.Connection;

public interface DbSessionManager {

    /**
     * Open session
     */
    void open();

    /**
     * Close session
     */
    void close();

    /**
     * @return get a connection
     */
    Connection getConnection();

}
