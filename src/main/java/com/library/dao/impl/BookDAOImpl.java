package com.library.dao.impl;

import com.library.dao.BookDAO;
import com.library.dao.CrudUtil;
import com.library.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    @Override
    public boolean save(Book book) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO books (book_id, title, author, category, published_year, quantity, available_quantity) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                book.getBookId(), book.getTitle(), book.getAuthor(), book.getCategory(),
                book.getPublishedYear(), book.getQuantity(), book.getAvailableQuantity()) > 0;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        return CrudUtil.executeUpdate(
                "UPDATE books SET title = ?, author = ?, category = ?, published_year = ?, quantity = ?, "
                        + "available_quantity = ? WHERE book_id = ?",
                book.getTitle(), book.getAuthor(), book.getCategory(), book.getPublishedYear(),
                book.getQuantity(), book.getAvailableQuantity(), book.getBookId()) > 0;
    }

    @Override
    public boolean delete(String bookId) throws SQLException {
        return CrudUtil.executeUpdate("DELETE FROM books WHERE book_id = ?", bookId) > 0;
    }

    @Override
    public Book search(String bookId) throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery("SELECT * FROM books WHERE book_id = ?", bookId)) {
            return rs.next() ? toBook(rs) : null;
        }
    }

    @Override
    public List<Book> getAll() throws SQLException {
        return query("SELECT * FROM books ORDER BY book_id");
    }

    @Override
    public List<Book> getAvailable() throws SQLException {
        return query("SELECT * FROM books WHERE available_quantity > 0 ORDER BY book_id");
    }

    @Override
    public boolean changeAvailableQuantity(String bookId, int change) throws SQLException {
        return CrudUtil.executeUpdate(
                "UPDATE books SET available_quantity = available_quantity + ? WHERE book_id = ?",
                change, bookId) > 0;
    }

    private List<Book> query(String sql) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (ResultSet rs = CrudUtil.executeQuery(sql)) {
            while (rs.next()) {
                books.add(toBook(rs));
            }
        }
        return books;
    }

    private Book toBook(ResultSet rs) throws SQLException {
        return new Book(rs.getString("book_id"), rs.getString("title"), rs.getString("author"),
                rs.getString("category"), rs.getInt("published_year"), rs.getInt("quantity"),
                rs.getInt("available_quantity"));
    }
}
