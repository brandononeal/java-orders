package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@Service(value = "agentServices")
public class AgentServicesImpl implements AgentServices
{
    @Autowired
    AgentRepository agentrepos;

    @Override
    public Agent findAgentById(long agentcode)
    {
        Agent rtnAgt = agentrepos.findById(agentcode)
            .orElseThrow(() -> new EntityNotFoundException("Agent " + agentcode + " Not Found"));
        return rtnAgt;
    }

    @Transactional
    @Override
    public Agent save(Agent agent)
    {
        return agentrepos.save(agent);
    }

    @Transactional
    @Override
    public void deleteAllAgents()
    {
        agentrepos.deleteAll();
    }
}
