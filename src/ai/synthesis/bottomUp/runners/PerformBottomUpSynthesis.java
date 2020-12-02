/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.runners;

import ai.synthesis.bottomUp.Correctness.CorrectByFPBattle;
import ai.synthesis.bottomUp.Correctness.CorrectByGroupOfScriptsOpponents;
import ai.synthesis.bottomUp.Correctness.iCorrectness;
import ai.synthesis.bottomUp.EquivalentMethods.BasicElimEquvalent;
import ai.synthesis.bottomUp.EquivalentMethods.BasicElimEquvalentParallel;
import ai.synthesis.bottomUp.EquivalentMethods.iRemoveEquivalent;
import ai.synthesis.bottomUp.GrowMethods.BasicGrow;
import ai.synthesis.bottomUp.GrowMethods.HeuristicGrow;
import ai.synthesis.bottomUp.GrowMethods.ReducedGrow;
import ai.synthesis.bottomUp.GrowMethods.iGrowCommand;
import ai.synthesis.bottomUp.Inputs.InputsForBT;
import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.bottomUp.Search.BottomUpDefaultSynthesis;
import ai.synthesis.bottomUp.Search.BottomUpSynthesis;
import ai.synthesis.bottomUp.Search.iBottonUpSearch;
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class PerformBottomUpSynthesis {
        
    public static void main(String[] args){
        SettingsBottomUp.setMode_debug(true);                
        if(SettingsBottomUp.isMode_debug()){
            System.err.println("---------MODE DEBUG ACTIVATED!---------");            
        }else{
            System.err.println("---------MODE OFFICIAL ACTIVATED!---------");
        }
        System.err.println("Map "+ SettingsBottomUp.get_map());
        //list of inputs
        List<iInputsBottomUp> inputs = get_all_inputs();
        
        //first BT search 
//        iGrowCommand grow = new BasicGrow();
//        //iRemoveEquivalent elimEquval = new BasicElimEquvalent();
//        iRemoveEquivalent elimEquval = new BasicElimEquvalentParallel();
//        iCorrectness isCorrect = new CorrectByFPBattle();
//        iBottonUpSearch btSearch = new BottomUpSynthesis(grow, elimEquval, isCorrect);
//        btSearch.performSearch(inputs);
        
        //fundamental BU Search - reduced DSL.        
//        iGrowCommand grow = new ReducedGrow();        
//        iRemoveEquivalent elimEquval = new BasicElimEquvalentParallel();
//        iCorrectness isCorrect = new CorrectByGroupOfScriptsOpponents();
//        iBottonUpSearch btSearch = new BottomUpDefaultSynthesis(grow, elimEquval, isCorrect);
//        btSearch.performSearch(inputs);
        
        //fundamental BU Search - Grow search guided by heurist. Full DSL.        
        iGrowCommand grow = new HeuristicGrow();        
        iRemoveEquivalent elimEquval = new BasicElimEquvalentParallel();
        iCorrectness isCorrect = new CorrectByGroupOfScriptsOpponents();
        iBottonUpSearch btSearch = new BottomUpDefaultSynthesis(grow, elimEquval, isCorrect);
        btSearch.performSearch(inputs);
    }

    private static List<iInputsBottomUp> get_all_inputs() {
        List<iInputsBottomUp> inputs = new ArrayList<>();
        String path = System.getProperty("user.dir").concat("/inputs_synthesis/");
        File folder = new File(path);
        for (String f : folder.list()) {
            iInputsBottomUp inp = new InputsForBT();
            inp.setPath(path.concat(f));
            inputs.add(inp);
        }
        
        return inputs;
    }
    
    
}
