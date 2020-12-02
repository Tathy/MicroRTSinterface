/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.GrowMethods;

import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.EmptyDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.S2DSL;
import ai.synthesis.grammar.dslTree.S3DSL;
import ai.synthesis.grammar.dslTree.S4DSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iBooleanDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iS1ConstraintDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iS4ConstraintDSL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubens
 */
public class HeuristicGrow extends AbstractGrow {

    private List<iDSL> initial_terminals;
    private Random rand;
    private HashMap<String, List<iDSL>> stored_ast;
    private BuilderDSLTreeSingleton builder;

    public HeuristicGrow() {
        this.initial_terminals = new ArrayList<>();
        this.rand = new Random();
        this.stored_ast = new HashMap<>();
        prepare_stored_list();
        this.builder = BuilderDSLTreeSingleton.getInstance();
    }

    @Override
    public List<iDSL> grow(List<iDSL> pList) {
        try {
            if (pList.size() > 1 || initial_terminals.isEmpty()) {
                pList.clear();
                //add new b, terminals 
                pList.addAll(utils.getAllBTerminals());
                //add all c, terminals.
                pList.addAll(utils.getAllCTerminals());
                Collections.shuffle(pList);
                initial_terminals = pList;
            }
            iDSL ast = get_randomly(pList);
            //check if the algorithms starts
            if (pList.size() == 1) {
                //select between - add new rule or combine old asts.
                if (Math.random() < 0.5) { // add new rule
                    ast = add_new_rule(pList.get(0));
                } else { // combine old

                }

            }

            return prepare_return(ast);
        } catch (Exception ex) {
            Logger.getLogger(HeuristicGrow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

    private iDSL get_randomly(List<iDSL> pList) {
        return pList.get(rand.nextInt(pList.size() - 1));
    }

    private List<iDSL> prepare_return(iDSL ast) throws Exception {
        List<iDSL> ele = new ArrayList<>();
        if (ast instanceof S1DSL) {
            ele.add(ast);
        } else {
            if (ast instanceof iS1ConstraintDSL) {
                ele.add(new S1DSL((iS1ConstraintDSL) ast));
            } else {
                throw new Exception("Problem in the ast selected. Method "
                        + "prepare_return, class " + this.getClass().getCanonicalName());
            }

        }
        return ele;
    }

    private iDSL add_new_rule(iDSL ast) {
        if (ast instanceof CDSL) {
            ast = add_new_C_(ast);
        } else if (ast instanceof BooleanDSL) {
            ast = add_new_S2(ast);
        } else if (ast instanceof S1DSL) {
            ast = add_piece_in_S1DSL(ast);
        } else if (ast instanceof S2DSL) {
            ast = add_piece_in_S2DSL(ast);
        } else if (ast instanceof S3DSL) {
            ast = add_piece_in_S3DSL(ast);
        } else if (ast instanceof S4DSL) {
            ast = add_piece_in_S4DSL(ast);
        }

        return ast;
    }

    private void prepare_stored_list() {
        //empty
        EmptyDSL ep = new EmptyDSL();
        List<iDSL> l1 = new ArrayList<>();
        l1.add(ep);
        this.stored_ast.put("epsilon", l1);
        //S1
        this.stored_ast.put("S1", Collections.EMPTY_LIST);
        //S2
        this.stored_ast.put("S2", Collections.EMPTY_LIST);
        //S3
        this.stored_ast.put("S3", Collections.EMPTY_LIST);
        //S4
        this.stored_ast.put("S4", Collections.EMPTY_LIST);
        //Boolean
        this.stored_ast.put("B", Collections.EMPTY_LIST);
        //C
        this.stored_ast.put("C", Collections.EMPTY_LIST);

    }

    /**
     * Receives a CDSL and add a new CDSL in the final points.
     *
     * @param ast - CDSL
     * @return the initial CDSL + a new one.
     */
    private iDSL add_new_C_(iDSL ast) {
        CDSL C = (CDSL) ast.clone();
        CDSL cinc = C.getNextCommand();
        while (cinc.getNextCommand() != null) {
            cinc = cinc.getNextCommand();
        }
        cinc.setNextCommand(builder.buildRandomlyCGramar(false));
        return C;
    }
    
    /**
     * Receives a BooleanDSL and build a S2DSL(If) without then.
     * @param ast - BooleanDSL
     * @return a S2DSL just with the conditional and then/else as null.
     */
    private iDSL add_new_S2(iDSL ast) {
        S2DSL S2 = new S2DSL((iBooleanDSL) ast.clone(), null);
        return S2;
    }

    /**
     * Receives a S1DSL and add a new fragment
     * @param ast
     * @return 
     */
    private iDSL add_piece_in_S1DSL(iDSL ast) {
        S1DSL S1 = (S1DSL) ast.clone();
        if(S1.getCommandS1() == null){
            
        }
        return null;
    }

    private iDSL add_piece_in_S2DSL(iDSL ast) {
        S2DSL S2 = (S2DSL) ast.clone();
        if(S2.getBoolCommand() == null){
            S2.setBoolCommand(builder.buildBGrammar(false));
        }else if(S2.getThenCommand() == null){
            S2.setThenCommand(builder.buildTerminalCGrammar(false));
        }else if(S2.getElseCommand() == null){
            S2.setElseCommand(builder.buildTerminalCGrammar(false));
        }
        return S2;
    }

    private iDSL add_piece_in_S3DSL(iDSL ast) {
        S3DSL S3 = (S3DSL) ast.clone();
        S3.setForCommand((S4DSL) add_piece_in_S4DSL(S3.getForCommand()));
        return S3;
    }

    private iDSL add_piece_in_S4DSL(iDSL ast) {
        S4DSL S4 = (S4DSL) ast.clone();
        if(S4.getFirstDSL() == null){
            if(Math.random() < 0.5){ // add C
                S4.setFirstDSL(builder.buildTerminalCGrammar(true));
            }else{ // add S2
                S4.setFirstDSL(builder.buildS2Grammar(true));
            }
        }else if(S4.getNextCommand() == null){
            S4.setNextCommand(builder.buildS4RandomlyGrammar(true));
        }else if(S4.getNextCommand() != null){
            S4.setNextCommand((S4DSL) add_new_rule(S4.getNextCommand()));
        }
        
        return S4;
    }
}
