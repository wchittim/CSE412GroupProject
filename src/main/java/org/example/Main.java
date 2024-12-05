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
    private static final String DB_PASSWORD = "your_db_password";

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
                    accessAdminFeatures(connection);
                } else if (username.equals("doctor")) {
                    System.out.println("Accessing doctor privileges...");
                    accessDoctorFeatures(connection);
                }
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid credentials. Access denied.");
        }

        scanner.close();
    }

    // Admin-specific functionality
    private static void accessAdminFeatures(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Fetching all users...");
            ResultSet rs = stmt.executeQuery("SELECT * FROM users;"); // Example query for admins

            while (rs.next()) {
                System.out.println("User: " + rs.getString("username"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching admin data: " + e.getMessage());
        }
    }

    // Doctor-specific functionality
    private static void accessDoctorFeatures(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Fetching patient data...");
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients WHERE assigned_doctor = 'doctor';"); // Example query for doctors

            while (rs.next()) {
                System.out.println("Patient: " + rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching doctor data: " + e.getMessage());
        }
    }
}