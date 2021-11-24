package me.raulbalanza.tjv.school_enrollment.business;

/**
 * A checked exception indicating problem related to existence of an entity.
 */
public class EntityStateException extends Exception {
    public EntityStateException() { }

    public EntityStateException(String msg) {
        super(msg);
    }
}
