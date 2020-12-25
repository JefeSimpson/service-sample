package com.jefesimpson.service.sample.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.jefesimpson.service.sample.configuration.DatabaseUtils;
import com.jefesimpson.service.sample.model.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BasicClientService implements ClientService{
    private final static Logger LOGGER = LoggerFactory.getLogger(BasicClientService.class);

    @Override
    public Client authenticate(String login, String password) {
        LOGGER.info("Started Basic auth checking");
        if (!loginExist(login)) {
            throw new RuntimeException("login not exists");
        }
        Client client = findByLogin(login);
        if (BCrypt.checkpw(password, client.getPassword())) {
            if (client.getToken() == null) {
                LOGGER.info("Check ended, returning employee");
                return client;
            } else {
                LOGGER.info("Check failed, token exists");
                return null;
            }
        }
        else {
            LOGGER.info("Check failed, password incorrect");
            return null;
        }
    }

    @Override
    public Client authenticate(String token) {
        LOGGER.info("Started Basic token checking");
        if (!tokenExist(token)) {
            throw new RuntimeException("token not exists");
        }
        Client client = findByToken(token);
        if (LocalDate.now().isBefore(client.getDestructionTime())) {
            LOGGER.info("Check ended, returning employee");
            return client;
        }
        else {
            LOGGER.info("Check failed, please update token date");
            return null;
        }
    }

    @Override
    public Client findByLogin(String login) {
        try {
            QueryBuilder<Client, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Client.COLUMN_LOGIN, login);
            PreparedQuery<Client> preparedQuery = queryBuilder.prepare();
            Client client = dao().queryForFirst(preparedQuery);
            return client;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SQL exception occurred");
        }
    }

    @Override
    public Client findByToken(String token) {
        try {
            QueryBuilder<Client, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Client.COLUMN_TOKEN, token);
            PreparedQuery<Client> preparedQuery = queryBuilder.prepare();
            Client client = dao().queryForFirst(preparedQuery);
            return client;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SQL exception occurred");
        }
    }

    @Override
    public boolean loginExist(String login) {
        return findByLogin(login) != null;
    }

    @Override
    public boolean tokenExist(String token) {
        return findByToken(token) != null;
    }

    @Override
    public boolean isPhoneUnique(String phone) {
        try {
            LOGGER.info("Started phone unique checking");
            QueryBuilder<Client, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Client.COLUMN_PHONE, phone);
            PreparedQuery<Client> preparedQuery = queryBuilder.prepare();
            Client client = dao().queryForFirst(preparedQuery);
            if (client == null) {
                LOGGER.info("Phone is unique, returning true");
                return true;
            } else {
                LOGGER.info("Phone isn't unique, returning false");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("dao exception");
        }
    }

    @Override
    public boolean isEmailUnique(String email) {
        try {
            LOGGER.info("Started email unique checking");
            QueryBuilder<Client, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Client.COLUMN_EMAIL, email);
            PreparedQuery<Client> preparedQuery = queryBuilder.prepare();
            Client client = dao().queryForFirst(preparedQuery);
            if (client == null) {
                LOGGER.info("Email is unique, returning true");
                return true;
            } else {
                LOGGER.info("Email isn't unique, returning false");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("dao exception");
        }
    }

    @Override
    public List<ModelPermission> permissionsFor(Client client, Client target) {
        List<ModelPermission> permissions = new ArrayList<>();
        if (client == null) {
            permissions.add(ModelPermission.CREATE);
            return permissions;
        }
        if (client.getId() == target.getId()) {
            permissions.addAll(Arrays.stream(ModelPermission.values()).filter(permission -> permission != ModelPermission.CREATE).collect(Collectors.toSet()));
        }
        return permissions;
    }

    @Override
    public List<ModelPermission> permissionsFor(Client client, Order target) {
        List<ModelPermission> permissions = new ArrayList<>();
        if (client.getId() == target.getId()) {
            permissions.add(ModelPermission.CREATE);
            permissions.add(ModelPermission.READ);
        }
        return permissions;
    }

    @Override
    public List<ModelPermission> permissionsFor(Client client, Product target) {
        List<ModelPermission> permissions = new ArrayList<>();
        if (client != null) {
            permissions.add(ModelPermission.READ);
        }
        return permissions;
    }

    @Override
    public Dao<Client, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Client.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }
    }
}
