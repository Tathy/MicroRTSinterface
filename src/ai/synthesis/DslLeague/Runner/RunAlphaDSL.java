/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.Runner;

import ai.synthesis.DslLeague.LeagueDSL;
import ai.synthesis.DslLeague.PlayerDSL;
import ai.synthesis.Novelty.Calculator.NoveltyCommandSingleton;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubens
 */
public class RunAlphaDSL {

    private static BuilderDSLTreeSingleton builder;    

    public static void main(String[] args) {
        //define if the leagueDSL will perform as debbug
        SettingsAlphaDSL.setMode_debug(false);          
        if(SettingsAlphaDSL.isMode_debug()){
            System.err.println("---------MODE DEBUG ACTIVATED!---------");
        }else{
            System.err.println("---------MODE OFFICIAL ACTIVATED!---------");
        }
        System.out.println("Total cores available: "+ Runtime.getRuntime().availableProcessors());
        //initial players
        builder = BuilderDSLTreeSingleton.getInstance();
        List<iDSL> initial_agents = new ArrayList<>();
        initial_agents.add(builder.buildS1Grammar());
        //league data
        LeagueDSL league = new LeagueDSL(initial_agents);
        Coordinator coordinator = new Coordinator(league);
        List<Learner> learners = new ArrayList<>();
        List<ActorLoopComposed> actors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PlayerDSL player = league.get_player(i);
            learners.add(new Learner(player));
            actors.add(new ActorLoopComposed(player, coordinator));
        }
        System.out.println("Initial Learners");
        learners.forEach((learner) -> {
            System.out.println("   ** " +learner.getPlayer().getClass().getSimpleName()
                    +" "+ learner.getString());
        });
        System.out.println("________________________________________________________");
        performLearnersTasks(learners);
        System.out.println("Improved Learners");
        learners.forEach((learner) -> {
            System.out.println("   ** " +learner.getPlayer().getClass().getSimpleName()
                    +" "+ learner.getString());
        });
        System.out.println("________________________________________________________");
        
        System.out.println("League Players");
        for (int i = 0; i < 4; i++) {
            PlayerDSL p = league.get_player(i);
            System.out.println("   ** " + p.getClass().getSimpleName()
                    +" "+p.getAgent().translate());
        };
        System.out.println("________________________________________________________");
        System.out.println("@@@  League Payoff Players");
        league.printPayoffAgents();
        System.out.println("________________________________________________________");
        
        for (int i = 0; i < SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {
            if(SettingsAlphaDSL.get_reset_novelty_table()){
                NoveltyCommandSingleton.getInstance().clear();
            }
            System.out.println("Initial  Actors generation " + i);
            actors.forEach((actor) -> {
                System.out.println("   ** " + actor.getString());
            });
            System.out.println("________________________________________________________");
            performActorsComposedLeague(actors);
            System.out.println("Improved Actors generation " + i);
            actors.forEach((actor) -> {
                System.out.println("   ** " + actor.getString());
            });
            System.out.println("________________________________________________________");
            System.out.println("@@@  League Payoff Players");
            league.printPayoffAgents();
            System.out.println("________________________________________________________");
            
           // System.out.println("@@@  League Payoff Matrix");
           // league.printPayoffMAtrix();
           // System.out.println("________________________________________________________");
        }

    }

    private static void performLearnersTasks(List<Learner> learners) {

        //ExecutorService pool = Executors.newFixedThreadPool(cores/qtd_base_threads);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (Learner learner : learners) {
            //learner.run();
            pool.execute(learner);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunAlphaDSL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void performActorsLeague(List<ActorLoop> actors) {
        //ExecutorService pool = Executors.newFixedThreadPool(cores/qtd_base_threads);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (ActorLoop actor : actors) {
            //learner.run();
            pool.execute(actor);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunAlphaDSL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void performActorsComposedLeague(List<ActorLoopComposed> actors) {
        //ExecutorService pool = Executors.newFixedThreadPool(cores/qtd_base_threads);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (ActorLoopComposed actor : actors) {
            //learner.run();
            pool.execute(actor);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunAlphaDSL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
