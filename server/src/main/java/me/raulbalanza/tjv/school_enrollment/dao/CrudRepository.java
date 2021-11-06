package me.raulbalanza.tjv.school_enrollment.dao;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<K, E> {

    void createOrUpdate(E entity);

    Optional<E> readById(K id);

    Collection<E> readAll();

    void deleteById(K id);

    boolean exists(E entity);

}
