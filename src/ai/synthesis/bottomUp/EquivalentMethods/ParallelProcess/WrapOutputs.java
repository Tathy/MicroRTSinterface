/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.EquivalentMethods.ParallelProcess;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.List;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public class WrapOutputs {
    private List<PlayerAction> actions;
    private iDSL ast;
    
    public WrapOutputs() {
        actions = new ArrayList<>();
    }
    
    public WrapOutputs(iDSL ast) {        
        this.ast = ast;
        actions = new ArrayList<>();
    }

    public WrapOutputs(List<PlayerAction> actions, iDSL ast) {
        this.actions = actions;
        this.ast = ast;
    }

    public List<PlayerAction> getActions() {
        return actions;
    }

    public void setActions(List<PlayerAction> actions) {
        this.actions = actions;
    }

    public iDSL getAst() {
        return ast;
    }

    public void setAst(iDSL ast) {
        this.ast = ast;
    }
    
    
    
    
}
