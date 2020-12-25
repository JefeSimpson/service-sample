package com.jefesimpson.service.sample;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jefesimpson.service.sample.configuration.ModelPermissionMapperFactory;
import com.jefesimpson.service.sample.controller.*;
import com.jefesimpson.service.sample.json.deserializer.*;
import com.jefesimpson.service.sample.json.serializer.ClientSerializer;
import com.jefesimpson.service.sample.json.serializer.EmployeeSerializer;
import com.jefesimpson.service.sample.json.serializer.OrderSerializer;
import com.jefesimpson.service.sample.json.serializer.ProductSerializer;
import com.jefesimpson.service.sample.model.*;
import com.jefesimpson.service.sample.service.*;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.defaultContentType = "application/json";
        });


        ClientService clientService = new BasicClientService();
        EmployeeService employeeService = new BasicEmployeeService();
        Service<Order> orderService = new BasicOrderService();
        Service<Product> productService = new BasicProductService();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Client.class, new ClientDeserializer(clientService))
                .addDeserializer(Employee.class, new EmployeeDeserializer(employeeService))
                .addDeserializer(Order.class, new OrderDeserializer())
                .addDeserializer(Product.class, new ProductDeserializer())
                .addSerializer(Client.class, new ClientSerializer())
                .addSerializer(Employee.class, new EmployeeSerializer())
                .addSerializer(Order.class, new OrderSerializer())
                .addSerializer(Product.class, new ProductSerializer());

        SimpleModule patchModule = new SimpleModule();
        patchModule.addDeserializer(Client.class, new ClientPatchDeserializer())
                .addDeserializer(Employee.class, new EmployeePatchDeserializer());

        Map<ModelPermission, Module> patchPermission = new HashMap<>();
        patchPermission.put(ModelPermission.UPDATE, patchModule);

        Map<ModelPermission, Module> permission = new HashMap<>();
        permission.put(ModelPermission.READ, module);
        permission.put(ModelPermission.CREATE, module);
        permission.put(ModelPermission.UPDATE, module);
        ModelPermissionMapperFactory modelPermissionMapperFactory = new ModelPermissionMapperFactory(permission, patchPermission);

        Controller<Client> clientController = new ClientController(clientService, employeeService, modelPermissionMapperFactory);
        Controller<Employee> employeeController = new EmployeeController(employeeService, clientService, modelPermissionMapperFactory);
        Controller<Order> orderController = new OrderController(orderService, clientService, employeeService, modelPermissionMapperFactory);
        Controller<Product> productController = new ProductController(productService, clientService, employeeService, modelPermissionMapperFactory);


        app.routes(() -> {
            path("clients", () -> {
                get(clientController::getAll);
                post(clientController::create);
                path(id, () -> {
                    get(ctx -> clientController.getOne(ctx,ctx.pathParam(id, Integer.class).get()));
                    patch(ctx -> clientController.update(ctx, ctx.pathParam(id, Integer.class).get()));
                    delete(ctx -> clientController.delete(ctx, ctx.pathParam(id, Integer.class).get()));
                });
            });
            path("employees", () -> {
                get(employeeController::getAll);
                post(employeeController::create);
                path(id, () -> {
                    get(ctx -> employeeController.getOne(ctx, ctx.pathParam(id, Integer.class).get()));
                    patch(ctx -> employeeController.update(ctx, ctx.pathParam(id, Integer.class).get()));
                    delete(ctx -> employeeController.delete(ctx, ctx.pathParam(id, Integer.class).get()));
                });
            });
            path("orders", () -> {
                get(orderController::getAll);
                post(orderController::create);
                path(id, () -> {
                    get(ctx -> orderController.getOne(ctx, ctx.pathParam(id, Integer.class).get()));
                    patch(ctx -> orderController.update(ctx, ctx.pathParam(id, Integer.class).get()));
                    delete(ctx -> orderController.delete(ctx, ctx.pathParam(id, Integer.class).get()));
                });
            });
            path("products", () -> {
                get(productController::getAll);
                post(productController::create);
                path(id, () -> {
                    get(ctx -> productController.getOne(ctx, ctx.pathParam(id, Integer.class).get()));
                    patch(ctx -> productController.update(ctx, ctx.pathParam(id, Integer.class).get()));
                    delete(ctx -> productController.delete(ctx, ctx.pathParam(id, Integer.class).get()));
                });
            });
        });


        app.start(9950);

    }
    static String id = ":id";
}
