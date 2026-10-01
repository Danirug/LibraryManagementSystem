package com.library;

import java.time.LocalDate;

public class BorrowRecord {
    private final Member member;
    private final Book book;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;   // null while the book is still borrowed

    public BorrowRecord(Member member, Book book, LocalDate issueDate, LocalDate dueDate) {
        this.member = member;
        this.book = book;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    // Convenience getters used by TableView columns
    public String getMemberId() { return member.getMemberId(); }
    public String getMemberName() { return member.getFullName(); }
    public String getBookTitle() { return book.getTitle(); }

    public boolean isReturned() { return returnDate != null; }

    public boolean isOverdue() {
        return !isReturned() && LocalDate.now().isAfter(dueDate);
    }

    public String getStatus() {
        if (isReturned()) return "Returned";
        return isOverdue() ? "Overdue" : "Borrowed";
    }
}