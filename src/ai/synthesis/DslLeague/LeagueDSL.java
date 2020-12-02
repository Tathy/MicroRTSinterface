/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class LeagueDSL {
    private Payoff payoff;
    private List<PlayerDSL> learning_agents;
    private int main_agents = 1;
    private int main_exploiters = 1;
    private int league_exploiters = 2;

    public LeagueDSL(List<iDSL> initial_agents) {
        this.payoff = new Payoff();
        this.learning_agents = new ArrayList<>();
        for (iDSL agent : initial_agents) {
            for (int i = 0; i < main_agents; i++) {
                PlayerDSL main_agent = new MainPlayer(agent, payoff);
                learning_agents.add(main_agent);
                payoff.add_player(main_agent.frozen_player());                
            }
            for (int i = 0; i < main_exploiters; i++) {
                learning_agents.add(new MainExploiter(payoff, (iDSL) agent.clone()));
            }
            for (int i = 0; i < league_exploiters; i++) {
                learning_agents.add(new LeagueExploiter(payoff, (iDSL) agent.clone()));
            }
        }
        for (PlayerDSL player : learning_agents) {
            payoff.add_player(player);
        }
    }
    
    public synchronized Payoff update(PlayerDSL home, PlayerDSL away, String result){        
        payoff.update(home, away, result);        
        return this.payoff;
    }
    
    public PlayerDSL get_player(int idx){
        return learning_agents.get(idx);
    }
    
    public synchronized void add_player(PlayerDSL player){
        payoff.add_player(player);
    }
    
    public void printPayoffAgents(){
        for (PlayerDSL player : payoff.getPlayers()) {            
            System.out.println("     @ "+player.getClass().getSimpleName()
                    +" "+ player.agent.translate());
        }
    }
    
    public void printPayoffMAtrix(){
        payoff.printMatrixValues();
    }
    
    public List<PlayerDSL> get_league_players(){
        return payoff.getPlayers();
    }
}
