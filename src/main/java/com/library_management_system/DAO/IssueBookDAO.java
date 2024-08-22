package com.library_management_system.DAO;

import com.library_management_system.Connection.DatabaseConnection;
import com.library_management_system.Model.IssueBook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IssueBookDAO {
    private static final Logger LOGGER = Logger.getLogger(IssueBookDAO.class.getName());

    public static void saveIssueBookDetail(IssueBook issueBook) throws SQLException {
        String query = "INSERT INTO issue_book_detail (book_id, book_name, student_id, student_name, issue_date, due_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, issueBook.getBookID());
            pstmt.setString(2, issueBook.getBookName());
            pstmt.setInt(3, issueBook.getStudentID());
            pstmt.setString(4, issueBook.getStudentName());
            pstmt.setDate(5, Date.valueOf(issueBook.getIssueDate()));
            pstmt.setDate(6, Date.valueOf(issueBook.getDueDate()));
            pstmt.setString(7, issueBook.getStatus());
            pstmt.executeUpdate();
        }
    }

    public static boolean isBookAlreadyIssued(int bookID, int studentID) {
        String query = "SELECT COUNT(*) FROM issue_book_detail WHERE book_id = ? AND student_id = ? AND status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookID);
            pstmt.setInt(2, studentID);
            pstmt.setString(3, "pending");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in isBookAlreadyIssued", e);
        }
        return false;
    }

    public static IssueBook findIssue(int studentID, int bookID) {
        String query = "SELECT * FROM issue_book_detail WHERE student_id = ? AND book_id = ? AND status = 'pending' ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, bookID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new IssueBook(
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in findIssue", e);
        }
        return null;
    }

    public static void updateIssueBookDetail(int issueID) {
        String query = "UPDATE issue_book_detail SET status = 'returned' WHERE issue_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, issueID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in updateIssueBookDetail", e);
        }
    }

    public static List<IssueBook> findIssuesByDateRange(LocalDate fromDate, LocalDate toDate) {
        String query = "SELECT * FROM issue_book_detail WHERE issue_date between ? and ?";
        List<IssueBook> issues = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                issues.add(new IssueBook(
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in findIssuesByDateRange", e);
        }
        return issues;
    }

    public static List<IssueBook> showAllRecords() {
        String query = "SELECT * FROM issue_book_detail";
        List<IssueBook> issues = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                issues.add(new IssueBook(
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in showAllRecords", e);
        }
        return issues;
    }


    public static List<IssueBook> showAllIssuedBooks() {
        String query = "SELECT * FROM issue_book_detail WHERE status = 'pending'";
        List<IssueBook> issues = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                issues.add(new IssueBook(
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in showAllIssuedBooks", e);
        }
        return issues;
    }

    public static List<IssueBook> showAllOverdueBooks() {
        String query = "SELECT * FROM issue_book_detail WHERE status = 'pending' AND due_date < ?";
        List<IssueBook> issues = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                issues.add(new IssueBook(
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception in showAllOverdueBooks", e);
        }
        return issues;
    }
}
