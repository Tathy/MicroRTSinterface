/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.Runner;

import ai.synthesis.DslLeague.PlayerDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SearchImplementation;
import ai.synthesis.localsearch.searchImplementation.SimAnnealingNoveltyDefault;

/**
 *
 * @author rubens
 */
public class Learner implements Runnable{
    private PlayerDSL player;
    private SearchImplementation searchAlgorithm;

    public PlayerDSL getPlayer() {
        return player;
    }
    
    public Learner(PlayerDSL player) {
        this.player = player;
        this.searchAlgorithm = new SimAnnealingNoveltyDefault();        
    }

    @Override
    public void run() {        
        //run at selfplay
        iDSL selfCopy = (iDSL) player.getAgent().clone();
        DetailedSearchResult results = this.searchAlgorithm.run(selfCopy, player.getAgent(), 1, 2.0f);
        player.setAgent(results.getsWinner());        
    }
    
    public String getString(){        
        return player.getAgent().translate();        
    }
    
    
}
