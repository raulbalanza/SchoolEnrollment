package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDto {

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
    public String rank;

    @JsonView(Views.Internal.class)
    public String password;

    @JsonView(Views.Internal.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    public LocalDate birthDate;

    public TeacherDto() { }

    public TeacherDto(String u, String ID, String p, String e, String n, String sn, LocalDate bD, String r) {
        username = u;
        this.ID = ID;
        password = p;
        email = e;
        name = n;
        surnames = sn;
        rank = r;
        birthDate = bD;
    }

}
