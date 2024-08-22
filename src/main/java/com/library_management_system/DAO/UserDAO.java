package com.library_management_system.DAO;

import com.library_management_system.Connection.DatabaseConnection;
import com.library_management_system.Util.PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void insertUser(String username, String user_password, String email) {
        String hashedPassword = PasswordHash.hashPassword(user_password);
        String query = "INSERT INTO Users(username, user_password, email) values (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
//            System.out.println("Executing query: " + query);
//            System.out.println("With parameters: " + username + ", " + hashedPassword + ", " + email);
            pstmt.executeUpdate();
//            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public static boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean checkUser(String username, String password) {
        String query = "SELECT user_password FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("user_password");
                boolean passwordMatch = PasswordHash.checkPassword(password, hashedPassword);
//                System.out.println("Password match: " + passwordMatch);
                return passwordMatch;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}