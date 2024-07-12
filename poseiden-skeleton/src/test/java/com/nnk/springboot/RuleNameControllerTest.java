package com.nnk.springboot;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.services.RuleNameService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RuleNameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    @Autowired
    private RuleNameController ruleNameController;

    @MockBean
    private RuleNameService ruleNameService;

    private static RuleName firstRuleName;
    private static RuleName secondRuleName;
    private static List<RuleName> ruleNames;

    private static RuleNameDTO firstRuleNameDTO;
    private static RuleNameDTO secondRuleNameDTO;
    private static List<RuleNameDTO> ruleNameDTOs;

    private static RuleNameParameter firstRuleNameParam;
    private static RuleNameParameter secondRuleNameParam;
    private static List<RuleNameParameter> ruleNamesParam;



    @BeforeEach
    public void setUp() {

        int id = 1;
        String name = "name";
        String description = "description";
        String json = "json";
        String template = "template";
        String sqlStr = "sqlStr";
        String sqlPart = "sqlPart";

        int id2 = 2;
        String name2 = "name2";
        String description2 = "description2";
        String json2 = "json2";
        String template2 = "template2";
        String sqlStr2 = "sqlStr2";
        String sqlPart2 = "sqlPart2";

        firstRuleName = new RuleName(id, name, description, json, template, sqlStr, sqlPart);
        secondRuleName = new RuleName(id2, name2, description2, json2, template2, sqlStr2, sqlPart2);
        ruleNames = List.of(firstRuleName, secondRuleName);

        firstRuleNameDTO = new RuleNameDTO(id, name, description, json, template, sqlStr, sqlPart);
        secondRuleNameDTO = new RuleNameDTO(id2, name2, description2, json2, template2, sqlStr2, sqlPart2);
        ruleNameDTOs = List.of(firstRuleNameDTO, secondRuleNameDTO);


        firstRuleNameParam = new RuleNameParameter(id, name, description, json, template, sqlStr, sqlPart);
        secondRuleNameParam = new RuleNameParameter(id2, name2, description2, json2, template2, sqlStr2, sqlPart2);
        ruleNamesParam = List.of(firstRuleNameParam, secondRuleNameParam);
    }

    @Test
    public void testHome() {
        when(ruleNameService.getAllRuleNames()).thenReturn(ruleNameDTOs);
        String home = ruleNameController.home(model);

        verify(model).addAttribute("ruleNameDTOs", ruleNameDTOs);
        assertEquals("ruleName/list", home);
    }

    @Test
    @WithMockUser
    public void testAddRuleNameForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser
    public void testValidateRuleNameInvalidInput() throws Exception {
        mockMvc.perform(post("/ruleName/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));

        verify(ruleNameService, times(0)).addRuleName(any(RuleNameParameter.class));
    }

    @Test
    @WithMockUser
    public void testValidateRuleNameValidInput() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", firstRuleName.getName())
                        .param("description", firstRuleName.getDescription())
                        .param("json", firstRuleName.getJson())
                        .param("template", firstRuleName.getTemplate())
                        .param("sqlStr", firstRuleName.getSqlStr())
                        .param("sqlPart", firstRuleName.getSqlStr()))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).addRuleName(any(RuleNameParameter.class));
    }

    @Test
    @WithMockUser
    public void testShowRuleNameUpdateForm() throws Exception {
        when(ruleNameService.getRuleNameParameterById(anyInt())).thenReturn(firstRuleNameParam);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    @WithMockUser
    public void testUpdateRuleNameValidRuleName() throws Exception {
        when(ruleNameService.getRuleNameById(firstRuleName.getId())).thenReturn(firstRuleName);

        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", secondRuleName.getName())
                        .param("description", secondRuleName.getDescription())
                        .param("json", secondRuleName.getJson())
                        .param("template", secondRuleName.getTemplate())
                        .param("sqlStr", secondRuleName.getSqlStr())
                        .param("sqlPart", secondRuleName.getSqlStr()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).updateRuleName(anyInt(), any(RuleNameParameter.class));
    }

    @Test
    @WithMockUser
    public void testUpdateRuleNameInvalidRuleName() throws Exception {
        when(ruleNameService.getRuleNameById(firstRuleName.getId())).thenReturn(firstRuleName);

        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", "")
                        .param("description", secondRuleName.getDescription())
                        .param("json", secondRuleName.getJson())
                        .param("template", secondRuleName.getTemplate())
                        .param("sqlStr", secondRuleName.getSqlStr())
                        .param("sqlPart", secondRuleName.getSqlStr()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

        verify(ruleNameService, times(0)).updateRuleName(anyInt(), any(RuleNameParameter.class));
    }

    @Test
    @WithMockUser
    public void testDeleteFoundRuleName() throws Exception {
        when(ruleNameService.getRuleNameById(firstRuleName.getId())).thenReturn(firstRuleName);

        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).deleteRuleName(firstRuleName.getId());
    }

    @Test
    @WithMockUser
    public void testValidateRuleNameWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);

        RuleNameParameter ruleNameParameter = new RuleNameParameter();
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("No RuleName found for id ")).when(ruleNameService).addRuleName(any(RuleNameParameter.class));

        String view = ruleNameController.validate(ruleNameParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testDeleteRuleNameWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No RuleName found for id ")).when(ruleNameService).deleteRuleName(anyInt());

        String view = ruleNameController.deleteRuleName(1, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testUpdateRuleNameWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        RuleNameParameter ruleNameParameter = new RuleNameParameter();
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No RuleName found for id ")).when(ruleNameService).updateRuleName(anyInt(), any(RuleNameParameter.class));

        String view = ruleNameController.updateRuleName(1, ruleNameParameter, result, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testShowUpdateRuleNameWithIllegalArgumentExceptionShouldReturnError() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("No RuleName found for id ")).when(ruleNameService).getRuleNameParameterById(anyInt());

        String view = ruleNameController.showUpdateForm(1, model);
        assertEquals("error", view);
    }

    @Test
    @WithMockUser
    public void testShowUpdateRuleNameResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String view = ruleNameController.showUpdateForm(1, model);
        assertEquals("ruleName/update", view);
    }
}
