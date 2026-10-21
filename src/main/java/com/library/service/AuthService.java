package com.library.service;

import com.library.dao.UserDAO;
import com.library.dao.impl.UserDAOImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HexFormat;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    /** Passwords are stored as SHA-256 hashes, so the typed password is hashed before checking. */
    public boolean login(String username, String password) throws SQLException {
        return userDAO.exists(username, hash(password));
    }

    private static String hash(String password) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
