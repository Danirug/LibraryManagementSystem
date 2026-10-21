package com.library.dao;

import com.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO extends CrudDAO<Book, String> {
    List<Book> getAvailable() throws SQLException;
    /** Adds the change (e.g. -1 when issued, +1 when returned) to the available copies. */
    boolean changeAvailableQuantity(String bookId, int change) throws SQLException;
}
