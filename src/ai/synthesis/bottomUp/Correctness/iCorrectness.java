/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Correctness;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.List;

/**
 *
 * @author rubens
 */
public interface iCorrectness {
    
    public List<iDSL> verifyCorrectness(List<iDSL> pList);
    
}
