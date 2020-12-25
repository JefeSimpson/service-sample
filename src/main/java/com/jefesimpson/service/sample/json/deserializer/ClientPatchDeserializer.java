package com.jefesimpson.service.sample.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.jefesimpson.service.sample.model.Client;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class ClientPatchDeserializer extends StdDeserializer<Client> {

    public ClientPatchDeserializer() {
        super(Client.class);
    }

    @Override
    public Client deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String name = root.get(Client.COLUMN_FIRST_NAME).asText();
        String surname = root.get(Client.COLUMN_LAST_NAME).asText();
        String email = root.get(Client.COLUMN_EMAIL).asText();
        String phone = root.get(Client.COLUMN_PHONE).asText();
        String login = root.get(Client.COLUMN_LOGIN).asText();
        String password = root.get(Client.COLUMN_PASSWORD).asText();

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        if(!(root.get(Client.COLUMN_DESTRUCTION_TIME) instanceof NullNode)) {
            LocalDate date = LocalDate.parse(root.get(Client.COLUMN_DESTRUCTION_TIME).asText());
            if (!(root.get(Client.COLUMN_TOKEN) instanceof NullNode)) {
                String token = root.get(Client.COLUMN_TOKEN).asText();
                return new Client(0, name, surname, email, phone, login, passwordHash, token, date);
            } else {
                return new Client(0, name, surname, email, phone, login, passwordHash, null, date);
            }
        } else {
            return new Client(0, name, surname, email, phone, login, passwordHash, null, null);
        }
    }
}
