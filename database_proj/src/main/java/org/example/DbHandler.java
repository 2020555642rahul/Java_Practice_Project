package org.example;


/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
*/
import org.postgresql.Driver;
import org.w3c.dom.ls.LSOutput;

import java.sql.*;
/*public class DbHandler {
    public Connection connect_to_db(String dbname, String username, String password) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/" + dbname, username, password);
            if (connection != null) {
                System.out.println("connecttion established");
                String create_table = "CREATE TABLE IF NOT EXISTS STUDENT_TABLE(" +
                        "id SERIAL PRIMARY KEY,"
                        +"name VARCHAR(20) NOT NULL,"
                        +"age INT"
                        +")";
                Statement statement = connection.createStatement();
                statement.execute(create_table);
                String query = "INSERT INTO STUDENT_TABLE (id, name, age) VALUES(1,'Rahul',21),(2,'Vansh',20),(3,'Siddhant',21),(4,'Ankit',21),(5,'Ayan',22),(6,'Rohit',21)";
                statement.executeUpdate(query);
                connection.commit();
                System.out.println("Tables Created and values inserted Successfully");

            } else {
                System.out.println("error in connection");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
} */
public class DbHandler{
    private Connection connection;
    public DbHandler(String dbname, String username, String password){

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/"+dbname,username,password);
            if(connection!=null){
                System.out.println("Connection Established");
            }else{
                System.out.println("Error in Connection");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }



    public void create_table(){
        try {
            String q = "create table if not exists my_table(" +
                    "id serial primary key,"
                    + "name varchar(20) not null,"
                    + "age INT"
                    + ")";
            Statement statement = connection.createStatement();
            statement.execute(q);
            System.out.println("Table Created Successfully");
        }catch(SQLException e){
            System.out.println("Error in Creating Table"+e.getMessage());
        }


    }

    public void insertData(String name, int age){
        try {
            String insert_q = "insert into my_table(name,age) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert_q);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.executeUpdate();
            System.out.println("Data Inserted Successfully");
        }catch(SQLException e){
            System.out.println("Error in Data Inserting"+e.getMessage());
        }
    }
    public void readData(){
        try{
            String q = "select * from my_table";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(q);
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("Id:" +id+ " " +"Name: "+name+" "+"Age: "+age);
            }
        }catch(SQLException e){
            System.out.println("Error in Reading: ");
        }
    }
    public void updateData(int id, String name, int age){
        try {
            String q = "update my_table set name = ?, age = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(q);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setInt(3,id);
            int rows_affected = preparedStatement.executeUpdate();
            if(rows_affected>0){
                System.out.println("Rows Updated Sucessfully");
            }else{
                System.out.println("No Rows Updated");
            }
        }catch (SQLException e){
            System.out.println("Error in Updating the Data"+e.getMessage());
        }
    }

    public void DeleteData(int id){
        try{
            String q = "delete from my_table where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(q);
            preparedStatement.setInt(1,id);
            int rows_affected = preparedStatement.executeUpdate();
            if(rows_affected>0){
                System.out.println("Rows Deleted Successfully");
            }else{
                System.out.println("No rows were deleted");
            }
        }catch(SQLException e){
            System.out.println("Error in Deleting the Data"+e.getMessage());
        }
    }

    public void closeConnection(){
        try{
            if(connection!=null){
                connection.close();
                System.out.println("Connection Closed");
            }
        }catch(SQLException e){
            System.out.println("Error in Close "+e.getMessage());
        }
    }
}
