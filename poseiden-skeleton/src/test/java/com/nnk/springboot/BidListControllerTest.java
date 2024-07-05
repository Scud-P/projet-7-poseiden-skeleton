package com.nnk.springboot;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidService;
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

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidListController.class)
@AutoConfigureMockMvc(addFilters = false)

public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListController bidController;

    @MockBean
    private BidService bidService;

    @Mock
    private Model model;

    private static BidList firstList;
    private static BidList secondList;

    private static List<BidList> bidLists;

    @BeforeEach
    public void setUp() {

        Timestamp firstDate = new Timestamp(1000L);
        Timestamp secondDate = new Timestamp(1000L);


        firstList = new BidList
                (1, "account", "type", 1.0, 1, 1, 1, "benchmark", firstDate, "commentary",
                        "security", "status", "trader", "book", "creationName", firstDate, "revisionName",
                        secondDate, "dealName", "dealType", "sourceListId", "side");

        secondList = new BidList
                (2, "account2", "type2", 2.0, 2, 2, 2, "benchmark2", firstDate, "commentary2",
                        "security2", "status2", "trader2", "book2", "creationName2", firstDate, "revisionName2",
                        secondDate, "dealName2", "dealType2", "sourceListId2", "side2");

        bidLists = List.of(firstList, secondList);
    }

    @Test
    public void testHome() {
        when(bidService.getAllBids()).thenReturn(bidLists);

        String home = bidController.home(model);

        verify(model).addAttribute("bidLists", bidLists);
        assertEquals("bidList/list", home);
    }

    @Test
    @WithMockUser
    public void testAddBidForm() throws Exception {

        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    public void testValidateBidListValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/bidList/validate")
                        .param("account", firstList.getAccount())
                        .param("type", firstList.getType())
                        .param("bidQuantity", String.valueOf(firstList.getBidQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void testValidateBidListInvalidInput() throws Exception {
        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);
        mockMvc.perform(post("/bidList/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    public void testUpdateBidListValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", secondList.getAccount())
                        .param("type", secondList.getType())
                        .param("bidQuantity", String.valueOf(secondList.getBidQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void testUpdateBidListInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", secondList.getAccount())
                        .param("type", "")
                        .param("bidQuantity", String.valueOf(secondList.getBidQuantity())))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));

        verify(bidService, never()).updateBidList(anyInt(), any(BidList.class));
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {

        when(bidService.getBidById(anyInt())).thenReturn(firstList);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
    }

    @Test
    @WithMockUser
    public void testDeleteBidList() throws Exception {

        when(bidService.getBidById(anyInt())).thenReturn(firstList);

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidService, times(1)).deleteBid(firstList.getId());
    }

}
