package me.heymrau.seniorparkour.storage.repository;

import java.util.Collection;
import java.util.List;

public interface Repository<T> {

    Collection<T> getAll();

    void save(T data);

    void saveAll(List<T> data);
}
