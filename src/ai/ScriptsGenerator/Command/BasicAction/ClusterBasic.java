/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Attack;
import ai.abstraction.Move;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens & Julian
 */
public class ClusterBasic extends AbstractBasicAction {

	int _cenX=0;
	int _cenY=0;
    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
        ResourceUsage resources = new ResourceUsage();
        PhysicalGameState pgs = game.getPhysicalGameState();
        //update variable resources
        resources = getResourcesUsed(currentPlayerAction, pgs);
        
        if(((ArrayList<Unit>)getPotentialUnits(game, currentPlayerAction, player)).size()>0)
        	CalcCentroide((ArrayList<Unit>)getPotentialUnits(game, currentPlayerAction, player));
        
       for(Unit unAlly : getPotentialUnits(game, currentPlayerAction, player)){
    	   
            //pick one enemy unit to set the action
//            int pX = getCoordinatesFromParam().getX();  
//            int pY = getCoordinatesFromParam().getY();
    	   
    	   	//pick the positions
    	   
            
            if (game.getActionAssignment(unAlly) == null && unAlly != null) {
                
            	UnitAction uAct = null;
            	UnitAction move = pf.findPathToAdjacentPosition(unAlly, _cenX+_cenY*pgs.getWidth(), game, resources);            	
            	if (move!=null && game.isUnitActionAllowed(unAlly, move));
                	uAct = move;

                if (uAct != null && (uAct.getType() == 5 || uAct.getType() == 1)) {
                    currentPlayerAction.addUnitAction(unAlly, uAct);
                    resources.merge(uAct.resourceUsage(unAlly, pgs));
                }
            }
        }
        return currentPlayerAction;
    }

    @Override
    public String toString() {
        String listParam = "Params:{";
        for (IParameters parameter : getParameters()) {
            listParam += parameter.toString()+",";
        }
        //remove the last comma.
        listParam = listParam.substring(0, listParam.lastIndexOf(","));
        listParam += "}";
        
        return "{MoveToCoordinatesBasic:{" + listParam+"}}";
    }
    
    private void CalcCentroide(ArrayList<Unit> unitAllys) {
        int x = 0, y = 0;
        for (Unit un : unitAllys) {
            x += un.getX();
            y += un.getY();
        }
        x = x / unitAllys.size();
        y = y / unitAllys.size();
        _cenX = x;
        _cenY = y;
    }

}
