package com.library.dao;

import com.library.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Runs parameterised SQL so every DAO doesn't repeat the PreparedStatement boilerplate. */
public final class CrudUtil {

    private CrudUtil() { }

    private static PreparedStatement prepare(String sql, Object... args) throws SQLException {
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            stm.setObject(i + 1, args[i]);
        }
        return stm;
    }

    /** For INSERT, UPDATE and DELETE. Returns the number of rows changed. */
    public static int executeUpdate(String sql, Object... args) throws SQLException {
        try (PreparedStatement stm = prepare(sql, args)) {
            return stm.executeUpdate();
        }
    }

    /** For SELECT. The caller must close the ResultSet (closing it also closes its statement). */
    public static ResultSet executeQuery(String sql, Object... args) throws SQLException {
        PreparedStatement stm = prepare(sql, args);
        stm.closeOnCompletion();
        return stm.executeQuery();
    }
}
