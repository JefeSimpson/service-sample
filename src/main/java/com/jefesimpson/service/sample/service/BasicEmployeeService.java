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

public class BasicEmployeeService implements EmployeeService {
    private final static Logger LOGGER = LoggerFactory.getLogger(BasicEmployeeService.class);

    @Override
    public Employee authenticate(String login, String password) {
        LOGGER.info("Started checking");
        if (!loginExist(login)) {
            LOGGER.info("login not exists, so throwing null");
            return null;
        }
        Employee employee = findByLogin(login);
        if (BCrypt.checkpw(password, employee.getPassword())) {
            if (employee.getToken() == null) {
                LOGGER.info("Check ended, returning employee");
                return employee;
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
    public Employee authenticate(String token) {
        LOGGER.info("Started checking");
        if (!tokenExist(token)) {
            LOGGER.info("token not exists, so returning null");
            return null;
        }
        Employee employee = findByToken(token);
        if (LocalDate.now().isBefore(employee.getDestructionTime())) {
            LOGGER.info("Check ended, returning employee");
            return employee;
        }
        else {
            employee.setToken(null);
            try {
                dao().update(employee);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            LOGGER.info("Check failed, please update token date");
            return null;
        }
    }

    @Override
    public Employee findByLogin(String login) {
        try {
            QueryBuilder<Employee, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Employee.COLUMN_LOGIN, login);
            PreparedQuery<Employee> preparedQuery = queryBuilder.prepare();
            Employee employee = dao().queryForFirst(preparedQuery);
            return employee;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SQL exception occurred");
        }
    }

    @Override
    public Employee findByToken(String token) {
        try {
            QueryBuilder<Employee, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Employee.COLUMN_TOKEN, token);
            PreparedQuery<Employee> preparedQuery = queryBuilder.prepare();
            Employee employee = dao().queryForFirst(preparedQuery);
            return employee;
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
            QueryBuilder<Employee, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Employee.COLUMN_PHONE, phone);
            PreparedQuery<Employee> preparedQuery = queryBuilder.prepare();
            Employee employee = dao().queryForFirst(preparedQuery);
            if (employee == null) {
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
            QueryBuilder<Employee, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq(Employee.COLUMN_EMAIL, email);
            PreparedQuery<Employee> preparedQuery = queryBuilder.prepare();
            Employee employee = dao().queryForFirst(preparedQuery);
            if (employee == null) {
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
    public List<ModelPermission> permissionsFor(Employee employee, Employee target) {
        if (employee.getDepartment() == EmployeeDepartment.MANAGEMENT) {
            List<ModelPermission> permissions = new ArrayList<>(Arrays.asList(ModelPermission.values()));
            return permissions;
        }
        return null;
    }

    @Override
    public List<ModelPermission> permissionsFor(Employee employee, Client target) {
        if (employee.getDepartment() == EmployeeDepartment.SALES) {
            List<ModelPermission> permissions = new ArrayList<>(Arrays.asList(ModelPermission.values()));
            return permissions;
        }
        return null;
    }

    @Override
    public List<ModelPermission> permissionsFor(Employee employee, Order target) {
        if (employee.getDepartment() == EmployeeDepartment.SALES) {
            List<ModelPermission> permissions = new ArrayList<>(Arrays.asList(ModelPermission.values()));
            return permissions;
        }
        if (employee.getDepartment() == EmployeeDepartment.PRODUCTION) {
            List<ModelPermission> permissions = new ArrayList<>(Arrays.asList(ModelPermission.READ));
            return permissions;
        }
        return null;
    }

    @Override
    public List<ModelPermission> permissionsFor(Employee employee, Product target) {
        if (employee.getDepartment() == EmployeeDepartment.PRODUCTION) {
            List<ModelPermission> permissions = new ArrayList<>(Arrays.asList(ModelPermission.values()));
            return permissions;
        }
        return null;
    }

    @Override
    public Dao<Employee, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Employee.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }
    }
}
