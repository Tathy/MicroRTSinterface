/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Utils;

import ai.synthesis.dslForScriptGenerator.DSLTableGenerator.FunctionsforDSL;
import ai.synthesis.dslForScriptGenerator.DSLTableGenerator.ParameterDSL;
import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.CommandDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iCommandDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author rubens
 */
public class DSLManager {

    private static DSLManager unInstance;

    private final FunctionsforDSL grammar;    

    /**
     * The constructor is private to keep the singleton working properly
     */
    private DSLManager() {
        this.grammar = new FunctionsforDSL();        
    }

    /**
     * get a instance of the class.
     *
     * @return the singleton instance of the class
     */
    public static synchronized DSLManager getInstance() {
        if (unInstance == null) {
            unInstance = new DSLManager();
        }
        return unInstance;
    }

    /**
     * Method used to return all terminals (CDSL) of the grammar.
     *
     * @return a list of all terminals in the grammar.
     */
    public List<iDSL> getAllTerminals() {
        List<iDSL> terminals = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        for (FunctionsforDSL command : grammar.getBasicFunctionsForGrammar()) {
            commands.addAll(mountByCommand(command));
        }
        //build all CDSL
        for (String com : commands) {
            iCommandDSL c = new CommandDSL(com);
            terminals.add(new S1DSL(new CDSL(c)));
        }

        return terminals;
    }
    
    
    public List<iDSL> getAllCTerminals() {
        List<iDSL> terminals = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        for (FunctionsforDSL command : grammar.getBasicFunctionsForGrammar()) {
            commands.addAll(mountByCommand(command));
        }
        //build all CDSL
        for (String com : commands) {
            iCommandDSL c = new CommandDSL(com);
            terminals.add(new CDSL(c));
        }

        return terminals;
    }
    
    public List<iDSL> getAllBTerminals() {
        List<iDSL> terminals = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        for (FunctionsforDSL command : grammar.getConditionalsForGrammar()) {
            commands.addAll(mountByCommand(command));
        }
        //build all B's
        for (String com : commands) {
            BooleanDSL b = new BooleanDSL(com);            
            terminals.add(b);
        }

        return terminals;
    }
        

    private List<String> mountByCommand(FunctionsforDSL command) {
        List<String> actual = new ArrayList<>();
        actual.add(command.getNameFunction() + "(");
        for (ParameterDSL p : command.getParameters()) {
            List<String> current = new ArrayList<>();
            for (String tCommand : actual) {
                if (p.getDiscreteSpecificValues() == null) {
                    for (int i = (int) p.getInferiorLimit(); i <= (int) p.getSuperiorLimit(); i++) {
                        current.add(tCommand + i + ",");
                    }
                } else {
                    for (String discreteSpecificValue : p.getDiscreteSpecificValues()) {
                        current.add(tCommand + discreteSpecificValue + ",");
                    }
                }
            }
            actual = current;
        }
        List<String> fixedCommand = new ArrayList<>();
        for (String c : actual) {
            fixedCommand.add(c.substring(0, c.length() - 1).concat(")"));
        }

        return fixedCommand;
    }

    private Collection<? extends String> mountByConditional(FunctionsforDSL fCond) {
        List<String> actual = new ArrayList<>();
        actual.add(fCond.getNameFunction() + "(");
        for (ParameterDSL p : fCond.getParameters()) {
            List<String> current = new ArrayList<>();
            for (String tCommand : actual) {
                if (p.getDiscreteSpecificValues() == null) {
                    for (int i = (int) p.getInferiorLimit(); i <= (int) p.getSuperiorLimit(); i++) {
                        current.add(tCommand + i + ",");
                    }
                } else {
                    for (String discreteSpecificValue : p.getDiscreteSpecificValues()) {
                        current.add(tCommand + discreteSpecificValue + ",");
                    }
                }
            }
            actual = current;
        }
        return actual;
    }

    public List<BooleanDSL> getAllConditionals() {
        List<String> actual = new ArrayList<>();
        for (FunctionsforDSL fCond : grammar.getConditionalsForGrammar()) {
            actual.addAll(mountByConditional(fCond));
        }

        List<BooleanDSL> fixedCommand = new ArrayList<>();
        for (String c : actual) {
            fixedCommand.add(new BooleanDSL(c.substring(0, c.length() - 1).concat(")")));
        }

        return fixedCommand;
    }

    public iDSL concatenate(iDSL ast1, iDSL ast2) {
        if (ast1 instanceof S1DSL) {
            S1DSL S1 = (S1DSL) ast1.clone();
            if (((S1DSL) ast1).getCommandS1() instanceof CDSL) {
                if (((S1DSL) ast2).getCommandS1() instanceof CDSL) {
                    CDSL cinc = ((CDSL) S1.getCommandS1());
                    while (cinc.getNextCommand() != null) {
                        cinc = cinc.getNextCommand();
                    }
                    cinc.setNextCommand((CDSL) ((S1DSL) ast2).getCommandS1().clone());
                    return S1;
                }
            }

            //improve - make
            S1DSL inc = S1;
            while (inc.getNextCommand() != null) {
                inc = inc.getNextCommand();
            }
            inc.setNextCommand((S1DSL) ast2.clone());

            return S1;
        }
        //include concat for all variations - make (?)

        return (iDSL) ast1.clone();
    }

}
