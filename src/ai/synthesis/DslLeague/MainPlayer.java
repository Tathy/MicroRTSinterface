/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author rubens
 */
public class MainPlayer extends PlayerDSL {

    public MainPlayer(iDSL agent, Payoff payoff) {
        super(payoff, agent);
    }

    /**
     * Get a Historical instance of the players in the payoff as historical with
     * pfsp probability equal to squared.
     *
     * @return a opponent
     */
    private PlayerDSL pfsp_branch() {
        List<PlayerDSL> historical = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if (player instanceof HistoricalDSL) {
                historical.add(player);
            }
        }
        List<Double> win_rates = payoff.getItem(this, historical);
        return getRandomChoice(historical, win_rates, "squared");
    }

    /**
     * Based on the opponent payoff data, return a correct opponent to evaluate
     *
     * @param opponent the pretended opponent
     * @return the correct opponent based on payoff data.
     */
    private PlayerDSL selfplay_branch(PlayerDSL opponent) {
        // Play self-play match
        if (payoff.getItem(this, opponent).get(0) > 0.3) {
            return opponent;
        }
        // If opponent is too strong, look for a checkpoint
        // as curriculum
        List<PlayerDSL> historical = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if (player instanceof HistoricalDSL) {
                if (((HistoricalDSL) player).getParent() == opponent.agent) {
                    historical.add(player);
                }
            }
        }
        List<Double> win_rates = payoff.getItem(this, historical);
        return getRandomChoice(historical, win_rates, "variance");
    }

    /**
     * Considering exploitation and forget opponents, return the correct agent
     * to be used.
     *
     * @param opponent the pretended opponent
     * @return the correct opponent based on exploitation or forgetting
     * opponents
     */
    private PlayerDSL verification_branch(PlayerDSL opponent) {
        //Check exploitation
        HashSet<PlayerDSL> exploiters = new HashSet<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if (player instanceof MainExploiter) {
                exploiters.add(player);
            }
        }

        List<PlayerDSL> exp_historical = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if (player instanceof HistoricalDSL) {
                if (exploiters.contains(((HistoricalDSL) player).getParent())) {
                    exp_historical.add(player);
                }
            }
        }

        List<Double> win_rates = payoff.getItem(this, exp_historical);
        if (win_rates.size() > 0 && Collections.min(win_rates) < 0.3) {
             return getRandomChoice(exp_historical, win_rates, "squared");
        }
        //Check forgetting
        List<PlayerDSL> historical = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if (player instanceof HistoricalDSL) {
                if (((HistoricalDSL) player).getParent() == opponent.agent) {
                    historical.add(player);
                }
            }
        }
        win_rates = payoff.getItem(this, historical);
        if (win_rates.size() > 0 && Collections.min(win_rates) < 0.7) {
            return getRandomChoice(historical, win_rates, "squared");
        }
        return null;
    }

    @Override
    public PlayerDSL get_match() {
        double coin_toss = Math.random();
        //Make sure you can beat the League
        if (coin_toss < 0.5) {
            return pfsp_branch();
        }
        //collect all main agents in the payoff
        List<PlayerDSL> main_agents = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if(player instanceof MainPlayer){
                main_agents.add(player);
            }
        }
        
        PlayerDSL opponent = main_agents.get(rand.nextInt(main_agents.size()));

        //Verify if there are some rare players we omitted
        if (coin_toss < (0.5 + 0.15)) {
            PlayerDSL request = verification_branch(opponent);
            if (request != null) {
                return request;
            }
        }
        return selfplay_branch(opponent);
    }

    @Override
    public boolean ready_to_checkpoint() {
        List<PlayerDSL> historical = new ArrayList<>();
        for (PlayerDSL player : payoff.getPlayers()) {
            if(player instanceof HistoricalDSL){
                historical.add(player);
            }
        }
        
        List<Double> win_rates = payoff.getItem(this, historical);
        return (Collections.min(win_rates) > 0.7);
    }

}
