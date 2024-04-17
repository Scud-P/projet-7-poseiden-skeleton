//package com.nnk.springboot;
//
//import com.nnk.springboot.repositories.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HomeControllerTest.class)
//public class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @WithMockUser
//    public void testHome() throws Exception {
//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testAdminHome() throws Exception {
//        mockMvc.perform(get("/admin/home"))
//                .andExpect(status().is3xxRedirection()) // Expect HTTP 3xx redirection status
//                .andExpect(redirectedUrl("/bidList/list")); // Expect redirection to "/bidList/list"
//    }
//
//}
