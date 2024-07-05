package com.nnk.springboot.services;


import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> getAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    @Transactional
    public RuleName addRuleName(RuleName ruleNameToAdd) {
        return ruleNameRepository.save(ruleNameToAdd);
    }

    public RuleName getRuleNameById(int id) {
        return ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No ruleName found for ID " + id));
    }

    @Transactional
    public RuleName updateRuleName(int id, RuleName ruleNameToUpdate) {
        RuleName existingRuleName = getRuleNameById(id);
        existingRuleName.setName(ruleNameToUpdate.getName());
        existingRuleName.setDescription(ruleNameToUpdate.getDescription());
        existingRuleName.setJson(ruleNameToUpdate.getJson());
        existingRuleName.setTemplate(ruleNameToUpdate.getTemplate());
        existingRuleName.setSqlStr(ruleNameToUpdate.getSqlStr());
        existingRuleName.setSqlPart(ruleNameToUpdate.getSqlPart());
        ruleNameRepository.save(existingRuleName);
        return existingRuleName;
    }

    @Transactional
    public void deleteRuleName(int id) {
        RuleName existingRuleName = getRuleNameById(id);
        ruleNameRepository.delete(existingRuleName);
    }
}
