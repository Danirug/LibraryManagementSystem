package com.library.dao.impl;

import com.library.dao.CrudUtil;
import com.library.dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean exists(String username, String passwordHash) throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery(
                "SELECT 1 FROM users WHERE username = ? AND password = ?", username, passwordHash)) {
            return rs.next();
        }
    }
}
