package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    // Database connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hospital";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "qwedcxzas";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username (admin/doctor): ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        // Verify credentials
        if ((username.equals("admin") && password.equals("admin")) ||
                (username.equals("doctor") && password.equals("doctor"))) {
            System.out.println("Login successful!");

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                if (username.equals("admin")) {
                    System.out.println("Accessing admin privileges...");
                    showMenuAndHandleInput(connection, "admin");
                } else if (username.equals("doctor")) {
                    System.out.println("Accessing doctor privileges...");
                    showMenuAndHandleInput(connection, "doctor");
                }
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid credentials. Access denied.");
        }

        scanner.close();
    }

    private static void showMenuAndHandleInput(Connection connection, String role) {
        Scanner scanner = new Scanner(System.in);
        String[] tables;

        if (role.equals("admin")) {
            tables = new String[]{"patient", "bill", "location", "records", "room", "staff"};
        } else { // doctor
            tables = new String[]{"patient", "records", "location", "room"};
        }

        while (true) {
            System.out.println("\nAvailable tables:");
            for (String table : tables) {
                System.out.println("- " + table);
            }
            System.out.println("Type a table name to view its data, or type 'exit' to log out:");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Logging out...");
                break;
            }

            boolean validTable = false;
            for (String table : tables) {
                if (table.equalsIgnoreCase(input)) {
                    validTable = true;
                    displayTableData(connection, table);
                    break;
                }
            }

            if (!validTable) {
                System.out.println("Invalid table name. Please try again.");
            }
        }
    }

    private static void displayTableData(Connection connection, String tableName) {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("\nFetching data from " + tableName + " table...");
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + ": " + rs.getString(i) + "  ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Error fetching data from " + tableName + ": " + e.getMessage());
        }
    }
}
