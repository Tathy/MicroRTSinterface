/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Inputs;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public interface iInputsBottomUp {
    
    public String get_path_input();
    public PlayerAction get_action_from_input(iDSL dsl);
    public void setPath(String path);
    public String getJsonInput();
}
