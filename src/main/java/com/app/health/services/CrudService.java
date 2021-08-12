package com.app.health.services;

import java.util.List;

public interface CrudService<T> {
    String add(T t);

    List<T> getAll();
}
