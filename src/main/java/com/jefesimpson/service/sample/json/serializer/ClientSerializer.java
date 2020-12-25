package com.jefesimpson.service.sample.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jefesimpson.service.sample.model.Client;

import java.io.IOException;

public class ClientSerializer extends StdSerializer<Client> {
    public ClientSerializer() {
        super(Client.class);
    }

    @Override
    public void serialize(Client client, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Client.COLUMN_ID, client.getId());
        jsonGenerator.writeStringField(Client.COLUMN_FIRST_NAME, client.getFirstName());
        jsonGenerator.writeStringField(Client.COLUMN_LAST_NAME, client.getLastName());
        jsonGenerator.writeStringField(Client.COLUMN_EMAIL, client.getEmail());
        jsonGenerator.writeStringField(Client.COLUMN_PHONE, client.getPhone());
        jsonGenerator.writeStringField(Client.COLUMN_LOGIN, client.getLogin());
        jsonGenerator.writeStringField(Client.COLUMN_TOKEN, client.getToken());
        jsonGenerator.writeObjectField(Client.COLUMN_DESTRUCTION_TIME, client.getDestructionTime());
        jsonGenerator.writeEndObject();
    }
}
