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
import ai.synthesis.localsearch.searchImplementation.SimulatedAnnealing;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubens
 */
public class ActorLoop implements Runnable {

    private PlayerDSL player;
    private SearchImplementation searchAlgorithm;
    private Coordinator coordinator;

    public ActorLoop(PlayerDSL player, Coordinator coordinator) {
        this.player = player;
        this.coordinator = coordinator;
        this.searchAlgorithm = new SimulatedAnnealing();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            PlayerDSL opponent = player.get_match();
            String sOpponent = opponent.getClass().getSimpleName()+" -"+ opponent.getAgent().translate();
            String sOriginal = player.getClass().getSimpleName()+" "+ player.getAgent().translate();
            //perform a SA
            DetailedSearchResult results = this.searchAlgorithm.run((iDSL) opponent.getAgent().clone(), player.getAgent(), 1, 2.0f);
            player.setAgent(results.getsWinner());
            System.out.println("  #  Original agent " + sOriginal + "\n     Improved agent " 
                    + player.getAgent().translate() + "\n     against " + sOpponent);
            //evaluate against the opponent and save.
            runFinalevaluation(player, opponent);
        }
    }

    /**
     * This method perform a final battle between the player and the opponent to
     * update the payoff matrix.
     *
     * @param player
     * @param opponent
     */
    private void runFinalevaluation(PlayerDSL player, PlayerDSL opponent) {
        String outcome = "loss";
        SmartRRGxGRunnable runner = new SmartRRGxGRunnable(player.getAgent(), opponent.getAgent());
        try {
            runner.start();
            runner.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ActorLoop.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (runner.getWinner() == 0) {
            outcome = "win";
        } else if (runner.getWinner() == -1) {
            outcome = "draw";
        }

        coordinator.send_outcome(player, opponent, outcome);
    }

    public String getString() {
        return player.getClass().getSimpleName()+" "+player.getAgent().translate();
    }

}
