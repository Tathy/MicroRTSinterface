/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.professionalScripts;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.asymmetric.PGS.LightPGSSCriptChoiceNoWaits;
import ai.core.AI;
import ai.core.ParameterSpecification;
import model.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens e julian
 */
public class Script_Template extends AbstractionLayerAI {

    protected UnitTypeTable utt;
    HashSet<String> usedCommands = new HashSet<>();
    HashMap<String, Integer> counterByFunction=new HashMap<String, Integer>();
    ICompiler compiler = new MainGPCompiler();

    public Script_Template(UnitTypeTable t_utt) {
        this(t_utt, new AStarPathFinding());
    }

    public Script_Template(UnitTypeTable t_utt, PathFinding a_pf) {
        super(a_pf);
        this.utt = t_utt;
    }

    public void reset() {
        super.reset();
    }

    public void reset(UnitTypeTable a_utt) {
        utt = a_utt;
    }

    @Override
    public AI clone() {
        return new Script_Template(utt, pf);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> parameters = new ArrayList<>();
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));
        return parameters;
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        PlayerAction pa = new PlayerAction();        
        pa = pa.merge(build(player, gs).getAction(player, gs));
              
        return pa;
    }

    private PlayerAction runCode(PlayerAction pa, String code, int player, GameState gs) throws Exception {
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI script = new ChromosomeAI(utt, commandsGP, "", code, usedCommands, counterByFunction);
        return pa.merge(script.getAction(player, gs));
    }

    public AI build(int player, GameState gs) {
    	List<ICommand> commandsGP = new ArrayList<>();
    	
    	if(player == 0) {
    		//commandsGP.addAll(compiler.CompilerCode("train(Worker,1,EnemyDir)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("build(Barrack,1)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("attack(Worker,closest,EnemyDir)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("harvest(1)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("if(HaveQtdUnitsHarversting(15))(attack(Worker,closest,u))", utt));
    		for(int i = 0; i < (Context.getInstance().getScritpsAi1()).size(); i++ ) {
    			commandsGP.addAll(compiler.CompilerCode( Context.getInstance().getScritpsAi1().get(i).toString(), utt));
    			//System.out.println( "lista: " + Context.getInstance().getScritpsAi1().toString() );
    		}
    	} else {
	    	//commandsGP.addAll(compiler.CompilerCode("build(Base,1)", utt));
	    	//commandsGP.addAll(compiler.CompilerCode("harvest(1)", utt));
	    	//commandsGP.addAll(compiler.CompilerCode("train(Ranged,20,EnemyDir)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("attack(Worker,closest,EnemyDir)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("build(Barrack,1)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("attack(Worker,closest,EnemyDir)", utt));
    		//commandsGP.addAll(compiler.CompilerCode("harvest(1)", utt));
    		for(int i = 0; i < (Context.getInstance().getScritpsAi2()).size(); i++ ) {
    			commandsGP.addAll(compiler.CompilerCode( Context.getInstance().getScritpsAi2().get(i).toString(), utt));
    			//System.out.println( Context.getInstance().getScritpsAi2().toString() );
    		}
    	}
    	
    	AI script = new ChromosomeAI(utt, commandsGP, "", "", usedCommands, counterByFunction);
    	//LightPGSSCriptChoiceNoWaits pgs = new LightPGSSCriptChoiceNoWaits(utt,teste ,200,  "Tathy");
    	
    	return script;
    }
    

}
