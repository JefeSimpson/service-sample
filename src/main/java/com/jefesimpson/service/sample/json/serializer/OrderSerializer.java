package com.jefesimpson.service.sample.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jefesimpson.service.sample.model.Order;

import java.io.IOException;

public class OrderSerializer extends StdSerializer<Order> {
    public OrderSerializer() {
        super(Order.class);
    }

    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Order.COLUMN_ID, order.getId());
        jsonGenerator.writeObjectField(Order.COLUMN_CLIENT_ID, order.getClient());
        jsonGenerator.writeObjectField(Order.COLUMN_PRODUCT_ID, order.getProduct());
        jsonGenerator.writeObjectField(Order.COLUMN_ORDER_DATE, order.getOrderDate());
        jsonGenerator.writeStringField(Order.COLUMN_ADDRESS, order.getAddress());
        jsonGenerator.writeStringField(Order.COLUMN_QUANTITY, order.getQuantity());
        jsonGenerator.writeStringField(Order.COLUMN_PRICE, order.getPrice());
        jsonGenerator.writeStringField(Order.COLUMN_STATUS, order.getStatus().name());
        jsonGenerator.writeEndObject();
    }
}
