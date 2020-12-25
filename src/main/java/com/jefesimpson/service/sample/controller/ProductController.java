package com.jefesimpson.service.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.jefesimpson.service.sample.configuration.Constants;
import com.jefesimpson.service.sample.configuration.MapperFactory;
import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.model.Employee;
import com.jefesimpson.service.sample.model.ModelPermission;
import com.jefesimpson.service.sample.model.Product;
import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;
import com.jefesimpson.service.sample.service.Service;
import io.javalin.http.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductController implements AuthorizationController<Product> {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final Service<Product> productService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final MapperFactory mapperFactory;

    public ProductController(Service<Product> productService, ClientService clientService, EmployeeService employeeService, MapperFactory mapperFactory) {
        this.productService = productService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.mapperFactory = mapperFactory;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public Service<Product> getProductService() {
        return productService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
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
            Employee employee = employeeSenderOrThrowUnauthorized(context);
            Product target = mapperFactory.objectMapper(ModelPermission.CREATE).readValue(context.body(), Product.class);

            LOGGER.info(String.format("Sender {%s} started to create product {%s} ", employee, target));
            if(employeeService.permissionsFor(employee, target).contains(ModelPermission.CREATE)){
                productService.save(target);
                LOGGER.info(String.format("Sender {%s} successfully created product {%s} ", employee, target));
                context.status(Constants.CREATED_201);
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to create product {%s}. Throwing Forbidden", employee, target));
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void delete(Context context, int id) {
        Employee employee = employeeSenderOrThrowUnauthorized(context);
        Product target = productService.findById(id);

        LOGGER.info(String.format("Sender {%s} started to delete product {%s} ", employee, target));
        if (employeeService.permissionsFor(employee, target).contains(ModelPermission.DELETE)){
            productService.deleteById(id);
            LOGGER.info(String.format("Sender {%s} successfully deleted product {%s} ", employee, target));
            context.status(Constants.NO_CONTENT_204);
        }
        else {
            LOGGER.info(String.format("Sender {%s} is not authorized to delete product {%s}. Throwing Forbidden", employee, target));
            throw new ForbiddenResponse();
        }

    }

    @Override
    public void update(Context context, int id) {
        try {
            Employee employee = employeeSenderOrThrowUnauthorized(context);
            Product target = productService.findById(id);

            LOGGER.info(String.format("Sender {%s} started to update product {%s} ", employee, target));
            if (employeeService.permissionsFor(employee, target).contains(ModelPermission.UPDATE)){
                Product updated = mapperFactory.objectMapper(ModelPermission.UPDATE).readValue(context.body(), Product.class);
                updated.setId(id);
                productService.update(updated);

                LOGGER.info(String.format("Sender {%s} successfully updated product {%s} ", employee, target));
                context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(updated));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to update product {%s}. Throwing Forbidden", employee, target));
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
                Product target = productService.findById(id);
                LOGGER.info(String.format("Sender {%s} started to getOne product {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.READ)) {
                    LOGGER.info(String.format("Sender {%s} successfully gotOne product {%s} ", employee, target));
                    context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                } else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to getOne product {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            }
            else {
                if(clientSenderChecker(context)) {
                    Client client = clientSender(context);
                    Product target = productService.findById(id);
                    LOGGER.info(String.format("Sender {%s} started to getOne product {%s} ", client, target));
                    if (clientService.permissionsFor(client, target).contains(ModelPermission.READ)) {
                        LOGGER.info(String.format("Sender {%s} successfully gotOne product {%s} ", client, target));
                        context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                    } else {
                        LOGGER.info(String.format("Sender {%s} is not authorized to getOne product {%s}. Throwing Forbidden", client, target));
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
        try {
            if (employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                LOGGER.info(String.format("Sender {%s} started to getAll", employee));
                List<Product> products = productService.all()
                        .stream()
                        .filter(target -> employeeService.permissionsFor(employee, target).contains(ModelPermission.READ))
                        .collect(Collectors.toList());
                LOGGER.info(String.format("Sender {%s} successfully gotAll", employee));
                context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(products));
            } else {
                Client client = clientSender(context);
                if (clientSenderChecker(context)) {
                    LOGGER.info(String.format("Sender {%s} started to getAll", client));
                    List<Product> products = productService.all()
                            .stream()
                            .filter(target -> clientService.permissionsFor(client, target).contains(ModelPermission.READ))
                            .collect(Collectors.toList());
                    LOGGER.info(String.format("Sender {%s} successfully gotAll", client));
                    context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(products));
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }
}
