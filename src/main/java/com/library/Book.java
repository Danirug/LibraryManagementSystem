package com.library;

public class Book {
    private final String bookId;
    private final String title;
    private final String author;
    private final String category;
    private final int publishedYear;
    private final int quantity;
    private int availableQuantity;

    public Book(String bookId, String title, String author, String category,
                int publishedYear, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publishedYear = publishedYear;
        this.quantity = quantity;
        this.availableQuantity = quantity;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getPublishedYear() { return publishedYear; }
    public int getQuantity() { return quantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    @Override
    public String toString() {          // this is what a ComboBox displays
        return bookId + " - " + title;
    }
}