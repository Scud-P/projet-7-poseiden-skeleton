package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class UserTest {

    @Test
    public void testEqualsAndHashCodeSameEntities() {

        User firstUser = new User(1, "userName", "password", "fullName", "role");
        User secondUser = new User(1, "userName", "password", "fullName", "role");

        assertEquals(firstUser, secondUser);
        assertEquals(firstUser.hashCode(), secondUser.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeDifferentEntities() {

        User firstUser = new User(1, "userName", "password", "fullName", "role");
        User secondUser = new User(2, "userName", "password", "fullName", "role");

        assertNotEquals(firstUser, secondUser);
        assertNotEquals(firstUser.hashCode(), secondUser.hashCode());
    }

}
