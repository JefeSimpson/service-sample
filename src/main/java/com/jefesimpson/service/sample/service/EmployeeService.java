package com.jefesimpson.service.sample.service;

import com.jefesimpson.service.sample.model.*;
import java.util.List;

public interface EmployeeService extends Service<Employee> {
    Employee authenticate(String login, String password);
    Employee authenticate(String token);
    Employee findByLogin(String login);
    Employee findByToken(String token);
    boolean loginExist(String login);
    boolean tokenExist(String token);
    boolean isPhoneUnique(String phone);
    boolean isEmailUnique(String email);
    List<ModelPermission> permissionsFor(Employee employee, Employee target);
    List<ModelPermission> permissionsFor(Employee employee, Client target);
    List<ModelPermission> permissionsFor(Employee employee, Order target);
    List<ModelPermission> permissionsFor(Employee employee, Product target);
}
