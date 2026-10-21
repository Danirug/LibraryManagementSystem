package com.library.dao.impl;

import com.library.dao.BorrowRecordDAO;
import com.library.dao.CrudUtil;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAOImpl implements BorrowRecordDAO {

    // Joins each record with its member and book so the full objects can be built
    private static final String SELECT_JOINED =
            "SELECT r.record_id, r.issue_date, r.due_date, r.return_date, "
                    + "m.member_id, m.full_name, m.email, m.phone, m.address, "
                    + "b.book_id, b.title, b.author, b.category, b.published_year, b.quantity, b.available_quantity "
                    + "FROM borrow_records r "
                    + "JOIN members m ON r.member_id = m.member_id "
                    + "JOIN books b ON r.book_id = b.book_id ";

    @Override
    public boolean save(BorrowRecord record) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO borrow_records (member_id, book_id, issue_date, due_date, return_date) "
                        + "VALUES (?, ?, ?, ?, ?)",
                record.getMemberId(), record.getBook().getBookId(), Date.valueOf(record.getIssueDate()),
                Date.valueOf(record.getDueDate()), toSqlDate(record.getReturnDate())) > 0;
    }

    @Override
    public boolean update(BorrowRecord record) throws SQLException {
        return CrudUtil.executeUpdate(
                "UPDATE borrow_records SET issue_date = ?, due_date = ?, return_date = ? WHERE record_id = ?",
                Date.valueOf(record.getIssueDate()), Date.valueOf(record.getDueDate()),
                toSqlDate(record.getReturnDate()), record.getRecordId()) > 0;
    }

    @Override
    public boolean delete(Integer recordId) throws SQLException {
        return CrudUtil.executeUpdate("DELETE FROM borrow_records WHERE record_id = ?", recordId) > 0;
    }

    @Override
    public BorrowRecord search(Integer recordId) throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery(SELECT_JOINED + "WHERE r.record_id = ?", recordId)) {
            return rs.next() ? toRecord(rs) : null;
        }
    }

    @Override
    public List<BorrowRecord> getAll() throws SQLException {
        return query(SELECT_JOINED + "ORDER BY r.issue_date DESC, r.record_id DESC");
    }

    @Override
    public List<BorrowRecord> getActive() throws SQLException {
        return query(SELECT_JOINED + "WHERE r.return_date IS NULL ORDER BY r.due_date");
    }

    @Override
    public boolean hasActiveLoans(String memberId) throws SQLException {
        return count("SELECT COUNT(*) FROM borrow_records WHERE member_id = ? AND return_date IS NULL",
                memberId) > 0;
    }

    @Override
    public boolean isBookBorrowedBy(String memberId, String bookId) throws SQLException {
        return count("SELECT COUNT(*) FROM borrow_records "
                + "WHERE member_id = ? AND book_id = ? AND return_date IS NULL", memberId, bookId) > 0;
    }

    @Override
    public int countActive() throws SQLException {
        return count("SELECT COUNT(*) FROM borrow_records WHERE return_date IS NULL");
    }

    @Override
    public int countOverdue() throws SQLException {
        return count("SELECT COUNT(*) FROM borrow_records WHERE return_date IS NULL AND due_date < CURDATE()");
    }

    private int count(String sql, Object... args) throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery(sql, args)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<BorrowRecord> query(String sql) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        try (ResultSet rs = CrudUtil.executeQuery(sql)) {
            while (rs.next()) {
                records.add(toRecord(rs));
            }
        }
        return records;
    }

    private BorrowRecord toRecord(ResultSet rs) throws SQLException {
        Member member = new Member(rs.getString("member_id"), rs.getString("full_name"),
                rs.getString("email"), rs.getString("phone"), rs.getString("address"));
        Book book = new Book(rs.getString("book_id"), rs.getString("title"), rs.getString("author"),
                rs.getString("category"), rs.getInt("published_year"), rs.getInt("quantity"),
                rs.getInt("available_quantity"));
        Date returnDate = rs.getDate("return_date");
        return new BorrowRecord(rs.getInt("record_id"), member, book,
                rs.getDate("issue_date").toLocalDate(), rs.getDate("due_date").toLocalDate(),
                returnDate == null ? null : returnDate.toLocalDate());
    }

    private static Date toSqlDate(LocalDate date) {
        return date == null ? null : Date.valueOf(date);
    }
}
