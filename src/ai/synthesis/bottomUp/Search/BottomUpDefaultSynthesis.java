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
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.List;

/**
 *
 * @author rubens
 */
public class BottomUpDefaultSynthesis extends AbstrBottomUpSearch {

    private iGrowCommand grow;
    private iRemoveEquivalent elimEquval;
    private iCorrectness isCorrect;

    public BottomUpDefaultSynthesis(iGrowCommand grow, iRemoveEquivalent elimEquval, iCorrectness isCorrect) {
        this.grow = grow;
        this.elimEquval = elimEquval;
        this.isCorrect = isCorrect;
    }

    @Override
    public void performSearch(List<iInputsBottomUp> inputs) {
        //plist := set of all terminals
        List<iDSL> plist = utils.getAllTerminals();
        for (int i = 1; i <= SettingsBottomUp.get_number_of_bu_iterations(); i++) {
            //plist := grow(plist);
            plist = this.grow.grow(plist);
            //plist := elimEquvalents(plist, inputs);             
//            if(plist.size() > 20000){
//                plist = plist.subList(0, 200);
//            }
            plist = this.elimEquval.removeEquivalents(plist, inputs);

            //forall( p in plist) if(isCorrect(p, inputs, outputs)): return p; 
            //Could I replace isCorrect by who wins who?
            List<iDSL> evaluations = this.isCorrect.verifyCorrectness(plist);

            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("##############################################");
                System.out.println("Total of programs after run " + i + " is " + plist.size());
                for (iDSL dSL : plist) {
                    System.out.println("-----" + dSL.translate());
                }
                System.out.println("##############################################");
            } else {
                System.out.println("##############################################");
                System.out.println("Total of programs after run " + i + " is " + plist.size());
                if (plist.size() < 100) {
                    for (iDSL dSL : plist) {
                        System.out.println("-----" + dSL.translate());
                    }
                } else {
                    for (iDSL dSL : plist.subList(0, 99)) {
                        System.out.println("-----" + dSL.translate());
                    }
                }
                System.out.println("##############################################");
            }
            if (!evaluations.isEmpty()) {
                System.out.println("Search Finished! \n Best individuals founded.");
                for (iDSL evaluation : evaluations) {
                    System.out.println("--- " + evaluation.translate());
                }
            }

        }

    }

}
