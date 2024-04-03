package com.project7.poseidenskeleton;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;

    @Test
    public void testAddRating() {

        int ratingId = 1;

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setOrderNumber(1);
        rating.setSandPRating("Good");
        rating.setFitchRating("OK");
        rating.setMoodysRating("Nice");

        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating addedRating = ratingService.addRating(rating);

        assertEquals(rating, addedRating);

    }

}
