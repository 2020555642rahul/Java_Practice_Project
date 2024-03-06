package org.example;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class CSV_ExecutorTest {

    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "ksolves";

    private static Connection connection;

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    @AfterClass
    public static void DropTable() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists testtable");
            System.out.println("Table TestTable dropped successfully");
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Test
    public void testCreateTable() throws SQLException {
        String tableName = "TestTable";
        String[] columns = {"ID", "NAME", "AGE"};

        try {
            CSV_Executor.createTable(connection, tableName, columns);

            // Check if the table has been created
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select exists ( select from pg_tables where tablename = 'TestTable')");


            assertTrue(resultSet.next()); // Table exists
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}


