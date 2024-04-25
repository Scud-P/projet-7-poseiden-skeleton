package com.nnk.springboot;

import com.nnk.springboot.domain.DBUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class DBUserTest {

    @Test
    public void testEqualsAndHashCodeSameEntities() {

        DBUser firstDBUser = new DBUser(1, "userName", "password", "fullName", "role");
        DBUser secondDBUser = new DBUser(1, "userName", "password", "fullName", "role");

        assertEquals(firstDBUser, secondDBUser);
        assertEquals(firstDBUser.hashCode(), secondDBUser.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeDifferentEntities() {

        DBUser firstDBUser = new DBUser(1, "userName", "password", "fullName", "role");
        DBUser secondDBUser = new DBUser(2, "userName", "password", "fullName", "role");

        assertNotEquals(firstDBUser, secondDBUser);
        assertNotEquals(firstDBUser.hashCode(), secondDBUser.hashCode());
    }

}
