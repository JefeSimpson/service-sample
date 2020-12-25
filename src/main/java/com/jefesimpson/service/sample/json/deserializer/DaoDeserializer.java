package com.jefesimpson.service.sample.json.deserializer;

import com.j256.ormlite.dao.Dao;

public interface DaoDeserializer<T, S> {
    Dao<T, Integer> daoOne();
    Dao<S, Integer> daoTwo();
}
