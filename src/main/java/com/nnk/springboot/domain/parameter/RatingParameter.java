package com.nnk.springboot.domain.parameter;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingParameter {

    private int id;

    @NotBlank(message = "moodysRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String moodysRating;

    @NotBlank(message = "sandPRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String sandPRating;

    @NotBlank(message = "fitchRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String fitchRating;

    @NotNull(message = "orderNumber is mandatory")
    @Min(value = 0, message = "Only positive values from 0 to 255")
    @Max(value = 255, message = "Only positive values from 0 to 255")
    private Integer orderNumber;

}
