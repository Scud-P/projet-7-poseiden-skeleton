package com.nnk.springboot;

import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerTest {

    @MockBean
    private RatingService ratingService;

    @Autowired
    private RatingController ratingController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;


    private static Rating firstRating;
    private static Rating secondRating;
    private static Rating thirdRating;
    private static List<Rating> ratings;


    @BeforeEach
    public void setUp() {

        firstRating = new Rating(1, "A", "B", "C", 1);
        secondRating = new Rating(2, "D", "E", "F", 2);
        thirdRating = new Rating(3, "G", "H", "I", 3);

        ratings = List.of(firstRating, secondRating, thirdRating);
    }

    @Test
    public void testHome() {

        when(ratingService.getAllRatings()).thenReturn(ratings);

        String home = ratingController.home(model);

        verify(model).addAttribute("ratings", ratings);
        assertEquals("rating/list", home);

    }

    @Test
    @WithMockUser
    public void testAddRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser
    public void testValidateValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", firstRating.getMoodysRating())
                        .param("sandPRating", firstRating.getSandPRating())
                        .param("fitchRating", firstRating.getFitchRating())
                        .param("orderNumber", String.valueOf(firstRating.getOrderNumber())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    public void testValidateInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/rating/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser
    public void testUpdateRatingValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", "A")
                        .param("sandPRating", "B")
                        .param("fitchRating", "C")
                        .param("orderNumber", "1")
                        .sessionAttr("rating", firstRating)
                        .flashAttr("bindingResult", mockResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        when(ratingService.updateRating(any(Rating.class))).thenReturn(firstRating);

        verify(ratingService, times(1)).updateRating(firstRating);

    }

    @Test
    @WithMockUser
    public void testUpdateRatingInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", "2")
                        .param("sandPRating", "3")
                        .param("fitchRating", "4")
                        .param("orderNumber", "5")
                        .sessionAttr("rating", firstRating)
                        .flashAttr("bindingResult", mockResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, never()).updateRating(firstRating);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenReturn(firstRating);

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    @Test
    @WithMockUser
    public void testDeleteRating() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenReturn(firstRating);

        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).deleteRating(firstRating);
    }

}
