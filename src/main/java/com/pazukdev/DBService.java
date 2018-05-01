package com.pazukdev;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
//import com.mysql.jdbc.Connection;

import com.mysql.jdbc.Constants;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBService {

    static Connection connection;

    static final String URL="jdbc:mysql://localhost:3306/demo_hotels?useSSL=true&amp;autoReconnect=true&amp;serverTimezone=UTC";
    static final String USERNAME="demo";
    static final String PASSWORD="D1e2m3o4O1E3AE!3";

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new FabricMySQLDriver());
            connection=DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new
                    JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase("db/db.changelog.xml",
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());
            database.close();

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e.getClass() + ": " + e.getMessage());
                }
            }
        }
    }

}
