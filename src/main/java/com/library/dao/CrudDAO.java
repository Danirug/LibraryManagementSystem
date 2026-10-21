package com.library.dao;

import java.sql.SQLException;
import java.util.List;

/** The basic Create, Read, Update and Delete operations every DAO provides. */
public interface CrudDAO<T, ID> {
    boolean save(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
    T search(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
}
