package com.library.dao.impl;

import com.library.dao.CrudUtil;
import com.library.dao.MemberDAO;
import com.library.model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    @Override
    public boolean save(Member member) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO members (member_id, full_name, email, phone, address) VALUES (?, ?, ?, ?, ?)",
                member.getMemberId(), member.getFullName(), member.getEmail(),
                member.getPhone(), member.getAddress()) > 0;
    }

    @Override
    public boolean update(Member member) throws SQLException {
        return CrudUtil.executeUpdate(
                "UPDATE members SET full_name = ?, email = ?, phone = ?, address = ? WHERE member_id = ?",
                member.getFullName(), member.getEmail(), member.getPhone(),
                member.getAddress(), member.getMemberId()) > 0;
    }

    @Override
    public boolean delete(String memberId) throws SQLException {
        return CrudUtil.executeUpdate("DELETE FROM members WHERE member_id = ?", memberId) > 0;
    }

    @Override
    public Member search(String memberId) throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery("SELECT * FROM members WHERE member_id = ?", memberId)) {
            return rs.next() ? toMember(rs) : null;
        }
    }

    @Override
    public List<Member> getAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        try (ResultSet rs = CrudUtil.executeQuery("SELECT * FROM members ORDER BY member_id")) {
            while (rs.next()) {
                members.add(toMember(rs));
            }
        }
        return members;
    }

    private Member toMember(ResultSet rs) throws SQLException {
        return new Member(rs.getString("member_id"), rs.getString("full_name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("address"));
    }
}
