package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
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
    public void testGetAllRuleNames() {

        when(ruleNameRepository.findAll()).thenReturn(ruleNames);
        List<RuleName> resultingRuleNames = ruleNameService.getAllRuleNames();

        assertEquals(ruleNames, resultingRuleNames);
        assertEquals(2, resultingRuleNames.size());

        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    public void testGetRuleNameByIdValidId() {

        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.ofNullable(firstRuleName));

        RuleName foundRuleName = ruleNameService.getRuleNameById(firstRuleName.getId());

        assertEquals(firstRuleName, foundRuleName);
        verify(ruleNameRepository, times(1)).findById(firstRuleName.getId());
    }

    @Test
    public void testGetRuleNameByIdInvalidId() {

        assertThrows(IllegalArgumentException.class, () -> ruleNameService.getRuleNameById(firstRuleName.getId()));
    }

    @Test
    public void testAddRuleNameValidInput() {

        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(firstRuleName);

        RuleName addedRuleName = ruleNameService.addRuleName(firstRuleName);

        assertEquals(firstRuleName, addedRuleName);
        verify(ruleNameRepository, times(1)).save(addedRuleName);
    }

    @Test
    public void testUpdateRuleNameValidInput() {

        RuleName updatedRuleName = new RuleName();
        updatedRuleName.setName("updatedName");
        updatedRuleName.setSqlPart("updatedPart");
        updatedRuleName.setSqlStr("updatedStr");
        updatedRuleName.setJson("updatedJson");
        updatedRuleName.setTemplate("updatedTemplate");
        updatedRuleName.setDescription("updatedDescription");

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));

        RuleName ruleNameToUpdate = ruleNameService.updateRuleName(firstRuleName.getId(), updatedRuleName);

        assertEquals(firstRuleName.getId(), ruleNameToUpdate.getId());
        assertEquals(updatedRuleName.getName(), ruleNameToUpdate.getName());
        assertEquals(updatedRuleName.getSqlPart(), ruleNameToUpdate.getSqlPart());
        assertEquals(updatedRuleName.getSqlStr(), ruleNameToUpdate.getSqlStr());
        assertEquals(updatedRuleName.getDescription(), ruleNameToUpdate.getDescription());
        assertEquals(updatedRuleName.getTemplate(), ruleNameToUpdate.getTemplate());
        assertEquals(updatedRuleName.getJson(), ruleNameToUpdate.getJson());

        verify(ruleNameRepository, times(1)).save(ruleNameToUpdate);
    }

    @Test
    public void testUpdateRuleNameInvalidInput() {

        assertThrows(IllegalArgumentException.class, () -> ruleNameService.updateRuleName(firstRuleName.getId(), secondRuleName));
    }

    @Test
    public void testDeleteRuleName() {

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));
        ruleNameService.addRuleName(firstRuleName);

        when(ruleNameRepository.findById(firstRuleName.getId())).thenReturn(Optional.ofNullable(firstRuleName));
        ruleNameService.deleteRuleName(firstRuleName);

        verify(ruleNameRepository, times(1)).delete(firstRuleName);
    }


}
