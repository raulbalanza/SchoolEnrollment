package me.raulbalanza.tjv.school_enrollment.business;

/**
 * A checked exception indicating problem related to existence of an entity.
 */
public class EntityStateException extends Exception {
    public EntityStateException() { }

    public <E> EntityStateException(E entity) {
        super("Entity " + entity + " has an invalid state");
    }
}
