/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Inputs;

import ai.core.AI;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public abstract class AbstractInput implements iInputsBottomUp {

    private String path;
    private String json;
    private UnitTypeTable utt;
    private IDSLCompiler compiler;

    public AbstractInput() {
        this.utt = new UnitTypeTable();
        compiler = new MainDSLCompiler();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        try {
            this.json = readFileAsJson(path);
        } catch (IOException ex) {
            Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PlayerAction get_action_from_input(iDSL dsl) {
        PlayerAction pa = new PlayerAction();
        GameState game = GameState.fromJSON(this.json, utt);
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

    private String readFileAsJson(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    @Override
    public String getJsonInput() {
        return this.json;
    }

}
