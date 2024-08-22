    package com.library_management_system.DAO;

    import com.library_management_system.Connection.DatabaseConnection;
    import com.library_management_system.Model.Book;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;

    public class BookDAO {
        public static boolean addBook(int id, String nameBook, String author, int quantity) {
            String query = "INSERT INTO books(book_id, book_name, author, quantity) values (?,?,?,?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, nameBook);
                pstmt.setString(3, author);
                pstmt.setInt(4, quantity);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        public static boolean deleteBook(int bookID) {
            String query = "DELETE FROM books WHERE book_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, bookID);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        public static boolean updateBook(int bookId, String bookName, String author, int quantity) {
            String query = "UPDATE books SET book_name = ?, author = ?, quantity = ? WHERE book_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, bookName);
                pstmt.setString(2, author);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, bookId);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        public static boolean bookExists(int bookId) {
            String query = "SELECT COUNT(*) FROM books WHERE book_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, bookId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        public static Book getBookDetails(int bookId) {
            Book book = null;
            String query = "SELECT * FROM books WHERE book_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, bookId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("book_name");
                    String author = rs.getString("author");
                    int quantity = rs.getInt("quantity");
                    book = new Book(bookId, name, author, quantity);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return book;
        }

        public static void updateBookQuantity(int bookID, int quantity) throws SQLException {
            String query = "UPDATE books SET quantity = quantity + ? WHERE book_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setInt(2, bookID);
                preparedStatement.executeUpdate();
            }
        }

        public static List<Book> searchBook(String keyword) {
            List<Book> bookList = new ArrayList<>();
            String query = "SELECT book_id, book_name, author, quantity " +
                    "FROM books " +
                    "WHERE book_id LIKE N'%' + ? + N'%' " +
                    "   OR book_name LIKE N'%' + ? + N'%' " +
                    "   OR author LIKE N'%' + ? + N'%' " +
                    "   OR quantity LIKE N'%' + ? + N'%'";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
    //            String searchKeyword = "%" + keyword + "%";
                for (int i = 1; i <= 4; i++) {
                    pstmt.setString(i, keyword);
                }
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    String author = rs.getString("author");
                    int quantity = rs.getInt("quantity");

                    bookList.add(new Book(bookId, bookName, author, quantity));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return bookList;
        }
    }