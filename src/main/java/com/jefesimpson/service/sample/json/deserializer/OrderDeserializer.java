package com.jefesimpson.service.sample.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.jefesimpson.service.sample.configuration.DatabaseUtils;
import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.model.Order;
import com.jefesimpson.service.sample.model.OrderStatus;
import com.jefesimpson.service.sample.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderDeserializer extends StdDeserializer<Order> implements DaoDeserializer<Client, Product>{
    public OrderDeserializer() {
        super(Order.class);
    }

    @Override
    public Order deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        int clientId = root.get(Order.COLUMN_CLIENT_ID).asInt();
        int productId = root.get(Order.COLUMN_PRODUCT_ID).asInt();
        LocalDate date = LocalDate.parse(root.get(Order.COLUMN_ORDER_DATE).asText());
        String address = root.get(Order.COLUMN_ADDRESS).asText();
        String quantity = root.get(Order.COLUMN_QUANTITY).asText();
        String price = root.get(Order.COLUMN_PRICE).asText();
        OrderStatus status = OrderStatus.valueOf(root.get(Order.COLUMN_STATUS).asText());

        Client client = null;
        try {
            client = daoOne().queryForId(clientId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Product product = null;
        try {
            product = daoTwo().queryForId(productId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new Order(0, client, product, date, address, quantity, price, status);
    }

    @Override
    public Dao<Client, Integer> daoOne() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Client.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }

    }

    @Override
    public Dao<Product, Integer> daoTwo() {
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Product.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Unable to create dao for Post model");
        }
    }
}
