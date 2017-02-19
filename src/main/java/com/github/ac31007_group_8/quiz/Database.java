package com.github.ac31007_group_8.quiz;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database handler.
 *
 * @author Robert T.
 */
@ParametersAreNonnullByDefault
public class Database {

    private Database() {} // Static

    private static String DB_URL;

    private static String getURL() {
        // Cache it, to save constant string rebuilds
        if (DB_URL != null) return DB_URL;
        DB_URL = "jdbc:mysql://" + Configuration.DATABASE_HOST + ":" + Configuration.DATABASE_PORT + "/" + Configuration.DATABASE_SCHEMA;
        return DB_URL;
    }

    /**
     * Get a connection directly from JDBC.
     *
     * This allows you to use the standard JDBC flow, as shown in this article: http://www.vogella.com/tutorials/MySQLJava/article.html
     *
     * <b>Remember to CLOSE your connection when you're done with it!</b>
     *
     * @return A fresh JDBC connection to use.
     */
    public static Connection getConnection() {
        // TODO: Pooling? Something more efficient?
        try (Connection conn = DriverManager.getConnection(getURL(), Configuration.DATABASE_USER, Configuration.DATABASE_PASSWORD)) {
            return conn;
        } catch(Exception ex) {
            // TODO: Make this less brutish.
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets a jOOQ DSL to use for database communication.
     *
     * This is provided for the SQLphobes in the team, as it provides a "SQL-ish" Java interface rather than raw SQL.
     *
     * See the jOOQ documentation for more: https://www.jooq.org/doc/3.9/manual/
     *
     * @return A jOOQ DSL, typically called 'create' in the documentation.
     */
    public static DSLContext getJooq() {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }
    /**
     * 
     * @param tableName - Name of table to return entry count from
     * @return # of entries
     */

}
