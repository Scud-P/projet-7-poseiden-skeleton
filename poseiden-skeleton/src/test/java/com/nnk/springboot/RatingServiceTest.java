package com.nnk.springboot;

import com.nnk.springboot.domain.DTO.RatingDTO;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.util.RatingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
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

    @Autowired
    private RatingMapper ratingMapper;

    private static Rating rating;

    private static RatingParameter ratingParameter;

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

        ratingParameter = new RatingParameter(ratingId, moodysRating, sandPrRating, fitchRating, orderNumber);

    }

    @Test
    public void testGetRatingById() {
        ratingService.addRating(ratingParameter);

        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.ofNullable(rating));

        Rating foundRating = ratingService.getRatingById(rating.getId());

        assertEquals(rating, foundRating);
    }

    @Test
    public void testAddRating() {

        Rating addedRating = ratingService.addRating(ratingParameter);
        verify(ratingRepository, times(1)).save(addedRating);
        assertEquals(rating, addedRating);

    }

    @Test
    public void testDeleteRatingExistingRating() {

        when(ratingRepository.save(rating)).thenReturn(rating);
        ratingService.addRating(ratingParameter);

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

        ratingService.addRating(ratingParameter);

        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.ofNullable(rating));

        RatingParameter updatedRatingParameter = new RatingParameter(
                1, "moodys", "sandPR", "fitch", 2
        );

        Rating resultingRating = ratingService.updateRating(rating.getId(), updatedRatingParameter);

        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(updatedRatingParameter.getId(), resultingRating.getId());
        assertEquals(updatedRatingParameter.getMoodysRating(), resultingRating.getMoodysRating());
        assertEquals(updatedRatingParameter.getFitchRating(), resultingRating.getFitchRating());
        assertEquals(updatedRatingParameter.getSandPRating(), resultingRating.getSandPRating());
        assertEquals(updatedRatingParameter.getOrderNumber(), resultingRating.getOrderNumber());

        verify(ratingRepository, times(2)).save(any(Rating.class));
    }

    @Test
    public void testUpdateRatingRatingNotFound() {

        assertThrows(IllegalArgumentException.class, () -> ratingService.updateRating(1, ratingParameter));
        verify(ratingRepository, times(0)).save(rating);
    }

    @Test
    public void testGetRatingByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ratingService.getRatingById(rating.getId()));
    }

    @Test
    public void testGetAllRatings() {

        Rating secondRating = new Rating(2, rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());

        RatingDTO ratingDTO = new RatingDTO
                (rating.getId(), rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());
        RatingDTO secondRatingDTO = new RatingDTO
                (secondRating.getId(),rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());

        List<Rating> ratings = List.of(rating, secondRating);
        List<RatingDTO> ratingDTOs = List.of(ratingDTO, secondRatingDTO);

        when(ratingRepository.findAll()).thenReturn(ratings);

        List<RatingDTO> result = ratingService.getAllRatings();

        assertEquals(2, result.size());
        assertEquals(ratingDTOs, result);

        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    public void testGetRatingDTOById() {
        RatingDTO ratingDTO = ratingMapper.toRatingDTO(rating);
        when(ratingRepository.findById(1)).thenReturn(Optional.ofNullable(rating));
        RatingDTO result = ratingService.getRatingDTOById(1);
        assertEquals(result, ratingDTO);
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    public void testGetRatingDTOByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ratingService.getRatingById(5));
        verify(ratingRepository, times(1)).findById(5);
    }

    @Test
    public void testGetRatingParamById() {
        RatingParameter ratingParam = ratingMapper.toRatingParameter(rating);
        when(ratingRepository.findById(1)).thenReturn(Optional.ofNullable(rating));
        RatingParameter result = ratingService.getRatingParameterById(1);
        assertEquals(result, ratingParam);
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    public void testGetRatingParamByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ratingService.getRatingParameterById(5));
        verify(ratingRepository, times(1)).findById(5);
    }

}
