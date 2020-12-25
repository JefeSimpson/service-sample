package com.jefesimpson.service.sample.service;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface Service <T> {
    default void save(T modelObject) {
        try {
            dao().create(modelObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(String.format("Unable to create record for model %s", modelObject));
        }
    }

    default void update(T modelObject) {
        try {
            dao().update(modelObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(String.format("Unable to update record for model %s", modelObject));
        }
    }

    default T findById(int id) {
        try {
            return dao().queryForId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(String.format("Unable to find record with id %d", id));

        }
    }

    default void deleteById(int id) {
        try {
            dao().deleteById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(String.format("Unable to delete record with id %d", id));
        }
    }

    default List<T> all() {
        try {
            return dao().queryForAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to find all records");
        }
    }

    Dao<T, Integer> dao();
}
