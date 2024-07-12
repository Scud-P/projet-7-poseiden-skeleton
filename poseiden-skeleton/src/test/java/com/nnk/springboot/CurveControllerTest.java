package com.nnk.springboot;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
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
    private static CurvePointDTO firstPointDTO;
    private static CurvePointDTO secondPointDTO;
    private static List<CurvePointDTO> curvePointDTOs;


    @BeforeEach
    public void setUp() {
        Timestamp firstStamp = new Timestamp(1000000000000L);
        Timestamp secondStamp = new Timestamp(1111111111111L);

        firstPoint = new CurvePoint(1, 10, firstStamp, 1.0, 1.0, firstStamp);
        secondPoint = new CurvePoint(2, 20, secondStamp, 2.0, 2.0, secondStamp);

        firstPointDTO = new CurvePointDTO(firstPoint.getId(), firstPoint.getCurveId(), firstPoint.getTerm(), firstPoint.getValue());
        secondPointDTO = new CurvePointDTO(secondPoint.getId(), secondPoint.getCurveId(), secondPoint.getTerm(), secondPoint.getValue());

        curvePointDTOs = List.of(firstPointDTO, secondPointDTO);
    }

    @Test
    public void testHome() {
        when(curveService.getAllCurvePoints()).thenReturn(curvePointDTOs);

        String home = curveController.home(model);

        verify(model).addAttribute("curvePointDTOs", curvePointDTOs);
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

        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePointValidInput() throws Exception {

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("id", String.valueOf(secondPoint.getId()))
                        .param("curveId", String.valueOf(secondPoint.getCurveId()))
                        .param("asOfDate", String.valueOf(secondPoint.getAsOfDate()))
                        .param("term", String.valueOf(secondPoint.getTerm()))
                        .param("value", String.valueOf(secondPoint.getValue()))
                        .param("creationDate", String.valueOf(secondPoint.getCreationDate()))
                        .sessionAttr("curvePoint", firstPoint))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePointParameter curvePointParameter = new CurvePointParameter
                (secondPoint.getId(), secondPoint.getCurveId(), secondPoint.getTerm(), secondPoint.getValue());


        verify(curveService, times(1)).updateCurvePoint(1, curvePointParameter);
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

        verify(curveService, never()).updateCurvePoint(anyInt(), any(CurvePointParameter.class));
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {

        when(curveService.getCurvePointParameterById(anyInt())).thenReturn
                (new CurvePointParameter(firstPoint.getId(), firstPoint.getCurveId(), firstPoint.getTerm(), firstPoint.getValue()));

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

    @Test
    @WithMockUser
    public void testValidateCurvePointWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);

        CurvePointParameter curvePointParameter = new CurvePointParameter();
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("No CurvePoint found for id ")).when(curveService).addCurvePoint(any(CurvePointParameter.class));

        String view = curveController.validate(curvePointParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testDeleteCurvePointWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No CurvePoint found for id ")).when(curveService).deleteCurvePoint(anyInt());

        String view = curveController.deleteCurvePoint(1, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePointWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        CurvePointParameter curvePointParameter = new CurvePointParameter();
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No CurvePoint found for id ")).when(curveService).updateCurvePoint(anyInt(), any(CurvePointParameter.class));

        String view = curveController.updateCurvePoint(1, curvePointParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testShowUpdateCurvePointWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No CurvePoint found for id ")).when(curveService).getCurvePointParameterById(anyInt());

        String view = curveController.showUpdateForm(1, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testShowUpdateCurvePointResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String view = curveController.showUpdateForm(1, model);
        assertEquals("curvePoint/update", view);
    }

}
