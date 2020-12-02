/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.EquivalentMethods;

import ai.synthesis.bottomUp.EquivalentMethods.ParallelProcess.CallableOutputCalc;
import ai.synthesis.bottomUp.EquivalentMethods.ParallelProcess.WrapOutputs;
import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class BasicElimEquvalentParallel implements iRemoveEquivalent {

    @Override
    public List<iDSL> removeEquivalents(List<iDSL> pList, List<iInputsBottomUp> inputs) {
        HashMap<iDSL, List<PlayerAction>> outputs = new HashMap<>();
        int qtdCore = Runtime.getRuntime().availableProcessors() - 1;        

        //compute all responses for each element in pList         
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Removing by Object PlayerAction comparation with "+qtdCore+" threads " + dtf.format(now));

        int partSize = get_part_size(pList.size(), qtdCore);
        UnitTypeTable utt = new UnitTypeTable();
        ExecutorService executor = Executors.newFixedThreadPool(qtdCore);
        int count = 1;
        for (List<iDSL> part : Partition.ofSize(pList, partSize)) {
            try {
                Collection<Callable<WrapOutputs>> tasks = new ArrayList<>();
                for (iDSL ast : part) {
                    CallableOutputCalc task = new CallableOutputCalc(inputs, ast, utt);
                    tasks.add(task);
                }
                List<Future<WrapOutputs>> futureTasks = executor.invokeAll(tasks);
                for (Future<WrapOutputs> resTask : futureTasks) {
                    List<PlayerAction> actions = resTask.get().getActions();                    
                    if (!existSameActionInOutputs(outputs, actions)) {
                        //if there ins't, include in the map.                        
                        outputs.put(resTask.get().getAst(), actions);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(BasicElimEquvalentParallel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(BasicElimEquvalentParallel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(BasicElimEquvalentParallel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(count % 20 == 0){
                System.out.println(dtf.format(LocalDateTime.now())+ " finished batch "+count+ 
                        " approx. "+(count*partSize)+" processed with "+ outputs.size()+
                        " elements shrinked.");
            }
            count++;
        }
        executor.shutdown();        
        System.out.println("Total of new elements shrinked = " + outputs.size());
        System.out.println(dtf.format(LocalDateTime.now()));
        if (SettingsBottomUp.isMode_debug()) {
            
            System.out.println("Final Commands");
            for (iDSL dSL : outputs.keySet()) {
                System.out.println("--------" + dSL.translate());
            }
        }

        return new ArrayList<>(outputs.keySet());

    }

    private boolean existSameActionInOutputs(HashMap<iDSL, List<PlayerAction>> outputs, List<PlayerAction> actions) throws Exception {
        //return true, if found or false if not

        for (List<PlayerAction> value : outputs.values()) {            
            if (isEqual(value, actions)) {                
                return true;
            }
        }

        return false;
    }

    private boolean isEqual(List<PlayerAction> value, List<PlayerAction> actions) throws Exception {
        if (value.size() != actions.size()) {
            System.err.println("Erro of same size");
            throw new Exception();
        }

        for (int i = 0; i < actions.size(); i++) {
            PlayerAction pa1 = value.get(i);
            PlayerAction pa2 = actions.get(i);
            if (!pa1.equals(pa2)) {
                return false;
            }
            //if(!pa1.toString().equals(pa2.toString())){
            //    return false;
            //}
        }

        return true;
    }

    private int get_part_size(int size, int cores) {
        if (size <= 1000) {
            return cores * 10;
        }
        return cores * 100;
    }

}
