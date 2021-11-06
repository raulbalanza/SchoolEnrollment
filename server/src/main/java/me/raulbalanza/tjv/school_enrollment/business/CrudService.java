package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CrudRepository;
import java.util.Collection;
import java.util.Optional;

/**
 * Common superclass for business logic of all entities supporting operations Create, Read, Update, Delete.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */
public abstract class CrudService<K, E> {

    protected final CrudRepository<K, E> repository; // Data layer

    protected CrudService(CrudRepository<K, E> repository) {
        this.repository = repository;
    }

    public void create(E entity) throws EntityStateException {
        if (repository.exists(entity))
            throw new EntityStateException(entity);
        repository.createOrUpdate(entity);
    }

    public E readById(K id) throws UnknownEntityException {
        Optional<E> entity = repository.readById(id);
        if (entity.isPresent()){
            return entity.get();
        } else {
            throw new UnknownEntityException(id);
        }
    }

    public Collection<E> readAll() {
        return repository.readAll();
    }

    public void update(E entity) throws UnknownEntityException {
        if (repository.exists(entity))
            repository.createOrUpdate(entity);
        else
            throw new UnknownEntityException(entity);
    }

    public void deleteById(K id) throws UnknownEntityException {
        this.readById(id);
        repository.deleteById(id);
    }
}

