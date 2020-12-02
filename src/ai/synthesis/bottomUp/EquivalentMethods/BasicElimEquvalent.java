/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.EquivalentMethods;

import ai.synthesis.bottomUp.Inputs.iInputsBottomUp;
import ai.synthesis.bottomUp.settings.SettingsBottomUp;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public class BasicElimEquvalent implements iRemoveEquivalent {

    @Override
    public List<iDSL> removeEquivalents(List<iDSL> pList, List<iInputsBottomUp> inputs) {
        HashMap<iDSL, List<PlayerAction>> outputs = new HashMap<>();

        //compute all responses for each element in pList 
        int monitor = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Removing by Object PlayerAction comparation " + dtf.format(now));

        for (iDSL dSL : pList) {
            monitor++;
            List<PlayerAction> actions = new ArrayList<>(inputs.size());
            int cont = 0;
            for (iInputsBottomUp input : inputs) {
                //get the action for all inputs            
                actions.add(cont, input.get_action_from_input(dSL));
                cont++;
            }
            try {                
                //check if there is the same list of actions in the map
                if (!existSameActionInOutputs(outputs, actions)) {
                    //if there ins't, include in the map.
                    outputs.put(dSL, actions);
                }
            } catch (Exception ex) {
                Logger.getLogger(BasicElimEquvalent.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (monitor % 5000 == 0) {
                System.out.println(dtf.format(LocalDateTime.now()) + "  Total pList verified = " + monitor
                        + " bag with " + outputs.size());
            }

        }

        if (SettingsBottomUp.isMode_debug()) {
            System.out.println("Total of new elements = " + outputs.size());
            System.out.println(dtf.format(LocalDateTime.now()));
            System.out.println("Final Commands");
            for (iDSL dSL : outputs.keySet()) {
                System.out.println("--------" + dSL.translate());
            }
        }

        return new ArrayList<>(outputs.keySet());

    }

    private boolean existSameActionInOutputs(HashMap<iDSL, List<PlayerAction>> outputs, List<PlayerAction> actions) throws Exception {
        //return true, if found or false if not

        for (List<PlayerAction> value : outputs.values()) {
            if (isEqual(value, actions)) {
                return true;
            }
        }

        return false;
    }

    private boolean isEqual(List<PlayerAction> value, List<PlayerAction> actions) throws Exception {
        if (value.size() != actions.size()) {
            System.err.println("Erro of same site");
            throw new Exception();
        }

        for (int i = 0; i < actions.size(); i++) {
            PlayerAction pa1 = value.get(i);
            PlayerAction pa2 = actions.get(i);
            if (!pa1.equals(pa2)) {
                return false;
            }
        }

        return true;
    }

}
