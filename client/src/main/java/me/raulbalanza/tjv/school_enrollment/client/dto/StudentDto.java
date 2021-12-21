package me.raulbalanza.tjv.school_enrollment.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class StudentDto {

    // These attributes are public because the DTO is just a transfer object. Wrong data will never get into the actual classes.
    public String username;
    public String ID;
    public String email;
    public String name;
    public String surnames;
    public int currentYear;
    public String password = "default";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthDate;

    public String errorMessage;

    public StudentDto() { }

    public StudentDto(StudentDto other, String errorM) {
        if (other != null){
            this.username = other.username;
            this.ID = other.ID;
            this.email = other.email;
            this.name = other.name;
            this.surnames = other.surnames;
            this.currentYear = other.currentYear;
            this.password = other.password;
        }
        this.errorMessage = errorM;
    }

    public StudentDto(String u, String ID, String p, String e, String n, String sn, LocalDate bD, int r) {
        username = u;
        this.ID = ID;
        password = p;
        email = e;
        name = n;
        surnames = sn;
        currentYear = r;
        birthDate = bD;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
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
