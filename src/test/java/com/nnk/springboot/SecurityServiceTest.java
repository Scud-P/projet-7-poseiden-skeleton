package com.nnk.springboot;

import com.nnk.springboot.services.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class SecurityServiceTest {

    @Autowired
    private SecurityService securityService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testIsAuthenticatedWhenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertFalse(securityService.isAuthenticated());
    }

    @Test
    public void testIsAuthenticatedWhenAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        assertTrue(securityService.isAuthenticated());
    }


    @Test
    public void testGetCurrentUserDetailsWhenUserIsAuthenticated() {
        UserDetails userDetails = User
                .withUsername("testUser")
                .password("V4l1dPassword")
                .roles("USER")
                .build();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertEquals(userDetails, securityService.getCurrentUserDetails());
    }

    @Test
    public void testGetCurrentUserDetailsWhenUserIsNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertNull(securityService.getCurrentUserDetails());
    }

    @Test
    public void testGetCurrentUserDetailsWhenPrincipalNotUserDetails() {
        Object principal = new Object();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        assertNull(securityService.getCurrentUserDetails());
    }

}
