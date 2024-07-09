package com.nnk.springboot;

import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RuleNameServiceTest {

    @Autowired
    private RuleNameService ruleNameService;

    @MockBean
    private RuleNameRepository ruleNameRepository;

    private static RuleName firstRuleName;
    private static RuleName secondRuleName;
    private static List<RuleName> ruleNames;

    private static RuleNameParameter ruleNameParameter;


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

        ruleNameParameter = new RuleNameParameter(id, name, description, json, template, sqlStr, sqlPart);
    }

    @Test
    public void testGetAllRuleNames() {

        when(ruleNameRepository.findAll()).thenReturn(ruleNames);
        List<RuleNameDTO> foundRuleNames = ruleNameService.getAllRuleNames();

        assertEquals(ruleNames.size(), foundRuleNames.size());
        verify(ruleNameRepository, times(1)).findAll();

    }

    @Test
    public void testGetRuleNameByIdValidId() {

        ruleNameService.addRuleName(ruleNameParameter);
        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));
        RuleName foundRuleName = ruleNameService.getRuleNameById(firstRuleName.getId());

        assertEquals(firstRuleName, foundRuleName);

    }

    @Test
    public void testGetRuleNameByIdInvalidId() {

        assertThrows(IllegalArgumentException.class, () -> ruleNameService.getRuleNameById(firstRuleName.getId()));
    }

    @Test
    public void testAddRuleNameValidInput() {

        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(firstRuleName);

        RuleName addedRuleName = ruleNameService.addRuleName(ruleNameParameter);

        assertEquals(firstRuleName, addedRuleName);
        verify(ruleNameRepository, times(1)).save(addedRuleName);
    }

    @Test
    public void testUpdateRuleNameValidInput() {

        ruleNameService.addRuleName(ruleNameParameter);

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));

        RuleNameParameter updatedRuleNameParameter = new RuleNameParameter();
        updatedRuleNameParameter.setId(1);
        updatedRuleNameParameter.setName("updatedName");
        updatedRuleNameParameter.setSqlPart("updatedPart");
        updatedRuleNameParameter.setSqlStr("updatedStr");
        updatedRuleNameParameter.setJson("updatedJson");
        updatedRuleNameParameter.setTemplate("updatedTemplate");
        updatedRuleNameParameter.setDescription("updatedDescription");

        RuleName resultingRuleName = ruleNameService.updateRuleName(firstRuleName.getId(), updatedRuleNameParameter);

        //TODO what does thenAnswer() do exactly and why can't I do a simple thenReturn() ?

        when(ruleNameRepository.save(any(RuleName.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(firstRuleName.getId(), resultingRuleName.getId());
        assertEquals(updatedRuleNameParameter.getName(), resultingRuleName.getName());
        assertEquals(updatedRuleNameParameter.getSqlPart(), resultingRuleName.getSqlPart());
        assertEquals(updatedRuleNameParameter.getSqlStr(), resultingRuleName.getSqlStr());
        assertEquals(updatedRuleNameParameter.getDescription(), resultingRuleName.getDescription());
        assertEquals(updatedRuleNameParameter.getTemplate(), resultingRuleName.getTemplate());
        assertEquals(updatedRuleNameParameter.getJson(), resultingRuleName.getJson());

        verify(ruleNameRepository, times(1)).save(resultingRuleName);
    }

    @Test
    public void testUpdateRuleNameInvalidInput() {

        assertThrows(IllegalArgumentException.class, () -> ruleNameService.updateRuleName(firstRuleName.getId(), ruleNameParameter));
    }

    @Test
    public void testDeleteRuleName() {

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));
        ruleNameService.addRuleName(ruleNameParameter);

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));
        ruleNameService.deleteRuleName(firstRuleName.getId());

        verify(ruleNameRepository, times(1)).delete(firstRuleName);
    }


}
