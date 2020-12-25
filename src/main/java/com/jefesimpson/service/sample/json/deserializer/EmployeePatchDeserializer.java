package com.jefesimpson.service.sample.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.jefesimpson.service.sample.model.Employee;
import com.jefesimpson.service.sample.model.EmployeeDepartment;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class EmployeePatchDeserializer extends StdDeserializer<Employee> {
    public EmployeePatchDeserializer() {
        super(Employee.class);
    }

    @Override
    public Employee deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String name = root.get(Employee.COLUMN_FIRST_NAME).asText();
        String surname = root.get(Employee.COLUMN_LAST_NAME).asText();
        String email = root.get(Employee.COLUMN_EMAIL).asText();
        String phone = root.get(Employee.COLUMN_PHONE).asText();
        EmployeeDepartment department = EmployeeDepartment.valueOf(root.get(Employee.COLUMN_DEPARTMENT).asText());
        String login = root.get(Employee.COLUMN_LOGIN).asText();
        String password = root.get(Employee.COLUMN_PASSWORD).asText();

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        if(!(root.get(Employee.COLUMN_DESTRUCTION_TIME) instanceof NullNode)) {
            LocalDate date = LocalDate.parse(root.get(Employee.COLUMN_DESTRUCTION_TIME).asText());
            if (!(root.get(Employee.COLUMN_TOKEN) instanceof NullNode)) {
                String token = root.get(Employee.COLUMN_TOKEN).asText();
                return new Employee(0, name, surname, email, phone, department, login, passwordHash, token, date);
            } else {
                return new Employee(0, name, surname, email, phone, department, login, passwordHash, null, date);
            }
        } else {
            return new Employee(0, name, surname, email, phone, department, login, passwordHash, null, null);
        }
    }
}
