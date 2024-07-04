package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "Username must contain only letters (lowercase and uppercase) and numbers, and must be between 4 and 20 characters"
    )    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(name = "fullName")
    @NotBlank(message = "FullName is mandatory")
    @Size(min = 2, max = 60, message =  "Full Name must be between 2 and 60 characters")
    @Pattern(
            regexp = "^[a-zA-Z ]{2,60}$",
            message = "Full Name must contain only letters (lowercase and uppercase) and spaces, and must be between 2 and 60 characters"
    )
    private String fullName;

    @Column(name = "role")
    @NotBlank(message = "Role is mandatory")
    private String role;
}