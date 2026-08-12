package com.officine.losto.service;

import java.util.*;

public interface IService<T> {
    List<T> getAll();

    T loadById(long id);

    T save(T t);

    List<T> saveAll(List<T> ts);

    T saveAndFlush(T t);

    List<T> saveAllAndFlush(List<T> ts);

    void remove(T t);

    T update(T t);
}
