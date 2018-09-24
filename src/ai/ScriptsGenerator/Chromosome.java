/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class Chromosome {

    List<ICommand> commands = new ArrayList<>();
    UnitTypeTable utt;

    public Chromosome(UnitTypeTable utt) {
        this.utt = utt;

        //train action
        TrainBasic train = new TrainBasic();
        train.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
        train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
        train.addParameter(new QuantityParam(20)); //add qtd unit
        PriorityPositionParam pos = new PriorityPositionParam();
        pos.addPosition(EnumPositionType.Up);
        pos.addPosition(EnumPositionType.Left);
        //pos.addPosition(EnumPositionType.Right);
        //pos.addPosition(EnumPositionType.Down);
        train.addParameter(pos);
        commands.add(train);
        //harverst action
        HarvestBasic harverst = new HarvestBasic();
        harverst.addParameter(TypeConcrete.getTypeWorker()); //add unit type
        harverst.addParameter(new QuantityParam(3)); //add qtd unit
        commands.add(harverst);
        //attack action
        AttackBasic attack = new AttackBasic();
        attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
        attack.addParameter(new ClosestEnemy()); //add behavior
        commands.add(attack);

    }

    public PlayerAction getAction(int player, GameState gs) {
        PlayerAction currentActions = new PlayerAction();
        PathFinding pf = new AStarPathFinding();

        //simulate one WR
        for (ICommand command : commands) {
            currentActions = command.getAction(gs, player, currentActions, pf, utt);
        }

        return currentActions;
    }
}
