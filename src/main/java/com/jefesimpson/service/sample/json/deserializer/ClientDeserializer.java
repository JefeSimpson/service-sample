package com.jefesimpson.service.sample.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.jefesimpson.service.sample.model.Client;
import com.jefesimpson.service.sample.service.ClientService;
import com.jefesimpson.service.sample.service.EmployeeService;
import io.javalin.http.BadRequestResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDeserializer extends StdDeserializer<Client> implements ValidationChecker {
    public ClientDeserializer(ClientService clientService) {
        super(Client.class);
        this.clientService = clientService;
    }
    private final ClientService clientService;
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

        if(phoneChecker(phone) && emailChecker(email)) {
            if (!(root.get(Client.COLUMN_DESTRUCTION_TIME) instanceof NullNode)) {
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
        else {
            throw new BadRequestResponse();
        }

    }

    @Override
    public boolean phoneChecker(String phone) {
        if(clientService.isPhoneUnique(phone)) {
            if(phone.charAt(0) == '8' && phone.charAt(1) == '7' && phone.matches("[0-9]+") && phone.length() == 11) {
                return true;
            }
            else {
                throw new BadRequestResponse("phone isn't valid. Try to write right");
            }
        }
        else {
            throw new BadRequestResponse("phone isn't unique. Use another phone");
        }
    }

    @Override
    public boolean emailChecker(String email) {
        if(clientService.isEmailUnique(email)) {
            String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
            Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = emailPat.matcher(email);
            return matcher.find();
        }
        else {
            throw new BadRequestResponse("email isn't unique. Use another email");
        }
    }


    @Override
    public ClientService clientService() {
        return clientService;
    }

    @Override
    public EmployeeService employeeService() {
        return null;
    }
}
