package com.jefesimpson.service.sample.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jefesimpson.service.sample.model.Employee;

import java.io.IOException;

public class EmployeeSerializer extends StdSerializer<Employee> {
    public EmployeeSerializer() {
        super(Employee.class);
    }

    @Override
    public void serialize(Employee employee, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Employee.COLUMN_ID, employee.getId());
        jsonGenerator.writeStringField(Employee.COLUMN_FIRST_NAME, employee.getFirstName());
        jsonGenerator.writeStringField(Employee.COLUMN_LAST_NAME, employee.getLastName());
        jsonGenerator.writeStringField(Employee.COLUMN_DEPARTMENT, employee.getDepartment().name());
        jsonGenerator.writeStringField(Employee.COLUMN_LOGIN, employee.getLogin());
        jsonGenerator.writeStringField(Employee.COLUMN_TOKEN, employee.getToken());
        jsonGenerator.writeObjectField(Employee.COLUMN_DESTRUCTION_TIME, employee.getDestructionTime());
        jsonGenerator.writeEndObject();
    }
}