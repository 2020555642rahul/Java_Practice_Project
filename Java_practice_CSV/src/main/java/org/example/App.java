package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DataBaseHandler db = new DataBaseHandler();
        db.connect_to_db("postgres","postgres","ksolves");
        Scanner s = new Scanner(System.in);
        // Check if the user provided the file path as a command-line argument
        System.out.println("Enter the File Path: ");
        String filePath = s.next();
                //args[0];
        if (filePath.length() == 0) {
            System.err.println("Usage: java App <file-path>");
            return;
        }

        // Retrieve the file path from the command-line arguments


        try {
            // Create a File object
            File file = new File(filePath);

            // Create a Scanner to read from the file
            Scanner sc = new Scanner(file);

            // Set the delimiter pattern
            sc.useDelimiter(",");

            // Read and print each token from the file
            int i = 0;
           while (i<5) {
               System.out.print(sc.next()+" ");
            }

            // Close the scanner
            sc.close();
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            System.err.println("File not found: " + e.getMessage());
        }
    }
}

