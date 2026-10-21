package com.library.dao;

import com.library.model.BorrowRecord;

import java.sql.SQLException;
import java.util.List;

public interface BorrowRecordDAO extends CrudDAO<BorrowRecord, Integer> {
    List<BorrowRecord> getActive() throws SQLException;
    boolean hasActiveLoans(String memberId) throws SQLException;
    boolean isBookBorrowedBy(String memberId, String bookId) throws SQLException;
    int countActive() throws SQLException;
    int countOverdue() throws SQLException;
}
