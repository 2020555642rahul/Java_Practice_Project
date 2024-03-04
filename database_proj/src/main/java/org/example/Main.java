package org.example;

//import java.sql.Connection;
import java.sql.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
      /*  DbHandler db = new DbHandler();
        db.connect_to_db("postgres","postgres","ksolves"); */
        DbHandler db = new DbHandler("postgres", "postgres", "ksolves");
        db.create_table();
        db.insertData("Rahul", 21);
        db.insertData("Vansh", 20);
        db.insertData("Siddhant",21);
        db.insertData("Ankit",21);
        db.insertData("Ayan",21);

        db.readData();
        db.updateData(3,"Rohit",21);
        db.DeleteData(5);
        db.closeConnection();

    }
}