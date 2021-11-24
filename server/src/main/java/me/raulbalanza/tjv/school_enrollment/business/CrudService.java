package me.raulbalanza.tjv.school_enrollment.business;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

/**
 * Common class for business logic of all entities supporting CRUD operations.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */
public abstract class CrudService<K, E, R extends JpaRepository<E, K>> {
    protected final R repository;

    protected CrudService(R repository) {
        this.repository = repository;
    }

    protected abstract boolean exists(E entity);

    // Store a new entity
    @Transactional
    public void create(E entity) throws EntityStateException {
        if (this.exists(entity))
            throw new EntityStateException("An entity with that ID already exists.");
        repository.save(entity);
    }

    public E readById(K id) throws UnknownEntityException {
        Optional<E> obj = repository.findById(id);
        if (obj.isEmpty()) throw new UnknownEntityException("An entity with ID " + id.toString() + " does not exist.");
        return obj.get();
    }

    public Collection<E> readAll() {
        return repository.findAll();
    }

    // Replace a stored entity
    @Transactional
    public void update(E entity) throws UnknownEntityException {
        if (this.exists(entity))
            repository.save(entity);
        else
            throw new UnknownEntityException("An entity with that ID does not exist.");
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}