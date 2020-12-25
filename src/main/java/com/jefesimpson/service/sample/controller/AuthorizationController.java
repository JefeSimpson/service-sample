package com.jefesimpson.service.sample.controller;

import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.model.Employee;
import com.jefesimpson.service.sample.service.BasicClientService;
import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

import java.util.Map;


public interface AuthorizationController <T> extends Controller <T> {
    ClientService clientService();
    EmployeeService employeeService();

    default Client clientSender(Context context) {
        if (!context.basicAuthCredentialsExist()) {
            String authorization = context.headerMap().get("Authorization");
            if (authorization != null) {
                String token = authorization.substring(SUBSTRING_INT, SUBSTRING_INT_END);
                return clientService().authenticate(token);
            }
            return null;
        }
        String login = context.basicAuthCredentials().getUsername();
        String password = context.basicAuthCredentials().getPassword();
        return clientService().authenticate(login, password);
    }

    default Employee employeeSender(Context context) {
        if (!context.basicAuthCredentialsExist()) {
            String authorization = context.headerMap().get("Authorization");
            if (authorization != null) {
                String token = authorization.substring(SUBSTRING_INT, SUBSTRING_INT_END);
                return employeeService().authenticate(token);
            }
            return null;
        }
        String login = context.basicAuthCredentials().getUsername();
        String password = context.basicAuthCredentials().getPassword();
        return employeeService().authenticate(login, password);
    }

    default boolean clientSenderChecker(Context context) {
        return clientSender(context) != null;
    }

    default boolean employeeSenderChecker(Context context) {
        return employeeSender(context) != null;
    }

    default Client clientSenderOrThrowUnauthorized(Context context) {
        Client client = clientSender(context);
        if (client == null) {
            throw new UnauthorizedResponse();
        }
        return client;
    }

    default Employee employeeSenderOrThrowUnauthorized(Context context) {
        Employee employee = employeeSender(context);
        if (employee == null) {
            throw new UnauthorizedResponse();
        }
        return employee;
    }

    static int SUBSTRING_INT = 7;
    static int SUBSTRING_INT_END = 39;
}
