/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Search;

import ai.synthesis.bottomUp.Correctness.iCorrectness;
import ai.synthesis.bottomUp.EquivalentMethods.iRemoveEquivalent;
import ai.synthesis.bottomUp.GrowMethods.iGrowCommand;
import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.bottomUp.Utils.DSLManager;
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class BottomUpSynthesis extends AbstrBottomUpSearch{
    private iGrowCommand grow;
    private iRemoveEquivalent elimEquval;
    private iCorrectness isCorrect;

    public BottomUpSynthesis(iGrowCommand grow, iRemoveEquivalent elimEquval, iCorrectness isCorrect) {
        this.grow = grow;
        this.elimEquval = elimEquval;
        this.isCorrect = isCorrect;
    }

    @Override
    public void performSearch(List<iInputsBottomUp> inputs) {
        //plist := set of all terminals
        List<iDSL> plist = utils.getAllTerminals();
        int cont = 0;
        while (true) {            
            cont++;
            //plist := grow(plist);
            plist = this.grow.grow(plist);
            //plist := elimEquvalents(plist, inputs);             
            if(plist.size() > 20000){
                plist = plist.subList(0, 200);
            }
            plist = this.elimEquval.removeEquivalents(plist, inputs);
            
            //re-execute grow to include new phase of inclusion
            plist = this.grow.grow(plist);            
            plist = this.elimEquval.removeEquivalents(plist, inputs);            
            
            //forall( p in plist) if(isCorrect(p, inputs, outputs)): return p; 
            //Could I replace isCorrect by who wins who?
            plist = this.isCorrect.verifyCorrectness(plist);
            
            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("##############################################");
                System.out.println("Total of programs after run "+cont+" is "+plist.size());
                for (iDSL dSL : plist) {
                    System.out.println("-----"+dSL.translate());
                }
                System.out.println("##############################################");
            }else{
                System.out.println("##############################################");
                System.out.println("Total of programs after run "+cont+" is "+plist.size());
                if(plist.size() < 100){
                    for (iDSL dSL : plist) {
                        System.out.println("-----"+dSL.translate());
                    }
                }else{
                    for (iDSL dSL : plist.subList(0, 99)) {
                        System.out.println("-----"+dSL.translate());
                    }
                }
                System.out.println("##############################################");
            }
            
        }
        
    }
    
}
