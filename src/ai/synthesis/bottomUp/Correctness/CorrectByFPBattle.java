/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Correctness;

import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.Fp_element;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTable;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
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

class resultsThreadsWithWeight {

    Float result;

    public resultsThreadsWithWeight(Float result) {
        this.result = result;
    }
}

class callableMultiThreadsBattleWithWeight implements Callable<resultsThreadsWithWeight> {

    private iDSL script1;
    private iDSL script2;
    private int weight;

    public callableMultiThreadsBattleWithWeight(iDSL script1, iDSL script2, int weight) {
        this.script1 = script1;
        this.script2 = script2;
        this.weight = weight;
    }

    @Override
    public resultsThreadsWithWeight call() throws Exception {
        SmartRRGxGRunnable runner1 = new SmartRRGxGRunnable(script1, script2);
        SmartRRGxGRunnable runner2 = new SmartRRGxGRunnable(script1, script2);
        SmartRRGxGRunnable runner3 = new SmartRRGxGRunnable(script2, script1);
        SmartRRGxGRunnable runner4 = new SmartRRGxGRunnable(script2, script1);

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
            if (runner1.getWinner() == 1) {
                totalScript2 += runner1.getResult();
            } else if (runner1.getWinner() == -1) {
                totalScript2 += runner1.getResult();
            }
            if (runner2.getWinner() == 1) {
                totalScript2 += runner2.getResult();
            } else if (runner2.getWinner() == -1) {
                totalScript2 += runner2.getResult();
            }

            if (runner3.getWinner() == 0) {
                totalScript2 += runner3.getResult();
            } else if (runner3.getWinner() == -1) {
                totalScript2 += runner3.getResult();
            }
            if (runner4.getWinner() == 0) {
                totalScript2 += runner4.getResult();
            } else if (runner4.getWinner() == -1) {
                totalScript2 += runner4.getResult();
            }

            totalScript2 = totalScript2 * ((float) weight);
            return new resultsThreadsWithWeight(totalScript2);
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + "  " + e.getMessage());
            return null;
        }
    }

}

/**
 *
 * @author rubens
 */
public class CorrectByFPBattle implements iCorrectness {

    private HashMap<String, Fp_element> cum_ind;
    private String uniqueID = UUID.randomUUID().toString();
    private String path;
    private List<iDSL> pListReturn;

    public CorrectByFPBattle() {
        this.cum_ind = new HashMap<>();
        this.path = System.getProperty("user.dir").concat("/logs2/");
        this.pListReturn = new ArrayList<>();
    }

    @Override
    public List<iDSL> verifyCorrectness(List<iDSL> pList) {
        this.cum_ind.clear();
        this.pListReturn.clear();
        //order pList by translate
        pList.sort(Comparator.comparing(iDSL::translate));
        //build a queue
        Queue<iDSL> queue = new LinkedList<>(pList);
        //run partial FP (without SA) to produce a bag of best players
        int cont = 0;
        iDSL iSc1 = queue.poll();
        System.out.println("################## FP for Bottom-UP search ID " + uniqueID);
        System.out.println("Initial S1 =" + iSc1.translate());
        SerializableController.saveSerializable(iSc1, "dsl_" + uniqueID + "_id_" + cont + ".ser", path);
        check_inclusion((iDSL) iSc1.clone());
        DetailedSearchResult res = run(cum_ind.values(), iSc1, queue);
        while (!queue.isEmpty()) {  
            cont++;
            check_inclusion((iDSL) res.getsWinner().clone());
            SerializableController.saveSerializable(res.getsWinner(), "dsl_" + uniqueID + "_id_" + cont + ".ser", path);
            res = run(cum_ind.values(), res.getsWinner(), queue);
        }
        
        System.out.println("################## Finished FP for Bottom-UP search ID " + uniqueID);
        pListReturn.addAll(prepare_list(cum_ind.values()));
        System.out.println("################## Finished FP for Bottom-UP  search "
                + " returning "+pListReturn.size()+" correct ast. ID " + uniqueID);
        return pListReturn;
    }

    public DetailedSearchResult run(Collection<Fp_element> sc_base, iDSL sc_improve, Queue<iDSL> q) {
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
        System.out.println("@@@@@@@@@@ \n FP Scripts  (" + sc_base.size() + ") = \n" + get_string(sc_base)
                + "\nScore Against Opponents = " + score
                + "\nInitial Script = " + sc_improve.translate() + "\n@@@@@@@@@@");

        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) base.clone();
        float best_score = score;
        //search
        iDSL nextSol = q.poll();
        while (nextSol != null) {
            //System.out.println("    ** Next Solution =" + nextSol.translate());
            //System.out.println(uniqueID+"    ** base =" + sc_base.translate());
            float new_score = 0.0f;
            try {
                new_score = evaluate_list_enemies(sc_base, nextSol);                
            } catch (InterruptedException ex) {
                Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(SAForFPTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (new_score >= score) {
                best_sol = (iDSL) nextSol.clone();
                best_score = new_score;
                results.setWinner(best_score);
                results.setsBase((iDSL) base.clone());
                results.setsWinner((iDSL) best_sol.clone());
                System.out.println("    @@@@ \n Best Score reached! "+
                        " \nImproved AST : " + best_sol.translate()+
                        "\n    @@@@");
                return results;
            }
            
            nextSol = q.poll();
        }
        results.setWinner(best_score);
        results.setsBase(base);
        results.setsWinner(best_sol);
        System.out.println(uniqueID + "    #### Normal execution Finished!!\n"
                + best_sol.translate() + "    ####");
        return results;
    }

    private float evaluate_list_enemies(Collection<Fp_element> sc_base, iDSL sc_neighbour) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        float new_score = 0.0f;
        Collection<Callable<resultsThreadsWithWeight>> tasks = new ArrayList<>();
        for (Fp_element dSL : sc_base) {
            //new_score += evaluate_thread_scripts(dSL, sc_neighbour);   
            callableMultiThreadsBattleWithWeight t = new callableMultiThreadsBattleWithWeight(dSL.getAst(),
                    sc_neighbour, dSL.getCounter());
            tasks.add(t);
        }
        List<Future<resultsThreadsWithWeight>> taskFutureList = executor.invokeAll(tasks);
        for (Future<resultsThreadsWithWeight> future : taskFutureList) {
            new_score += future.get().result;
        }
        executor.shutdown();
        return new_score / get_full_size(sc_base);

    }

    private String get_string(Collection<Fp_element> sc_base) {
        String ret = "";
        for (Fp_element el : sc_base) {
            ret += "|" + el.getAst().translate() + " | weight: " + el.getCounter() + " |\n";
        }

        return ret;
    }

    private float get_full_size(Collection<Fp_element> sc_base) {
        float value = 0.0f;
        for (Fp_element fp_element : sc_base) {
            value += (float) fp_element.getCounter();
        }
        return value;
    }

    private void check_inclusion(iDSL idsl) {
        if(cum_ind.size() == SettingsBottomUp.get_limit_of_plist_battle_correct()){
            this.pListReturn.addAll(prepare_list(cum_ind.values()));
            this.cum_ind.clear();
        }
        
        String elem = idsl.translate();
        if (this.cum_ind.containsKey(elem)) {
            this.cum_ind.get(elem).incrementCount();
        } else {
            Fp_element newElem = new Fp_element(idsl);
            this.cum_ind.put(elem, newElem);
        }
    }

    private List<iDSL> prepare_list(Collection<Fp_element> values) {
        List<iDSL> t_list = new ArrayList<>();
        for (Fp_element value : values) {
            t_list.add(value.getAst());
        }
        return t_list;
    }

}
