package com.nnk.springboot.domain.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBUserParameter {

    private int id;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "Username must contain only letters (lowercase and uppercase) and numbers, and must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Size(min = 2, max = 60, message =  "Full Name must be between 2 and 60 characters")
    @Pattern(
            regexp = "^[a-zA-Z ]{2,60}$",
            message = "Full Name must contain only letters (lowercase and uppercase) and spaces, and must be between 2 and 60 characters")
    private String fullName;

    @NotBlank(message = "Role is mandatory")
    private String role;
}
