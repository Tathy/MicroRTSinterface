/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicBoolean;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.AbstractBooleanAction;
import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Attack;
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
 * @author rubens Julian
 * This condition evaluates if there are X ally units of type t attacking
 */
public class NAllyUnitsAttacking extends AbstractBooleanAction {


	public NAllyUnitsAttacking(List<ICommand> commandsBoolean) {
		this.commandsBoolean=commandsBoolean;
	}


	@Override
	public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
		ResourceUsage resources = new ResourceUsage();
		PhysicalGameState pgs = game.getPhysicalGameState();
		ArrayList<Unit> unitstoApplyWait = new ArrayList<>();
		//update variable resources
		resources = getResourcesUsed(currentPlayerAction, pgs);

		//here we validate if there are x ally units of type t in the map
		
		if(getAllyUnitsAttacking(game, currentPlayerAction, player).size()>=getQuantityFromParam().getQuantity())
			currentPlayerAction=appendCommands(player, game, currentPlayerAction);
		

		return currentPlayerAction;
	}

}
