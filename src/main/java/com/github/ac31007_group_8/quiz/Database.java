package com.github.ac31007_group_8.quiz;

import com.github.ac31007_group_8.quiz.util.CircularBuffer;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database handler.
 *
 * @author Robert T.
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"unused", "WeakerAccess"})
public class Database {

    private Database() {} // Static

    private static String DB_URL;
    private static Logger LOGGER = LoggerFactory.getLogger(Database.class);
    private static MockDataProvider mockDataProvider = null;
    private static CircularBuffer<Connection> connBuffer = new CircularBuffer<>(8, (conn) -> {
        try {
            conn.close();
        } catch (Exception ex) {
            LOGGER.warn("Error closing connection during eviction.", ex);
        }
    });

    private static String getURL() {
        // Cache it, to save constant string rebuilds
        if (DB_URL != null) return DB_URL;
        DB_URL = "jdbc:mysql://" + Configuration.DATABASE_HOST + ":" + Configuration.DATABASE_PORT + "/" + Configuration.DATABASE_SCHEMA+"?rewriteBatchedStatements=true";
        return DB_URL;
    }

    /**
     * Get a connection directly from JDBC.
     *
     * This allows you to use the standard JDBC flow, as shown in this article: http://www.vogella.com/tutorials/MySQLJava/article.html
     *
     * Connections will <b>automatically</b> be closed, eventually.
     *
     * @return A fresh JDBC connection to use.
     */
    public static Connection getConnection() {
        try {
            Connection conn;
            if (mockDataProvider != null) {
                conn = new MockConnection(mockDataProvider);
            } else {
                conn = DriverManager.getConnection(getURL(), Configuration.DATABASE_USER, Configuration.DATABASE_PASSWORD);
            }
            // The buffer will automatically close connections after 8 other connections have been established,
            // keeping our 'live' connection count to, at worst, 8.
            connBuffer.add(conn);
            return conn;
        } catch(Exception ex) {
            // TODO: Make this less brutish.
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets a jOOQ DSL to use for database communication.
     *
     * This is provided for the SQLphobes in the team, as it provides a "SQL-ish" Java interface rather than raw SQL. Do
     * not hold these objects - get a new one for each new client request. Older connections will be killed, eventually.
     *
     * See the jOOQ documentation for more: https://www.jooq.org/doc/3.9/manual/
     *
     * @return A jOOQ DSL, typically called 'create' in the documentation.
     */
    public static DSLContext getJooq() {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }

    /**
     * Allows overriding jOOQ connections returned by getJooq() via the jOOQ MockDataProvider. This affects ALL jOOQ
     * connections (via getJooq) after this point! Passing null reverts the behaviour to normal.
     *
     * @param mockDataProvider The mock provider, or null to disable again.
     */
    public static void setMockProvider(@Nullable MockDataProvider mockDataProvider) {
        Database.mockDataProvider = mockDataProvider;
    }

}
