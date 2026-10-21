package com.library.service;

import com.library.dao.BorrowRecordDAO;
import com.library.dao.MemberDAO;
import com.library.dao.impl.BorrowRecordDAOImpl;
import com.library.dao.impl.MemberDAOImpl;
import com.library.model.Member;

import java.sql.SQLException;
import java.util.List;

public class MemberService {

    private final MemberDAO memberDAO = new MemberDAOImpl();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();

    public boolean registerMember(Member member) throws SQLException {
        return memberDAO.save(member);
    }

    public boolean updateMember(Member member) throws SQLException {
        return memberDAO.update(member);
    }

    /** A member who still has borrowed books can't be deleted. */
    public boolean deleteMember(String memberId) throws SQLException {
        if (hasActiveLoans(memberId)) {
            return false;
        }
        return memberDAO.delete(memberId);
    }

    public boolean memberExists(String memberId) throws SQLException {
        return memberDAO.search(memberId) != null;
    }

    public boolean hasActiveLoans(String memberId) throws SQLException {
        return borrowRecordDAO.hasActiveLoans(memberId);
    }

    public List<Member> getAllMembers() throws SQLException {
        return memberDAO.getAll();
    }
}
