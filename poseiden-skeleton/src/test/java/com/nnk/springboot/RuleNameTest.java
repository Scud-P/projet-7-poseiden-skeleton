package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class RuleNameTest {

    private static RuleName firstRule;

    @BeforeEach
    public void setUp() {
        firstRule = new RuleName();
    }

    @Test
    public void testGettersAndSetters() {

        int id = 1;
        String name = "name";
        String description = "description";
        String json = "json";
        String template = "template";
        String sqlStr = "sqlStr";
        String sqlPart = "sqlPart";

        firstRule.setId(id);
        firstRule.setName(name);
        firstRule.setDescription(description);
        firstRule.setJson(json);
        firstRule.setTemplate(template);
        firstRule.setSqlStr(sqlStr);
        firstRule.setSqlPart(sqlPart);

        assertEquals(id, firstRule.getId());
        assertEquals(name, firstRule.getName());
        assertEquals(description, firstRule.getDescription());
        assertEquals(json, firstRule.getJson());
        assertEquals(template, firstRule.getTemplate());
        assertEquals(sqlStr, firstRule.getSqlStr());
        assertEquals(sqlPart, firstRule.getSqlPart());
    }

    @Test
    public void testEqualsAndHashCodeAndToStringSameEntities() {

        int id = 1;
        String name = "name";
        String description = "description";
        String json = "json";
        String template = "template";
        String sqlStr = "sqlStr";
        String sqlPart = "sqlPart";

        RuleName rule1 = new RuleName(id, name, description, json, template, sqlStr, sqlPart);
        RuleName rule2 = new RuleName(id, name, description, json, template, sqlStr, sqlPart);

        assertEquals(rule1, rule2);
        assertEquals(rule1.hashCode(), rule2.hashCode());
        assertEquals(rule1.toString(), rule2.toString());
    }

    @Test
    public void testEqualsAndHashCodeAndToStringDifferentEntities() {

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

        RuleName rule1 = new RuleName(id, name, description, json, template, sqlStr, sqlPart);
        RuleName rule2 = new RuleName(id2, name2, description2, json2, template2, sqlStr2, sqlPart2);

        assertNotEquals(rule1, rule2);
        assertNotEquals(rule1.hashCode(), rule2.hashCode());
        assertNotEquals(rule1.toString(), rule2.toString());
    }

}
