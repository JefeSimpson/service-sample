package com.jefesimpson.service.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.jefesimpson.service.sample.configuration.Constants;
import com.jefesimpson.service.sample.configuration.MapperFactory;
import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.model.Employee;
import com.jefesimpson.service.sample.model.ModelPermission;
import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import io.javalin.http.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController implements AuthorizationController<Client> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final MapperFactory mapperFactory;

    public ClientController(ClientService clientService, EmployeeService employeeService, MapperFactory mapperFactory) {
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.mapperFactory = mapperFactory;
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
            if (employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                Client target = mapperFactory.objectMapper(ModelPermission.CREATE).readValue(context.body(), Client.class);
                LOGGER.info(String.format("Sender {%s} started to create client {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.CREATE)) {
                    clientService.save(target);
                    LOGGER.info(String.format("Sender {%s} successfully created client {%s} ", employee, target));
                    context.status(Constants.CREATED_201);
                }
                else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to create client {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            }
            else {
                Client client = clientSender(context);
                Client target = mapperFactory.objectMapper(ModelPermission.CREATE).readValue(context.body(), Client.class);
                if (clientSenderChecker(context)) {
                    LOGGER.info(String.format("Sender {%s} is authorized to create client {%s}. Throwing Forbidden", client, target));
                    throw new ForbiddenResponse();
                }
                LOGGER.info(String.format("Sender {%s} started to create client {%s} ", client, target));
                if (clientService.permissionsFor(client, target).contains(ModelPermission.CREATE)) {
                    clientService.save(target);
                    LOGGER.info(String.format("Sender {%s} successfully created client {%s} ", client, target));
                    context.status(Constants.CREATED_201);
                }
                else {
                    LOGGER.info(String.format("Sender {%s} is authorized to create client {%s}. Throwing Forbidden", client, target));
                    throw new ForbiddenResponse();
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void delete(Context context, int id) {
        if (employeeSenderChecker(context)) {
            Employee employee = employeeSender(context);
            Client target = clientService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to delete client {%s} ", employee, target));
            if (employeeService.permissionsFor(employee, target).contains(ModelPermission.DELETE)) {
                clientService.deleteById(id);
                LOGGER.info(String.format("Sender {%s} successfully deleted client {%s} ", employee, target));
                context.status(Constants.NO_CONTENT_204);
            }
            else {
                LOGGER.info(String.format("Sender {%s} is not authorized to delete client {%s}. Throwing Forbidden", employee, target));
                throw new ForbiddenResponse();
            }
        }
        else {
            if (clientSenderChecker(context)) {
                Client client = clientSender(context);
                Client target = clientService.findById(id);
                LOGGER.info(String.format("Sender {%s} started to delete client {%s} ", client, target));
                if (clientService.permissionsFor(client, target).contains(ModelPermission.DELETE)) {
                    clientService.deleteById(id);
                    LOGGER.info(String.format("Sender {%s} successfully deleted client {%s} ", client, target));
                    context.status(Constants.NO_CONTENT_204);
                }
                else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to delete client {%s}. Throwing Forbidden", client, target));
                    throw new ForbiddenResponse();
                }
            }
            else {
                throw new UnauthorizedResponse();
            }
        }
    }

    @Override
    public void update(Context context, int id) {
        try {
            if (employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                Client target = clientService.findById(id);
                LOGGER.info(String.format("Sender {%s} started to update client {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.UPDATE)) {
                    context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(target));

                    Client updated = mapperFactory.patchObjectMapper(ModelPermission.UPDATE).readValue(context.body(), Client.class);
                    if (updated.getDestructionTime() != null && updated.getToken() == null) {
                        if (LocalDate.now().isBefore(updated.getDestructionTime())) {
                            SecretGenerator secretGenerator = new DefaultSecretGenerator();
                            String secret = secretGenerator.generate();
                            updated.setToken(secret);
                        } else {
                            updated.setToken(null);
                        }
                    }
                    updated.setId(id);
                    clientService.update(updated);
                    LOGGER.info(String.format("Sender {%s} successfully updated client {%s} ", employee, target));
                    context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(updated));
                }
                else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to update client {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            }
            else {
                if (clientSenderChecker(context)) {
                    Client client = clientSender(context);
                    Client target = clientService.findById(id);
                    LOGGER.info(String.format("Sender {%s} started to update client {%s} ", client, target));
                    if (clientService.permissionsFor(client, target).contains(ModelPermission.UPDATE)) {
                        context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(target));

                        Client updated = mapperFactory.patchObjectMapper(ModelPermission.UPDATE).readValue(context.body(), Client.class);
                        if (updated.getDestructionTime() != null && updated.getToken() == null) {
                            if (LocalDate.now().isBefore(updated.getDestructionTime())) {
                                SecretGenerator secretGenerator = new DefaultSecretGenerator();
                                String secret = secretGenerator.generate();
                                updated.setToken(secret);
                            } else {
                                updated.setToken(null);
                            }
                        }
                        updated.setId(id);
                        clientService.update(updated);
                        LOGGER.info(String.format("Sender {%s} successfully updated client {%s} ", client, target));
                        context.result(mapperFactory.objectMapper(ModelPermission.UPDATE).writeValueAsString(updated));
                    }
                    else {
                        LOGGER.info(String.format("Sender {%s} is not authorized to update client {%s}. Throwing Forbidden", client, target));
                        throw new ForbiddenResponse();
                    }
                }
                else {
                    throw new UnauthorizedResponse();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void getOne(Context context, int id) {
        try {
            if(employeeSenderChecker(context)) {
                Employee employee = employeeSender(context);
                Client target = clientService.findById(id);
                LOGGER.info(String.format("Sender {%s} started to getOne client {%s} ", employee, target));
                if (employeeService.permissionsFor(employee, target).contains(ModelPermission.READ)) {
                    LOGGER.info(String.format("Sender {%s} successfully gotOne client {%s} ", employee, target));
                    context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                }
                else {
                    LOGGER.info(String.format("Sender {%s} is not authorized to getOne client {%s}. Throwing Forbidden", employee, target));
                    throw new ForbiddenResponse();
                }
            }
            else {
                if(clientSenderChecker(context)) {
                    Client client = clientSender(context);
                    Client target = clientService.findById(id);
                    LOGGER.info(String.format("Sender {%s} started to getOne client {%s} ", client, target));
                    if (clientService.permissionsFor(client, target).contains(ModelPermission.READ)) {
                        LOGGER.info(String.format("Sender {%s} successfully gotOne client {%s} ", client, target));
                        context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(target));
                    }
                    else {
                        LOGGER.info(String.format("Sender {%s} is not authorized to getOne client {%s}. Throwing Forbidden", client, target));
                        throw new ForbiddenResponse();
                    }
                }
                else {
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

                List<Client> clients = clientService.all()
                        .stream()
                        .filter(target -> employeeService.permissionsFor(employee, target).contains(ModelPermission.READ))
                        .collect(Collectors.toList());
                LOGGER.info(String.format("Sender {%s} successfully gotAll", employee));
                context.result(mapperFactory.objectMapper(ModelPermission.READ).writeValueAsString(clients));
            } else {
                throw new UnauthorizedResponse();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }
}
