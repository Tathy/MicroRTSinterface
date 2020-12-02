/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.GrowMethods;

import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.S2DSL;
import ai.synthesis.grammar.dslTree.S3DSL;
import ai.synthesis.grammar.dslTree.S4DSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iS4ConstraintDSL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author rubens
 */
public class BasicGrow extends AbstractGrow {

    private List<iDSL> initial_terminals;
    private boolean phase1_executed;

    public BasicGrow() {
        this.initial_terminals = new ArrayList<>();
        this.phase1_executed = false;
    }

    @Override
    public List<iDSL> grow(List<iDSL> pList) {
        List<iDSL> full_list = new ArrayList<>();
        full_list.addAll(pList);
        if (SettingsBottomUp.isMode_debug()) {
            System.out.println("Total of Initial elements = " + full_list.size());
        }

        if (phase1_executed) {
            //grow by if extension (including then) - make - move after reduce
            List<iDSL> inIFs = get_list_of_if_then(pList);
            full_list.addAll(grow_extension_if(inIFs, pList));

            //grow by for extension (including "u") - make
            //grow combining for's - make (?)
            this.phase1_executed = false;
        } else {
            if (initial_terminals.size() == 0) {
                initial_terminals = pList;
            } else {
                iDSL nAST;
                List<iDSL> t_l = new ArrayList<>();
                for (iDSL frag1 : pList) {
                    for (iDSL frag2 : initial_terminals) {
                        if(is_worth(frag2)){
                            nAST = utils.concatenate(frag1, frag2);
                            t_l.add((iDSL) nAST.clone());                        
                        }
                        
                    }
                }
                pList.addAll(t_l);
            }
            //grow by combination
            full_list.addAll(combine_fragments(pList));
            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("Total of elements after Combination = " + full_list.size());
            }
            //grow by if inclusion             
            full_list.addAll(grow_by_if_inclusion(pList));
            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("Total of elements after IF inclusion = " + full_list.size());
            }
            //grow by for inclusion
            full_list.addAll(grow_by_for_inclusion(pList));
            if (SettingsBottomUp.isMode_debug()) {
                System.out.println("Total of elements after FOR inclusion = " + full_list.size());
            }
            this.phase1_executed = true;
        }

        if (SettingsBottomUp.isMode_debug()) {
            System.out.println("Total of elements before remove= " + full_list.size());
        }

        //include process to remove duplicity - make
        
        System.out.println("Total of elements after remove= " + full_list.size());
        return full_list;
    }

    private List<iDSL> combine_fragments(List<iDSL> pList) {
        List<iDSL> t_list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        iDSL nAST;
        for (iDSL frag1 : pList) {
            for (iDSL frag2 : pList) {
                nAST = utils.concatenate(frag1, frag2);
                if (!set.contains(nAST.translate())) {
                    t_list.add(nAST);
                    set.add(nAST.translate());
                }

            }
        }
        return t_list;
    }

    private List<iDSL> grow_by_if_inclusion(List<iDSL> pList) {
        List<iDSL> t_list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        //for each element in pList, build a if-then
        List<BooleanDSL> condList = utils.getAllConditionals();

        for (iDSL dSL : pList) {
            for (BooleanDSL booleanDSL : condList) {
                iDSL s2 = build_new_if(dSL, booleanDSL);
                if (s2 != null) {
                    if (!set.contains(s2.translate())) {
                        t_list.add(s2);
                        set.add(s2.translate());
                    }
                }
            }
        }

        return t_list;
    }

    private iDSL build_new_if(iDSL dSL, BooleanDSL booleanDSL) {
        if (((S1DSL) dSL).getCommandS1() instanceof CDSL) {
            CDSL C = (CDSL) ((S1DSL) dSL).getCommandS1();
            S2DSL S2 = new S2DSL(booleanDSL.clone(), C.clone());
            return new S1DSL(S2);
        }

        return null;
    }

    private List<iDSL> grow_by_for_inclusion(List<iDSL> pList) {
        List<iDSL> t_list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (iDSL dSL : pList) {
            if (isAbleToBuildFor(dSL)) {
                iDSL s3 = build_new_for(dSL);
                if (s3 != null) {
                    if (!set.contains(s3.translate())) {
                        t_list.add(s3);
                        set.add(s3.translate());
                    }
                }
            }
        }

        return t_list;
    }

    private iDSL build_new_for(iDSL dSL) {
        if (dSL instanceof S1DSL) {
            S1DSL S1 = (S1DSL) dSL;
            if (S1.getCommandS1() instanceof iS4ConstraintDSL) {
                iS4ConstraintDSL t = (iS4ConstraintDSL) S1.getCommandS1().clone();
                S4DSL S4 = new S4DSL(t);
                S3DSL S3 = new S3DSL(S4);
                return new S1DSL(S3);
            }
        } else if (dSL instanceof CDSL) {
            CDSL C = (CDSL) dSL.clone();
            S4DSL S4 = new S4DSL(C);
            S3DSL S3 = new S3DSL(S4);
            return new S1DSL(S3);
        } else if (dSL instanceof S2DSL) {
            S2DSL S2 = (S2DSL) dSL.clone();
            S4DSL S4 = new S4DSL(S2);
            S3DSL S3 = new S3DSL(S4);
            return new S1DSL(S3);
        }

        return null;
    }

    private boolean isAbleToBuildFor(iDSL dSL) {
        if (dSL instanceof S1DSL) {
            S1DSL S1 = (S1DSL) dSL;
            if (S1.getCommandS1() instanceof CDSL
                    || S1.getCommandS1() instanceof S2DSL) {
                return true;
            }
        } else if (dSL instanceof CDSL) {
            return true;
        } else if (dSL instanceof S2DSL) {
            return true;
        }
        return false;
    }

    private List<iDSL> grow_extension_if(List<iDSL> inIFs, List<iDSL> pList) {
        List<iDSL> t_list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (iDSL dSL : pList) {
            if (dSL instanceof S1DSL) {
                S1DSL S1 = (S1DSL) dSL;
                if (S1.getCommandS1() instanceof CDSL) {
                    CDSL cElse = (CDSL) S1.getCommandS1();
                    for (iDSL inIF : inIFs) {
                        S2DSL nIF = (S2DSL) ((S1DSL) inIF).getCommandS1().clone();
                        nIF.setElseCommand((CDSL) cElse.clone());
                        if (!set.contains(nIF.translate())) {
                            t_list.add(new S1DSL(nIF));
                            set.add(nIF.translate());
                        }
                    }
                }
            }

        }
        return t_list;
    }

    private List<iDSL> get_list_of_if_then(List<iDSL> pList) {
        List<iDSL> t_list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();

        for (iDSL dSL : pList) {
            if (dSL instanceof S1DSL) {
                S1DSL S = (S1DSL) dSL;
                if (S.getCommandS1() instanceof S2DSL) {
                    S2DSL S2t = (S2DSL) S.getCommandS1();
                    if (S2t.getElseCommand() == null) {
                        if (!set.contains(dSL.translate())) {
                            t_list.add(dSL);
                            set.add(dSL.translate());
                        }

                    }
                }
            }
        }

        return t_list;
    }

    private boolean is_worth(iDSL frag2) {
        if(frag2.translate().contains("harvest")){
            return true;
        }        
        return false;
    }

}
