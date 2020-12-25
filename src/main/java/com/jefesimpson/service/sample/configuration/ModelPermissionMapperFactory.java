package com.jefesimpson.service.sample.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jefesimpson.service.sample.model.ModelPermission;

import java.util.Map;

public class ModelPermissionMapperFactory implements MapperFactory {
    private Map<ModelPermission, Module> permission;
    private Map<ModelPermission, Module> patchPermission;



    public ModelPermissionMapperFactory() {}

    public Map<ModelPermission, Module> getPatchPermission() {
        return patchPermission;
    }

    public void setPatchPermission(Map<ModelPermission, Module> patchPermission) {
        this.patchPermission = patchPermission;
    }

    public Map<ModelPermission, Module> getPermission() {
        return permission;
    }

    public void setPermission(Map<ModelPermission, Module> permission) {
        this.permission = permission;
    }

    @Override
    public ObjectMapper objectMapper(ModelPermission modelPermission) {
        ObjectMapper mapper = new ObjectMapper();
        Module module = permission.get(modelPermission);
        mapper.registerModule(module);
        return mapper;
    }

    public ModelPermissionMapperFactory(Map<ModelPermission, Module> permission, Map<ModelPermission, Module> patchPermission) {
        this.permission = permission;
        this.patchPermission = patchPermission;
    }

    @Override
    public ObjectMapper patchObjectMapper(ModelPermission modelPermission) {
        ObjectMapper mapper = new ObjectMapper();
        Module module = patchPermission.get(modelPermission);
        mapper.registerModule(module);
        return mapper;
    }
}
