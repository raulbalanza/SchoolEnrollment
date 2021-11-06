package me.raulbalanza.tjv.school_enrollment.domain;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class User {

    protected final String username;
    protected String password;
    protected String ID;
    protected String email;
    protected String name;
    protected String surnames;
    protected LocalDateTime birthDate;
    protected LocalDateTime registeredAt;

    public User(String username, String password, String email, String name, String surnames, String ID, LocalDateTime birthDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surnames = surnames;
        this.ID = ID;
        this.birthDate = birthDate;
        this.registeredAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User u){
            return this.username.equals(u.username);
        } else return false;
    }

    @Override
    public String toString() {
        return "User " + this.username + " with ID: " + this.ID +
                "\n\t - Name: " + this.name +
                "\n\t - Surnames: " + this.surnames +
                "\n\t - Email: " + this.email +
                "\n\t - Role: " + this.email +
                "\n\t - Birthdate: " + ((this.birthDate == null) ? "-" : this.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE)) +
                "\n\t - Registered at: " + this.registeredAt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
