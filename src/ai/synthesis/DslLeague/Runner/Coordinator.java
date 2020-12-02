/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.Runner;

import ai.synthesis.DslLeague.HistoricalDSL;
import ai.synthesis.DslLeague.LeagueDSL;
import ai.synthesis.DslLeague.PlayerDSL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class Coordinator {
    private LeagueDSL league;

    public Coordinator(LeagueDSL league) {
        this.league = league;
    }
    
    public synchronized void send_outcome(PlayerDSL home, PlayerDSL away, String outcome){     
        //System.err.println("Coordinator called!");
        league.update(home, away, outcome);
        if (home.ready_to_checkpoint()) {
            league.add_player(home.frozen_player());
        }
    }
    
    public List<PlayerDSL> get_agents_frozen(){
        List<PlayerDSL> frozen = new ArrayList<>();
        for (PlayerDSL lp : league.get_league_players()) {
            if(lp instanceof HistoricalDSL){
                frozen.add(lp);
            }
        }
        return frozen;
    }
    
}
