package com.nnk.springboot.services;

import com.nnk.springboot.domain.DTO.RatingDTO;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.util.RatingMapper;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    public List<RatingDTO> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toRatingDTO)
                .toList();
    }

    @Transactional
    public Rating addRating(RatingParameter ratingParameter) {
        Rating rating = ratingMapper.toRating(ratingParameter);
        ratingRepository.save(rating);
        return rating;
    }

    public Rating getRatingById(int id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating id"));
    }

    @Transactional
    public Rating updateRating(int id, RatingParameter ratingParameter) {

        Rating existingRating = getRatingById(id);
        existingRating.setMoodysRating(ratingParameter.getMoodysRating());
        existingRating.setSandPRating(ratingParameter.getSandPRating());
        existingRating.setFitchRating(ratingParameter.getFitchRating());
        existingRating.setOrderNumber(ratingParameter.getOrderNumber());

        ratingRepository.save(existingRating);

        return existingRating;
    }

    @Transactional
    public void deleteRating(int id) {
        Rating existingRating = getRatingById(id);
        ratingRepository.delete(existingRating);
    }

    public RatingDTO getRatingDTOById(int id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating found for id " + id));
        return ratingMapper.toRatingDTO(rating);
    }

    public RatingParameter getRatingParameterById(int id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating found for id " + id));
        return ratingMapper.toRatingParameter(rating);
    }
}
