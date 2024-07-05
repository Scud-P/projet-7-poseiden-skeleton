package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;

    private static Rating rating;

    @BeforeEach
    public void setUp() {
        int ratingId = 1;
        int orderNumber = 1;
        String sandPrRating = "Good";
        String fitchRating = "OK";
        String moodysRating = "Nice";

        rating = new Rating();
        rating.setId(ratingId);
        rating.setOrderNumber(orderNumber);
        rating.setSandPRating(sandPrRating);
        rating.setFitchRating(fitchRating);
        rating.setMoodysRating(moodysRating);
    }

    @Test
    public void testGetRatingById() {

        when(ratingRepository.save(rating)).thenReturn(rating);
        Rating existingRating = ratingService.addRating(rating);

        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.ofNullable(rating));
        Rating result = ratingService.getRatingById(rating.getId());

        assertEquals(existingRating.getId(), result.getId());
        assertEquals(existingRating.getOrderNumber(), result.getOrderNumber());
        assertEquals(existingRating.getSandPRating(), result.getSandPRating());
        assertEquals(existingRating.getFitchRating(), result.getFitchRating());
        assertEquals(existingRating.getMoodysRating(), result.getMoodysRating());
    }

    @Test
    public void testAddRating() {

        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating addedRating = ratingService.addRating(rating);

        verify(ratingRepository, times(1)).save(rating);
        assertEquals(rating, addedRating);
    }

    @Test
    public void testDeleteRatingExistingRating() {

        when(ratingRepository.save(rating)).thenReturn(rating);
        ratingService.addRating(rating);

        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.of(rating));
        ratingService.deleteRating(rating.getId());

        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    public void testDeleteRatingRatingNotFound() {

        assertThrows(IllegalArgumentException.class, () -> ratingService.deleteRating(rating.getId()));

        verify(ratingRepository, times(0)).delete(rating);
    }

    @Test
    public void testUpdateRatingExistingRating() {

        Rating updatedRating = new Rating();

        int updatedRatingId = 1;
        int updatedOrderNumber = 2;
        String updatedSandPrRating = "Bad";
        String updatedFitchRating = "Terrible";
        String updatedMoodysRating = "Toxic";

        updatedRating.setId(updatedRatingId);
        updatedRating.setOrderNumber(updatedOrderNumber);
        updatedRating.setSandPRating(updatedSandPrRating);
        updatedRating.setFitchRating(updatedFitchRating);
        updatedRating.setMoodysRating(updatedMoodysRating);

        when(ratingRepository.save(rating)).thenReturn(rating);
        ratingService.addRating(rating);

        when(ratingRepository.findById(updatedRating.getId())).thenReturn(Optional.of(rating));
        Rating result = ratingService.updateRating(1, updatedRating);

        assertEquals(updatedOrderNumber, result.getOrderNumber());
        assertEquals(updatedSandPrRating, result.getSandPRating());
        assertEquals(updatedFitchRating, result.getFitchRating());
        assertEquals(updatedMoodysRating, result.getMoodysRating());

    }

    @Test
    public void testUpdateRatingRatingNotFound() {

        assertThrows(IllegalArgumentException.class, () -> ratingService.updateRating(1, rating));
        verify(ratingRepository, times(0)).save(rating);
    }

    @Test
    public void testGetRatingByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ratingService.getRatingById(rating.getId()));
    }

}
