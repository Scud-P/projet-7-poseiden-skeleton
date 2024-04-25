package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class DBUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="username")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @Column(name="password")
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Column(name="fullName")
    @NotBlank(message = "FullName is mandatory")
    private String fullName;

    @Column(name="role")
    @NotBlank(message = "Role is mandatory")
    private String role;

    public DBUser(Integer id, String username, String password, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public DBUser() {

    }
}