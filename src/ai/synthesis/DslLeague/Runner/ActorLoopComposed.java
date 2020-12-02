/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.Runner;

import ai.synthesis.DslLeague.LeagueExploiter;
import ai.synthesis.DslLeague.MainPlayer;
import ai.synthesis.DslLeague.PlayerDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SearchImplementation;
import ai.synthesis.localsearch.searchImplementation.SimAnComposedNoveltyDefault;
import ai.synthesis.localsearch.searchImplementation.SimAnnealingNoveltyDefault;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubens
 */
public class ActorLoopComposed implements Runnable {

    private PlayerDSL player;
    private SearchImplementation searchAlgorithm;
    private SimAnComposedNoveltyDefault searchAlgorithmComposed;
    private Coordinator coordinator;

    public ActorLoopComposed(PlayerDSL player, Coordinator coordinator) {
        this.player = player;
        this.coordinator = coordinator;
        this.searchAlgorithm = new SimAnnealingNoveltyDefault();
        this.searchAlgorithmComposed = new SimAnComposedNoveltyDefault();
    }

    @Override
    public void run() {
        if (player instanceof LeagueExploiter) {
            leagueExploiterEvaluation();
        } else if (player instanceof MainPlayer) {
            mainPlayerEvaluation();
        } else {
            mainExploiterEvaluation();
        }
    }

    public void mainExploiterEvaluation() {
        for (int i = 0; i < SettingsAlphaDSL.get_number_rounds_actor(); i++) {
            String sOpponent = "";
            HashSet<PlayerDSL> opponents = new HashSet<>();
            HashSet<iDSL> dslOpponents = new HashSet<>();
            if (SettingsAlphaDSL.get_run_full_league()) {
                opponents.addAll(get_all_frozen_agents());
            } else {
                for (int j = 0; j < SettingsAlphaDSL.get_number_opponents_exploiter() * 2; j++) {
                    PlayerDSL opponent = player.get_match();
                    opponents.add(opponent);
                }
            }

            //get MainPlayer
            List<PlayerDSL> learnPlayers = player.getNonFreezedPlayers();
            for (PlayerDSL lPlayer : learnPlayers) {
                if (lPlayer instanceof MainPlayer) {
                    opponents.add(player);
                }
            }

            for (PlayerDSL opponent : opponents) {
                sOpponent += opponent.getClass().getSimpleName() + " -" + opponent.getAgent().translate() + "\n";
                dslOpponents.add(opponent.getAgent());
            }
            sOpponent = sOpponent.substring(0, sOpponent.length() - 1);
            String sOriginal = player.getClass().getSimpleName() + " " + player.getAgent().translate();
            //perform a SA
            DetailedSearchResult results = this.searchAlgorithmComposed.run(dslOpponents,
                    player.getAgent(), SettingsAlphaDSL.get_number_sa_steps(), 2.0f);
            player.setAgent(results.getsWinner());
            System.out.println("  #  Original agent " + sOriginal + "\n     Improved agent "
                    + player.getAgent().translate() + "\n     against " + sOpponent);
            //evaluate against the opponent and save.
            for (PlayerDSL opponent : opponents) {
                runFinalevaluation(player, opponent);
            }

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
            Logger.getLogger(ActorLoopComposed.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (runner.getWinner() == 0) {
            outcome = "win";
        } else if (runner.getWinner() == -1) {
            outcome = "draw";
        }

        coordinator.send_outcome(player, opponent, outcome);
    }

    public String getString() {
        return player.getClass().getSimpleName() + " " + player.getAgent().translate();
    }

    private void leagueExploiterEvaluation() {
        for (int i = 0; i < SettingsAlphaDSL.get_number_rounds_actor(); i++) {
            String sOpponent = "";
            HashSet<PlayerDSL> opponents = new HashSet<>();
            HashSet<iDSL> dslOpponents = new HashSet<>();
            if (SettingsAlphaDSL.get_run_full_league()) {
                opponents.addAll(get_all_frozen_agents());
            } else {
                for (int j = 0; j < SettingsAlphaDSL.get_number_opponents_exploiter(); j++) {
                    PlayerDSL opponent = player.get_match();
                    opponents.add(opponent);
                }
            }
            //get MainPlayer, MainExploiter and the other LeagueExploiter to train against
            List<PlayerDSL> learnPlayers = player.getNonFreezedPlayers();
            learnPlayers.remove(player);
            opponents.addAll(learnPlayers);

            for (PlayerDSL opponent : opponents) {
                sOpponent += opponent.getClass().getSimpleName() + " -" + opponent.getAgent().translate() + "\n";
                dslOpponents.add(opponent.getAgent());
            }
            sOpponent = sOpponent.substring(0, sOpponent.length() - 1);
            String sOriginal = player.getClass().getSimpleName() + " " + player.getAgent().translate();
            //perform a SA
            DetailedSearchResult results = this.searchAlgorithmComposed.run(dslOpponents,
                    player.getAgent(), SettingsAlphaDSL.get_number_sa_steps(), 2.0f);
            player.setAgent(results.getsWinner());
            System.out.println("  #  Original agent " + sOriginal + "\n     Improved agent "
                    + player.getAgent().translate() + "\n     against " + sOpponent);
            //evaluate against the opponent and save.
            for (PlayerDSL opponent : opponents) {
                runFinalevaluation(player, opponent);
            }

        }
    }

    private void mainPlayerEvaluation() {
        for (int i = 0; i < SettingsAlphaDSL.get_number_rounds_actor(); i++) {
            String sOpponent = "";
            HashSet<PlayerDSL> opponents = new HashSet<>();
            HashSet<iDSL> dslOpponents = new HashSet<>();
            if (SettingsAlphaDSL.get_run_full_league()) {
                opponents.addAll(get_all_frozen_agents());
            } else {
                //get opponents using normal league options
                for (int j = 0; j < SettingsAlphaDSL.get_number_opponents_exploiter(); j++) {
                    PlayerDSL opponent = player.get_match();
                    opponents.add(opponent);
                }
            }
            //get mainExploiter and leagueExploiters to train against
            List<PlayerDSL> learnPlayers = player.getNonFreezedPlayers();
            learnPlayers.remove(player);
            opponents.addAll(learnPlayers);

            for (PlayerDSL opponent : opponents) {
                sOpponent += opponent.getClass().getSimpleName() + " -" + opponent.getAgent().translate() + "\n";
                dslOpponents.add(opponent.getAgent());
            }
            sOpponent = sOpponent.substring(0, sOpponent.length() - 1);
            String sOriginal = player.getClass().getSimpleName() + " " + player.getAgent().translate();
            //perform a SA
            DetailedSearchResult results = this.searchAlgorithmComposed.run(dslOpponents,
                    player.getAgent(), SettingsAlphaDSL.get_number_sa_steps(), 2.0f);
            player.setAgent(results.getsWinner());
            System.out.println("  #  Original agent " + sOriginal + "\n     Improved agent "
                    + player.getAgent().translate() + "\n     against " + sOpponent);
            //evaluate against the opponent and save.
            for (PlayerDSL opponent : opponents) {
                runFinalevaluation(player, opponent);
            }

        }
    }

    public List<PlayerDSL> get_all_frozen_agents() {
        return coordinator.get_agents_frozen();
    }
}
