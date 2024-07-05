package com.nnk.springboot;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
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
    }

    @Test
    public void testHome() throws Exception {

        when(ruleNameService.getAllRuleNames()).thenReturn(ruleNames);
        String home = ruleNameController.home(model);

        verify(model).addAttribute("ruleNames", ruleNames);
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

        verify(ruleNameService, times(0)).addRuleName(any(RuleName.class));
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

        verify(ruleNameService, times(1)).addRuleName(any(RuleName.class));
    }

    @Test
    @WithMockUser
    public void testShowRuleNameUpdateForm() throws Exception {

        when(ruleNameService.getRuleNameById(anyInt())).thenReturn(firstRuleName);

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

        verify(ruleNameService, times(1)).updateRuleName(anyInt(), any(RuleName.class));
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

        verify(ruleNameService, times(0)).updateRuleName(anyInt(), any(RuleName.class));
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
}
