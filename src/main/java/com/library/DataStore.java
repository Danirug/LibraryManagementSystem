package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

/**
 * Holds all application data in memory (no database needed for this assignment).
 * One shared instance, so every page sees the same lists.
 */
public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final ObservableList<Book> books = FXCollections.observableArrayList();
    private final ObservableList<Member> members = FXCollections.observableArrayList();
    private final ObservableList<BorrowRecord> records = FXCollections.observableArrayList();

    private DataStore() {
        loadSampleData();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public ObservableList<Book> getBooks() { return books; }
    public ObservableList<Member> getMembers() { return members; }
    public ObservableList<BorrowRecord> getRecords() { return records; }

    public Book findBookById(String id) {
        return books.stream().filter(b -> b.getBookId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Member findMemberById(String id) {
        return members.stream().filter(m -> m.getMemberId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public List<Book> getAvailableBooks() {
        return books.stream().filter(b -> b.getAvailableQuantity() > 0).toList();
    }

    public List<BorrowRecord> getActiveRecords() {
        return records.stream().filter(r -> !r.isReturned()).toList();
    }

    public boolean hasActiveLoans(Member member) {
        return records.stream().anyMatch(r -> r.getMember() == member && !r.isReturned());
    }

    public boolean isBookBorrowedBy(Member member, Book book) {
        return records.stream().anyMatch(r ->
                r.getMember() == member && r.getBook() == book && !r.isReturned());
    }

    public long countCurrentlyBorrowed() {
        return records.stream().filter(r -> !r.isReturned()).count();
    }

    public long countOverdue() {
        return records.stream().filter(BorrowRecord::isOverdue).count();
    }

    public BorrowRecord issueBook(Member member, Book book, LocalDate issueDate, LocalDate dueDate) {
        BorrowRecord record = new BorrowRecord(member, book, issueDate, dueDate);
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        records.add(record);
        return record;
    }

    public void returnBook(BorrowRecord record, LocalDate returnDate) {
        record.setReturnDate(returnDate);
        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
    }

    private void loadSampleData() {
        Book b1 = new Book("B001", "Clean Code", "Robert C. Martin", "Technology", 2008, 3);
        Book b2 = new Book("B002", "Sapiens", "Yuval Noah Harari", "History", 2011, 2);
        Book b3 = new Book("B003", "The Alchemist", "Paulo Coelho", "Fiction", 1988, 4);
        Book b4 = new Book("B004", "A Brief History of Time", "Stephen Hawking", "Science", 1988, 2);
        Book b5 = new Book("B005", "Atomic Habits", "James Clear", "Non-Fiction", 2018, 3);
        books.addAll(b1, b2, b3, b4, b5);

        Member m1 = new Member("M001", "Alex Johnson", "alex.johnson@example.com", "0711234567", "12 Main Street");
        Member m2 = new Member("M002", "Priya Sharma", "priya.sharma@example.com", "0722345678", "45 Lake Road");
        Member m3 = new Member("M003", "Daniel Silva", "daniel.silva@example.com", "0753456789", "8 Hill Avenue");
        Member m4 = new Member("M004", "Emma Wilson", "emma.wilson@example.com", "0764567890", "101 Park Lane");
        members.addAll(m1, m2, m3, m4);

        LocalDate today = LocalDate.now();
        issueBook(m1, b1, today.minusDays(10), today.plusDays(4));               // Borrowed
        issueBook(m2, b2, today.minusDays(20), today.minusDays(6));              // Overdue
        BorrowRecord done = issueBook(m3, b3, today.minusDays(30), today.minusDays(16));
        returnBook(done, today.minusDays(18));                                   // Returned
    }
}