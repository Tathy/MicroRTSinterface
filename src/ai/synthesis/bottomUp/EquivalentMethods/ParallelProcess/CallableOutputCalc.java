/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.EquivalentMethods.ParallelProcess;

import ai.core.AI;
import ai.synthesis.bottomUp.Inputs.AbstractInput;
import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class CallableOutputCalc implements Callable<WrapOutputs> {

    private List<iInputsBottomUp> inputs;
    private iDSL ast;
    private UnitTypeTable utt;
    private IDSLCompiler compiler;

    public CallableOutputCalc(List<iInputsBottomUp> inputs, iDSL ast, UnitTypeTable utt) {
        this.inputs = inputs;
        this.ast = ast;
        this.utt = utt;
        compiler = new MainDSLCompiler();
    }

    @Override
    public WrapOutputs call() throws Exception {
        List<PlayerAction> actions = new ArrayList<>(inputs.size());
        int count = 0;
        for (iInputsBottomUp input : inputs) {
            //get the action for all inputs            
            actions.add(count, get_action_from_input(ast, input.getJsonInput()));
            count++;
        }
        return new WrapOutputs(actions, ast);
    }

    private PlayerAction get_action_from_input(iDSL dsl, String json) {
        PlayerAction pa = new PlayerAction();

        GameState game = GameState.fromJSON(json, utt);
        AI ai = buildCommandsIA(utt, dsl);
        if (game.canExecuteAnyAction(0)) {
            try {
                pa = ai.getAction(0, game);
            } catch (Exception ex) {
                Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                pa = ai.getAction(1, game);
            } catch (Exception ex) {
                Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return pa;
    }

    private AI buildCommandsIA(UnitTypeTable utt, iDSL code) {
        HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
        List<ICommand> commandsDSL = compiler.CompilerCode(code, utt);
        AI aiscript = new DslAI(utt, commandsDSL, "P1", code, counterByFunction);
        return aiscript;
    }

}
