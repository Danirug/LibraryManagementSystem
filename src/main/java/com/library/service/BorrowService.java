package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.dao.impl.BorrowRecordDAOImpl;
import com.library.db.DBConnection;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BorrowService {

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();
    private final BookDAO bookDAO = new BookDAOImpl();

    public boolean isBookBorrowedBy(Member member, Book book) throws SQLException {
        return borrowRecordDAO.isBookBorrowedBy(member.getMemberId(), book.getBookId());
    }

    /** Saves the loan and takes one copy off the shelf, both in one transaction. */
    public void issueBook(Member member, Book book, LocalDate issueDate, LocalDate dueDate) throws SQLException {
        runInTransaction(() -> {
            borrowRecordDAO.save(new BorrowRecord(member, book, issueDate, dueDate));
            bookDAO.changeAvailableQuantity(book.getBookId(), -1);
        });
    }

    /** Marks the loan as returned and puts the copy back, both in one transaction. */
    public void returnBook(BorrowRecord record, LocalDate returnDate) throws SQLException {
        runInTransaction(() -> {
            record.setReturnDate(returnDate);
            borrowRecordDAO.update(record);
            bookDAO.changeAvailableQuantity(record.getBook().getBookId(), 1);
        });
    }

    public List<BorrowRecord> getAllRecords() throws SQLException {
        return borrowRecordDAO.getAll();
    }

    public List<BorrowRecord> getActiveRecords() throws SQLException {
        return borrowRecordDAO.getActive();
    }

    public int countCurrentlyBorrowed() throws SQLException {
        return borrowRecordDAO.countActive();
    }

    public int countOverdue() throws SQLException {
        return borrowRecordDAO.countOverdue();
    }

    private interface SqlWork {
        void run() throws SQLException;
    }

    /** Commits all the work together, or rolls everything back if any step fails. */
    private void runInTransaction(SqlWork work) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            work.run();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
