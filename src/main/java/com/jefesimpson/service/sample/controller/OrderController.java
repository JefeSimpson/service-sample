package com.jefesimpson.service.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.jefesimpson.service.sample.configuration.Constants;
import com.jefesimpson.service.sample.configuration.MapperFactory;
import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.model.Employee;
import com.jefesimpson.service.sample.model.ModelPermission;
import com.jefesimpson.service.sample.model.Order;
import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;
import com.jefesimpson.service.sample.service.Service;
import io.javalin.http.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderController implements AuthorizationController<Order> {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final Service<Order> orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final MapperFactory mapperFactory;

    public OrderController(Service<Order> orderService, ClientService clientService, EmployeeService employeeService, MapperFactory mapperFactory) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.mapperFactory = mapperFactory;
    }

    public Service<Order> getOrderService() {
        return orderService;
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    @Override
    public ClientService clientService() {
        return clientService;
    }

    @Override
    public EmployeeService employeeService() {
        return employeeService;
    }

    @Override
    public void create(Context context) {
        try {
            if (employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                Order target = mapperFactory.objectMapper(ModelPermission.CREATE).readValue(context.body(), Order.class);
                LOGGER.info(String.format("Sender {%s} started to create order {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.CREATE)) {
                    orderService.save(target);
                    LOGGER.info(String.format("Sender {%s} successfully created order {%s} ", employee, target));
                    context.status(Constants.CREATED_201);
                } else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to create order {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            } else {
                if (clientSenderChecker(context)) {
                    Client client = clientSender(context);
                    Order target = mapperFactory.objectMapper(ModelPermission.CREATE).readValue(context.body(), Order.class);
                    LOGGER.info(String.format("Sender {%s} started to create order {%s} ", client, target));
                    if (clientService.permissionsFor(client, target).contains(ModelPermission.CREATE)) {
                        orderService.save(target);
                        LOGGER.info(String.format("Sender {%s} successfully created order {%s} ", client, target));
                        context.status(Constants.CREATED_201);
                    } else {
                        LOGGER.info(String.format("Sender {%s} is not authorized to create order {%s}. Throwing Forbidden", client, target));
                        throw new ForbiddenResponse();
                    }
                } else {
                    throw new UnauthorizedResponse();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void delete(Context context, int id) {
        Employee employee = employeeSenderOrThrowUnauthorized(context);
        Order target = orderService.findById(id);

        LOGGER.info(String.format("Sender {%s} started to delete order {%s} ", employee, target));
        if (employeeService.permissionsFor(employee, target).contains(ModelPermission.DELETE)){
            orderService.deleteById(id);
            LOGGER.info(String.format("Sender {%s} successfully deleted order {%s} ", employee, target));
            context.status(Constants.NO_CONTENT_204);
        }
        else {
            LOGGER.info(String.format("Sender {%s} is not authorized to delete order {%s}. Throwing Forbidden", employee, target));
            throw new ForbiddenResponse();
        }

    }

    @Override
    public void update(Context context, int id) {
        try {
            Employee employee = employeeSenderOrThrowUnauthorized(context);
            Order target = orderService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to update order {%s} ", employee, target));

            if (employeeService.permissionsFor(employee, target).contains(ModelPermission.UPDATE)){
                Order updated = mapperFactory.objectMapper(ModelPermission.UPDATE).readValue(context.body(), Order.class);
                updated.setId(id);
                orderService.update(updated);

                LOGGER.info(String.format("Sender {%s} successfully updated order {%s} ", employee, target));
                context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(updated));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to update order {%s}. Throwing Forbidden", employee, target));
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void getOne(Context context, int id) {

        try {
            if (employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                Order target = orderService.findById(id);
                LOGGER.info(String.format("Sender {%s} started to create order {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.READ)) {
                    LOGGER.info(String.format("Sender {%s} successfully created order {%s} ", employee, target));
                    context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                } else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to create order {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            } else {
                if (clientSenderChecker(context)) {
                    Client client = clientSender(context);
                    Order target = orderService.findById(id);
                    LOGGER.info(String.format("Sender {%s} started to create order {%s} ", client, target));
                    if (clientService.permissionsFor(client, target).contains(ModelPermission.READ)) {
                        LOGGER.info(String.format("Sender {%s} successfully created order {%s} ", client, target));
                        context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                    } else {
                        LOGGER.info(String.format("Sender {%s} is not authorized to create order {%s}. Throwing Forbidden", client, target));
                        throw new ForbiddenResponse();
                    }
                } else {
                    throw new UnauthorizedResponse();
                }
            }
        } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new InternalServerErrorResponse();
        }
    }

    @Override
    public void getAll(Context context) {
        Employee employee = employeeSenderOrThrowUnauthorized(context);
        LOGGER.info(String.format("Sender {%s} started to getAll", employee));
        try {
            List<Order> orders = orderService.all()
                    .stream()
                    .filter(target -> employeeService.permissionsFor(employee, target).contains(ModelPermission.READ))
                    .collect(Collectors.toList());
            LOGGER.info(String.format("Sender {%s} successfully gotAll", employee));
            context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(orders));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }
}
