package com.nnk.springboot.services;


import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.util.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Autowired
    private RuleNameMapper ruleNameMapper;

    public List<RuleNameDTO> getAllRuleNames() {
        return ruleNameRepository.findAll()
                .stream()
                .map(ruleNameMapper::toRuleNameDTO)
                .toList();
    }

    @Transactional
    public RuleName addRuleName(RuleNameParameter ruleNameParameter) {
        RuleName ruleName = ruleNameMapper.toRuleName(ruleNameParameter);
        ruleNameRepository.save(ruleName);
        return ruleName;
    }

    public RuleName getRuleNameById(int id) {
        return ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No ruleName found for ID " + id));
    }

    @Transactional
    public RuleName updateRuleName(int id, RuleNameParameter ruleNameParameter) {
        RuleName existingRuleName = getRuleNameById(id);
        existingRuleName.setName(ruleNameParameter.getName());
        existingRuleName.setDescription(ruleNameParameter.getDescription());
        existingRuleName.setJson(ruleNameParameter.getJson());
        existingRuleName.setTemplate(ruleNameParameter.getTemplate());
        existingRuleName.setSqlStr(ruleNameParameter.getSqlStr());
        existingRuleName.setSqlPart(ruleNameParameter.getSqlPart());
        ruleNameRepository.save(existingRuleName);
        return existingRuleName;
    }

    @Transactional
    public void deleteRuleName(int id) {
        RuleName existingRuleName = getRuleNameById(id);
        ruleNameRepository.delete(existingRuleName);
    }

    public RuleNameParameter getRuleNameParameterById(int id) {
        RuleName ruleName = getRuleNameById(id);
        return ruleNameMapper.toRuleNameParameter(ruleName);
    }
}
