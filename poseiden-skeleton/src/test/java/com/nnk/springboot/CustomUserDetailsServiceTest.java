package com.nnk.springboot;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUserName() {

        String username = "Bob";
        String role = "user";
        String password = "P4ssW0rd!";
        String fullName = "Bob Ross";

        DBUser testUser = new DBUser();
        testUser.setFullName(fullName);
        testUser.setUsername(username);
        testUser.setRole(role);
        testUser.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(testUser);

        User resultUserDetails = (User) customUserDetailsService.loadUserByUsername(username);

        List<GrantedAuthority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        assertEquals(testUser.getUsername(), resultUserDetails.getUsername());
        assertEquals(testUser.getPassword(), resultUserDetails.getPassword());

        List<GrantedAuthority> actualAuthorities = new ArrayList<>(resultUserDetails.getAuthorities());

        assertEquals(expectedAuthorities, actualAuthorities);

        System.out.println(resultUserDetails);
    }

    @Test
    public void testGetGrantedAuthorities() {
        String role = "user";

        List<GrantedAuthority> actualAuthorities = getGrantedAuthorities(role);

        List<GrantedAuthority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        assertEquals(expectedAuthorities, actualAuthorities);
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}

