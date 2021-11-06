package me.raulbalanza.tjv.school_enrollment.business;

/**
 * A checked exception indicating that an entity with a certain ID does not exist.
 */
public class UnknownEntityException extends Exception {
    public UnknownEntityException() { }

    public <K> UnknownEntityException(K id) {
        super("Entity with ID " + id.toString() + " does not exist");
    }
}
