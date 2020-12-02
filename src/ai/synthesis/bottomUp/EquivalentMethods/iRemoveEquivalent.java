/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.EquivalentMethods;

import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.List;

/**
 *
 * @author rubens
 */
public interface iRemoveEquivalent {
    
    public List<iDSL> removeEquivalents(List<iDSL> pList, List<iInputsBottomUp> inputs);
}
