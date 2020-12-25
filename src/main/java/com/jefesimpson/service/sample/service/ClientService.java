package com.jefesimpson.service.sample.service;

import com.jefesimpson.service.sample.model.*;
import java.util.List;

public interface ClientService extends Service<Client> {
    Client authenticate(String login, String password);
    Client authenticate(String token);
    Client findByLogin(String login);
    Client findByToken(String token);
    boolean loginExist(String login);
    boolean tokenExist(String token);
    boolean isPhoneUnique(String phone);
    boolean isEmailUnique(String email);
    List<ModelPermission> permissionsFor(Client client, Client target);
    List<ModelPermission> permissionsFor(Client client, Order target);
    List<ModelPermission> permissionsFor(Client client, Product target);
}
