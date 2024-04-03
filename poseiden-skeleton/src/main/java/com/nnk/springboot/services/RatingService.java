package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Transactional
    public Rating addRating(Rating rating) {
        ratingRepository.save(rating);
        return rating;
    }

}
