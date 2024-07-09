package com.nnk.springboot.util;

import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuleNameMapper {

    RuleName toRuleName(RuleNameParameter ruleNameParameter);
    RuleNameDTO toRuleNameDTO(RuleName ruleName);
    RuleNameParameter toRuleNameParameter(RuleNameDTO ruleNameDTO);

}
