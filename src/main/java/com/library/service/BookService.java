package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {

    private final BookDAO bookDAO = new BookDAOImpl();

    public boolean addBook(Book book) throws SQLException {
        return bookDAO.save(book);
    }

    public boolean bookExists(String bookId) throws SQLException {
        return bookDAO.search(bookId) != null;
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAll();
    }

    public List<Book> getAvailableBooks() throws SQLException {
        return bookDAO.getAvailable();
    }
}
