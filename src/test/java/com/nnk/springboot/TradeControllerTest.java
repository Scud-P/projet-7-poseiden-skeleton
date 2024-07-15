package com.nnk.springboot;

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.domain.parameter.TradeParameter;
import com.nnk.springboot.services.TradeService;
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

@WebMvcTest(TradeController.class)
@AutoConfigureMockMvc(addFilters = false)

public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeController tradeController;

    @MockBean
    private TradeService tradeService;

    @Mock
    Model model;

    private static Trade firstTrade;
    private static Trade secondTrade;
    private static List<Trade> tradeList;
    private static Trade firstSimpleTrade;
    private static Trade secondSimpleTrade;
    private static List<Trade> simpleTrades;

    @BeforeEach
    public void setUp() {

        int id = 1;
        String account = "account";
        String type = "type";
        double buyQuantity = 1.0;
        double sellQuantity = 1.0;
        double buyPrice = 1.0;
        double sellPrice = 1.0;
        Timestamp tradeDate = new Timestamp(System.currentTimeMillis());
        String security = "security";
        String status = "status";
        String trader = "trader";
        String benchmark = "benchmark";
        String book = "book";
        String creationName = "creationName";
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        String revisionName = "revisionName";
        Timestamp revisionDate = new Timestamp(System.currentTimeMillis());
        String dealName = "dealName";
        String dealType = "dealType";
        String sourceListId = "sourceListId";
        String side = "side";

        int id2 = 2;
        String account2 = "account2";
        String type2 = "type2";
        double buyQuantity2 = 2.0;
        double sellQuantity2 = 2.0;
        double buyPrice2 = 2.0;
        double sellPrice2 = 2.0;
        Timestamp tradeDate2 = new Timestamp(System.currentTimeMillis());
        String security2 = "security2";
        String status2 = "status2";
        String trader2 = "trader2";
        String benchmark2 = "benchmark2";
        String book2 = "book2";
        String creationName2 = "creationName2";
        Timestamp creationDate2 = new Timestamp(System.currentTimeMillis());
        String revisionName2 = "revisionName2";
        Timestamp revisionDate2 = new Timestamp(System.currentTimeMillis());
        String dealName2 = "dealName2";
        String dealType2 = "dealType2";
        String sourceListId2 = "sourceListId2";
        String side2 = "side2";

        firstTrade = new Trade(id, account, type, buyQuantity, sellQuantity, buyPrice, sellPrice, tradeDate,
                security, status, trader, benchmark, book, creationName, creationDate, revisionName, revisionDate,
                dealName, dealType, sourceListId, side);

        secondTrade = new Trade(id2, account2, type2, buyQuantity2, sellQuantity2, buyPrice2, sellPrice2, tradeDate2,
                security2, status2, trader2, benchmark2, book2, creationName2, creationDate2, revisionName2, revisionDate2,
                dealName2, dealType2, sourceListId2, side2);

        tradeList = List.of(firstTrade, secondTrade);

        firstSimpleTrade =  new Trade();
        firstSimpleTrade.setId(0);
        firstSimpleTrade.setAccount(account);
        firstSimpleTrade.setType(type);
        firstSimpleTrade.setBuyQuantity(buyQuantity);

        secondSimpleTrade =  new Trade();
        secondSimpleTrade.setId(3);
        secondSimpleTrade.setAccount(account2);
        secondSimpleTrade.setType(type2);
        secondSimpleTrade.setBuyQuantity(buyQuantity2);

        simpleTrades = List.of(firstSimpleTrade, secondSimpleTrade);

    }

    @Test
    public void testHome() {

        List<TradeDTO> tradeDTOs = List.of(
                new TradeDTO(0, firstTrade.getAccount(), firstTrade.getType(), firstTrade.getBuyQuantity()),
                new TradeDTO(3, secondTrade.getAccount(), secondTrade.getType(), secondTrade.getBuyQuantity())
                );

        when(tradeService.getAllTrades()).thenReturn(tradeDTOs);
        String home = tradeController.home(model);

        verify(model).addAttribute("tradeDTOs", tradeDTOs);
        assertEquals("trade/list", home);
    }

    @Test
    @WithMockUser
    public void testAddTradeForm() throws Exception {

        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser
    public void testValidateTradeValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/trade/validate")
                        .param("account", firstTrade.getAccount())
                        .param("type", firstTrade.getType()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).addTrade(any(TradeParameter.class));
    }

    @Test
    @WithMockUser
    public void testValidateTradeInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/trade/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        verify(tradeService, times(0)).addTrade(any(TradeParameter.class));
    }

    @Test
    @WithMockUser
    public void testShowTradeUpdateForm() throws Exception {

        when(tradeService.getTradeParameterById(anyInt())).thenReturn(new TradeParameter(
                firstTrade.getId(), firstTrade.getAccount(), firstTrade.getType(), firstTrade.getBuyQuantity()));

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }

    @Test
    @WithMockUser
    public void testUpdateTradeValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        when(tradeService.getTradeById(firstTrade.getId())).thenReturn(firstTrade);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", secondTrade.getAccount())
                        .param("type", secondTrade.getType()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).updateTrade(anyInt(), any(TradeParameter.class));
    }

    @Test
    @WithMockUser
    public void testUpdateTradeInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", "")
                        .param("type", secondTrade.getType()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));

        verify(tradeService, times(0)).updateTrade(anyInt(), any(TradeParameter.class));
    }

    @Test
    @WithMockUser
    public void testDeleteTrade() throws Exception {

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).deleteTrade(1);
    }

    @Test
    @WithMockUser
    public void testValidateTradeWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        TradeParameter tradeParameter = new TradeParameter();
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("No RuleName found for id ")).when(tradeService).addTrade(any(TradeParameter.class));

        String view = tradeController.validate(tradeParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testDeleteTradeWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("No Trade found for id ")).when(tradeService).deleteTrade(anyInt());

        String view = tradeController.deleteTrade(1, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testUpdateTradeWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        TradeParameter tradeParameter = new TradeParameter();
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No Trade found for id ")).when(tradeService).updateTrade(anyInt(), any(TradeParameter.class));

        String view = tradeController.updateTrade(1, tradeParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testShowUpdateTradeWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No Trade found for id ")).when(tradeService).getTradeParameterById(anyInt());

        String view = tradeController.showUpdateForm(1, model);
        assertEquals("error", view);
    }
}
