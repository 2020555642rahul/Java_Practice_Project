package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CSV_Executor {
    static int count = 0;

    public static Logger logger = LogManager.getLogger(CSV_Executor.class);

    public static void main(String[] args) {
        // Database connection details
        String jdbcURL = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String username = "postgres";
        String password = "ksolves";

        // CSV file path
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the file path");
        String csvFilePath = s.next();

        // Initialize ExecutorService with a fixed pool of threads
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Change 5 to the desired number of threads

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            if (connection != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

            // Read the first line of the CSV file to get column names
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String[] columns = reader.readLine().split(",");

            System.out.println("Attributes of the Table: ");
            for (String str : columns) {
                System.out.print(str + ",  ");
            }

            // Create the table dynamically based on column names
            createTable(connection, "CSV_Data", columns);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Submit task to ExecutorService for each row insertion
                Connection finalConnection = connection;
                executorService.submit(() -> {
                    logger.info("Thread id:"+ Thread.currentThread().getId()+"Inserting the Rows into Database");
                    insertRow(finalConnection, "CSV_Data", columns, values);
                });
            }

            System.out.println("Data insertion tasks submitted.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Shutdown ExecutorService
            executorService.shutdown();
            try {
                // Wait for all tasks to complete or until the specified timeout
                executorService.awaitTermination(2, TimeUnit.MINUTES); // Adjust the timeout as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Close the database connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.debug("Hello this is logger");
        logger.info("Hello this is info");
    }

    // Method to dynamically create table based on column names
    public static void createTable(Connection connection, String tableName, String[] columns) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (");
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]).append(" VARCHAR(255)");
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");
        String createTableQuery = queryBuilder.toString();
        connection.createStatement().executeUpdate(createTableQuery);
    }

    // Method to insert a row into the table

    public static void insertRow(Connection connection, String tableName, String[] columns, String[] values) {
        try {
            String insertQuery = "INSERT INTO " + tableName + " (" +
                    Arrays.stream(columns).collect(Collectors.joining(", ")) +
                    ") VALUES (" +
                    Arrays.stream(columns).map(column -> "?").collect(Collectors.joining(", ")) +
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (int i = 0; i < columns.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            count++;

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
