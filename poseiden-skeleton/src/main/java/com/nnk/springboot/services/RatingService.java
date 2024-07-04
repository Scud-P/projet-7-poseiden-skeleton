package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Transactional
    public Rating addRating(Rating rating) {
        ratingRepository.save(rating);
        return rating;
    }

    public Rating getRatingById(int id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating id"));
    }

    @Transactional
    public Rating updateRating(int id, Rating updatedRating) {

        Rating existingRating = getRatingById(id);
        existingRating.setMoodysRating(updatedRating.getMoodysRating());
        existingRating.setSandPRating(updatedRating.getSandPRating());
        existingRating.setFitchRating(updatedRating.getFitchRating());
        existingRating.setOrderNumber(updatedRating.getOrderNumber());

        ratingRepository.save(existingRating);

        return existingRating;
    }

    @Transactional
    public void deleteRating(int id) {
        Rating existingRating = getRatingById(id);
        ratingRepository.delete(existingRating);
    }
}
