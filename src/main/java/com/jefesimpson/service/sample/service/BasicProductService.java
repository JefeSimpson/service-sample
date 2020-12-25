package com.jefesimpson.service.sample.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.jefesimpson.service.sample.configuration.DatabaseUtils;
import com.jefesimpson.service.sample.model.Product;

import java.sql.SQLException;

public class BasicProductService implements Service<Product> {
    @Override
    public Dao<Product, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Product.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }
    }
}
