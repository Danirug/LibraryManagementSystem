package com.library.dao;

import java.sql.SQLException;

public interface UserDAO {
    /** Returns true if a user with this username and password hash exists. */
    boolean exists(String username, String passwordHash) throws SQLException;
}
