package com.library_management_system.Model;

import java.time.LocalDate;

public class IssueBook {
    private int issueID;
    private int bookID;
    private String bookName;
    private int studentID;
    private String studentName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String status;

    // Constructor

    public IssueBook(int issueID, int bookID, String bookName, int studentID, String studentName, LocalDate issueDate, LocalDate dueDate, String status) {
        this.issueID = issueID;
        this.bookID = bookID;
        this.bookName = bookName;
        this.studentID = studentID;
        this.studentName = studentName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public IssueBook(int bookID, String bookName, int studentID, String studentName, LocalDate issueDate, LocalDate dueDate, String status) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.studentID = studentID;
        this.studentName = studentName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    //Getters and setters
    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}