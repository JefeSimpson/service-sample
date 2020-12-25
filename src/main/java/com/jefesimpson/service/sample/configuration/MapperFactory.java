package com.jefesimpson.service.sample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jefesimpson.service.sample.model.ModelPermission;

public interface MapperFactory {
    ObjectMapper objectMapper(ModelPermission modelPermission);
    ObjectMapper patchObjectMapper(ModelPermission modelPermission);
}
