package com.jefesimpson.service.sample.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jefesimpson.service.sample.model.Product;

import java.io.IOException;

public class ProductSerializer extends StdSerializer<Product> {
    public ProductSerializer() {
        super(Product.class);
    }

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Product.COLUMN_ID, product.getId());
        jsonGenerator.writeStringField(Product.COLUMN_PRODUCT_NAME, product.getProductName());
        jsonGenerator.writeStringField(Product.COLUMN_DESCRIPTION, product.getDescription());
        jsonGenerator.writeObjectField(Product.COLUMN_EMPLOYEE_ID, product.getEmployee());
        jsonGenerator.writeEndObject();
    }
}
