package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {

    // These attributes are public because the DTO is just a transfer object. Wrong data will never get into the actual classes.
    @JsonView(Views.Basic.class)
    public String username;

    @JsonView(Views.Basic.class)
    public String ID;

    @JsonView(Views.Detailed.class)
    public String email;

    @JsonView(Views.Detailed.class)
    public String name;

    @JsonView(Views.Detailed.class)
    public String surnames;

    @JsonView(Views.Detailed.class)
    public int currentYear;

    @JsonView(Views.Internal.class)
    public String password;

    @JsonView(Views.Internal.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    public LocalDate birthDate;

    public StudentDto() { }

    public StudentDto(String u, String ID, String p, String e, String n, String sn, LocalDate bD, int cY) {
        username = u;
        this.ID = ID;
        password = p;
        email = e;
        name = n;
        surnames = sn;
        currentYear = cY;
        birthDate = bD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
