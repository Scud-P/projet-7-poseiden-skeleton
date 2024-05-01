package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        return optionalRating.orElseThrow(() -> new IllegalArgumentException("Invalid rating id"));
    }

    @Transactional
    public Rating updateRating(Rating updatedRating) {

        Rating existingRating = ratingRepository.findById(updatedRating.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating id"));

        existingRating.setMoodysRating(updatedRating.getMoodysRating());
        existingRating.setSandPRating(updatedRating.getSandPRating());
        existingRating.setFitchRating(updatedRating.getFitchRating());
        existingRating.setOrderNumber(updatedRating.getOrderNumber());

        ratingRepository.save(existingRating);

        return existingRating;
    }

    @Transactional
    public void deleteRating(Rating ratingToDelete) {
        Rating existingRating = ratingRepository.findById(ratingToDelete.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating id"));
        ratingRepository.delete(existingRating);
    }
}
