/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;

/**
 *
 * @author rubens
 */
public class HistoricalDSL extends PlayerDSL{
    
    private iDSL parent;

    public HistoricalDSL(iDSL agent, Payoff payoff) {
        super(payoff, (iDSL) agent.clone());        
        this.parent = agent;
    }

    public iDSL getAgent() {
        return agent;
    }

    public iDSL getParent() {
        return parent;
    }
    
    @Override
    public PlayerDSL get_match() {
        throw new UnsupportedOperationException("Not supported for this instance."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean ready_to_checkpoint() {
        return false;
    }
    
}
