package com.nnk.springboot;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
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

@WebMvcTest(CurveController.class)
@AutoConfigureMockMvc(addFilters = false)

public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    @Autowired
    private CurveController curveController;

    @MockBean
    private CurvePointService curveService;

    @MockBean
    private CurvePointRepository curveRepository;

    private static CurvePoint firstPoint;
    private static CurvePoint secondPoint;
    private static CurvePoint thirdPoint;
    private static List<CurvePoint> curvePoints;

    @BeforeEach
    public void setUp() {
        Timestamp firstStamp = new Timestamp(1000000000000L);
        Timestamp secondStamp = new Timestamp(1111111111111L);

        firstPoint = new CurvePoint(1, 10, firstStamp, 1.0, 1.0, firstStamp);
        secondPoint = new CurvePoint(2, 20, secondStamp, 2.0, 2.0, secondStamp);

        curvePoints = List.of(firstPoint, secondPoint);
    }

    @Test
    public void testHome() {
        when(curveService.getAllCurvePoints()).thenReturn(curvePoints);

        String home = curveController.home(model);

        verify(model).addAttribute("curvePoints", curvePoints);
        assertEquals("curvePoint/list", home);
    }

    @Test
    @WithMockUser
    public void testAddCurvePointForm() throws Exception {

        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void testValidateValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/curvePoint/validate")
                        .param("id", String.valueOf(firstPoint.getId()))
                        .param("curveId", String.valueOf(firstPoint.getCurveId()))
                        .param("asOfDate", String.valueOf(firstPoint.getAsOfDate()))
                        .param("term", String.valueOf(firstPoint.getTerm()))
                        .param("value", String.valueOf(firstPoint.getValue()))
                        .param("creationDate", String.valueOf(firstPoint.getCreationDate())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void testValidateInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/curvePoint/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePointValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("id", String.valueOf(secondPoint.getId()))
                        .param("curveId", String.valueOf(secondPoint.getCurveId()))
                        .param("asOfDate", String.valueOf(secondPoint.getAsOfDate()))
                        .param("term", String.valueOf(secondPoint.getTerm()))
                        .param("value", String.valueOf(secondPoint.getValue()))
                        .param("creationDate", String.valueOf(secondPoint.getCreationDate()))
                        .sessionAttr("curvePoint", firstPoint)
                        .flashAttr("bindingResult", mockResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePointInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("id", String.valueOf(secondPoint.getId()))
                        .param("curveId", "")
                        .param("asOfDate", String.valueOf(secondPoint.getAsOfDate()))
                        .param("term", String.valueOf(secondPoint.getTerm()))
                        .param("value", String.valueOf(secondPoint.getValue()))
                        .param("creationDate", String.valueOf(secondPoint.getCreationDate()))
                        .sessionAttr("curvePoint", firstPoint)
                        .flashAttr("bindingResult", mockResult))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));

        verify(curveService, never()).updateCurvePoint(1, firstPoint);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {

        when(curveService.getCurvePointById(anyInt())).thenReturn(firstPoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser
    public void testDeleteCurvePoint() throws Exception {

        when(curveService.getCurvePointById(anyInt())).thenReturn(firstPoint);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curveService, times(1)).deleteCurvePoint(firstPoint.getId());
    }

}
