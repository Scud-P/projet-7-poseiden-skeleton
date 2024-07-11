package com.nnk.springboot.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBUserDTO {
    private Integer id;
    private String username;
    private String password;
    private String fullName;
    private String role;
}
