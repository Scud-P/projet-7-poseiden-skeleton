package com.nnk.springboot.util;

import com.nnk.springboot.domain.DTO.RatingDTO;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.parameter.RatingParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    Rating toRating(RatingParameter ratingparameter);
    RatingDTO toRatingDTO(Rating rating);
    RatingParameter toRatingParameter(Rating rating);

}
