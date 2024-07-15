package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class RatingTest {

    @Test
    public void testEqualsSameEntities() {

        Rating firstRating = new Rating(1, "1", "1", "1", 1);
        Rating secondRating = new Rating(1, "1", "1", "1", 1);
        assertEquals(firstRating, secondRating);
    }

    @Test
    public void testHashCodeSameEntities() {

        Rating firstRating = new Rating(1, "1", "1", "1", 1);
        Rating secondRating = new Rating(1, "1", "1", "1", 1);

        assertEquals(firstRating.hashCode(), secondRating.hashCode());
    }

    @Test
    public void testEqualsDifferentEntities() {

        Rating firstRating = new Rating(1, "1", "1", "1", 1);
        Rating secondRating = new Rating(2, "1", "1", "1", 1);
        assertNotEquals(firstRating, secondRating);
    }

    @Test
    public void testHashCodeDifferentEntities() {

        Rating firstRating = new Rating(1, "1", "1", "1", 1);
        Rating secondRating = new Rating(2, "1", "1", "1", 1);

        assertNotEquals(firstRating.hashCode(), secondRating.hashCode());
    }

}
