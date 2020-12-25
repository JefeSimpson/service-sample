package com.jefesimpson.service.sample.controller;

import io.javalin.http.Context;

public interface Controller <T> {
    void create(Context context);
    void delete(Context context, int id);
    void update(Context context, int id);
    void getOne(Context context, int id);
    void getAll(Context context);

}
