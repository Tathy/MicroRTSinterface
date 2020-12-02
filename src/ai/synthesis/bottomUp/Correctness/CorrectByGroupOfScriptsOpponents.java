/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Correctness;

import ai.abstraction.WorkerRush;
import ai.core.AI;
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTable;
import ai.synthesis.runners.roundRobinLocal.SmartRRBaseIAxGRunnable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.units.UnitTypeTable;

class callableMultiThreadsBattleAgainstIA implements Callable<resultsThreadsWithWeight> {

    private iDSL script;
    private AI opponent;

    public callableMultiThreadsBattleAgainstIA(iDSL script, AI opponent) {
        this.script = script;
        this.opponent = opponent;
    }

    @Override
    public resultsThreadsWithWeight call() throws Exception {
        SmartRRBaseIAxGRunnable runner1 = new SmartRRBaseIAxGRunnable(script, opponent, false);
        SmartRRBaseIAxGRunnable runner2 = new SmartRRBaseIAxGRunnable(script, opponent, false);
        SmartRRBaseIAxGRunnable runner3 = new SmartRRBaseIAxGRunnable(script, opponent, true);
        SmartRRBaseIAxGRunnable runner4 = new SmartRRBaseIAxGRunnable(script, opponent, true);

        try {
            runner1.start();
            runner2.start();
            runner3.start();
            runner4.start();

            runner1.join();
            runner2.join();
            runner3.join();
            runner4.join();

            float totalScript2 = 0.0f;
            totalScript2 += runner1.getResult();
            totalScript2 += runner2.getResult();
            totalScript2 += runner3.getResult();
            totalScript2 += runner4.getResult();

            return new resultsThreadsWithWeight(totalScript2);
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + "  " + e.getMessage());
            return null;
        }
    }

}

/**
 * This class just work with AI's that implements the default constructor.
 *
 * @author rubens
 */
public class CorrectByGroupOfScriptsOpponents implements iCorrectness {

    private String uniqueID = UUID.randomUUID().toString();
    private String path;
    private List<AI> l_opponents;

    public CorrectByGroupOfScriptsOpponents() {
        this.path = System.getProperty("user.dir").concat("/logs2/");
        this.l_opponents = new ArrayList<>();
    }

    public CorrectByGroupOfScriptsOpponents(List<AI> l_opponents) {
        this();
        this.l_opponents = l_opponents;
    }

    @Override
    public List<iDSL> verifyCorrectness(List<iDSL> pList) {
        List<iDSL> ret = new ArrayList<>();
        if (this.l_opponents.isEmpty()) {
            set_default_opponents();
        }
        if (!pList.isEmpty()) {
            //order pList by translate
            pList.sort(Comparator.comparing(iDSL::translate));
        }
        //build a queue
        Queue<iDSL> queue = new LinkedList<>(pList);
        int cont = 0;
        iDSL iSc1 = queue.poll();
        System.out.println("################## Group Opponents Eval for Bottom-UP search ID " + uniqueID);
        System.out.println("Initial S1 =" + iSc1.translate());
        DetailedSearchResult res = run(l_opponents, iSc1, queue);
        if (res != null) {
            SerializableController.saveSerializable(res.getsWinner(), "Best_AST_"
                    + uniqueID + "_id_" + cont + ".ser", path);
            ret.add(res.getsWinner());
        }
        while (!queue.isEmpty()) {
            cont++;
            SerializableController.saveSerializable(res.getsWinner(), "Best_AST_"
                    + uniqueID + "_id_" + cont + ".ser", path);
            res = run(l_opponents, res.getsWinner(), queue);
            
        }

        System.out.println("################## Finished Group Opponents Eval for Bottom-UP search ID " + uniqueID);
        if(res != null){
            System.out.println("################## Finished Group Opponents Eval for Bottom-UP  search "
                + " returning " + res.getsWinner().translate() + " correct ast. ID " + uniqueID);
            if(!ret.contains(res.getWinner())){
                ret.add(res.getsWinner());
            }            
        }
        
        return ret;
    }

    public DetailedSearchResult run(List<AI> sc_base, iDSL sc_improve, Queue<iDSL> q) {
        iDSL base = (iDSL) sc_improve.clone();
        DetailedSearchResult results = new DetailedSearchResult();

        float score = 2.0f;
        try {
            score = evaluate_list_enemies(sc_base, sc_improve);
        } catch (InterruptedException ex) {
            Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("@@@@@@@@@@ \n Group Opponents Eval Scripts  (" + sc_base.size() + ") = \n" + get_string(sc_base)
                + "\nScore Against Opponents = " + score
                + "\nInitial Script = " + sc_improve.translate() + "\n@@@@@@@@@@");

        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) base.clone();
        float best_score = score;
        //search
        iDSL nextSol = q.poll();
        while (nextSol != null) {
            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("    ** Next Solution =" + nextSol.translate());
            }
            //System.out.println(uniqueID+"    ** base =" + sc_base.translate());
            float new_score = 0.0f;
            try {
                new_score = evaluate_list_enemies(sc_base, nextSol);
            } catch (InterruptedException ex) {
                Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (new_score >= 4.0f) {
                best_sol = (iDSL) nextSol.clone();
                best_score = new_score;
                results.setWinner(best_score);
                results.setsBase((iDSL) base.clone());
                results.setsWinner((iDSL) best_sol.clone());
                System.out.println("    @@@@ \n Best Score reached! "
                        + " \nImproved AST : " + best_sol.translate()
                        + "\n    @@@@");
                return results;
            }

            nextSol = q.poll();
        }
        System.out.println(uniqueID + "    #### Normal execution Finished!!\n"
                + best_sol.translate() + "    ####");
        return null;
    }

    private float evaluate_list_enemies(List<AI> sc_base, iDSL sc_neighbour) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        float new_score = 0.0f;
        Collection<Callable<resultsThreadsWithWeight>> tasks = new ArrayList<>();
        for (AI ai : sc_base) {
            callableMultiThreadsBattleAgainstIA t = new callableMultiThreadsBattleAgainstIA(sc_neighbour, ai);
            tasks.add(t);
        }
        List<Future<resultsThreadsWithWeight>> taskFutureList = executor.invokeAll(tasks);
        for (Future<resultsThreadsWithWeight> future : taskFutureList) {
            new_score += future.get().result;
        }
        executor.shutdown();
        return new_score / sc_base.size();

    }

    private String get_string(List<AI> sc_base) {
        String ret = "";
        for (AI el : sc_base) {
            ret += "|" + el.toString() + " |\n";
        }

        return ret;
    }

    private void set_default_opponents() {
        this.l_opponents.add(new WorkerRush(new UnitTypeTable()));
    }

}
