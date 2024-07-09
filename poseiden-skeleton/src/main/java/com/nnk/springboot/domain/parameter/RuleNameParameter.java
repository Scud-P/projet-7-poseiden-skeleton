package com.nnk.springboot.domain.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleNameParameter {

    private int id;

    @NotBlank(message = "name is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String name;

    @NotBlank(message = "description is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String description;

    @NotBlank(message = "Json is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String json;

    @Size(max = 512, message = "Can't be more than 512 characters.")
    private String template;

    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String sqlStr;

    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String sqlPart;

}
