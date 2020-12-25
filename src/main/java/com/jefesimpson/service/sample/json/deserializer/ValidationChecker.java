package com.jefesimpson.service.sample.json.deserializer;

import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;

public interface ValidationChecker {
    boolean phoneChecker(String phone);
    boolean emailChecker(String email);
    ClientService clientService();
    EmployeeService employeeService();
}
