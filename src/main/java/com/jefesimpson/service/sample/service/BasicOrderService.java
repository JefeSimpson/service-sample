package com.jefesimpson.service.sample.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.jefesimpson.service.sample.configuration.DatabaseUtils;
import com.jefesimpson.service.sample.model.Order;

import java.sql.SQLException;

public class BasicOrderService implements Service<Order> {
    @Override
    public Dao<Order, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Order.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }
    }
}
