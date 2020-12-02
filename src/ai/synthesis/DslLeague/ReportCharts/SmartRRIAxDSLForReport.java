/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts;


import Standard.StrategyTactics;
import ai.CMAB.A3NNoWait;
import ai.RandomBiasedAI;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.competition.newBotsEval.botEmptyBase;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import static util.SOA.RoundRobinClusterLeve_Cluster.decodeScripts;

public class SmartRRIAxDSLForReport implements Callable<Float>{

    IDSLCompiler compiler = new MainDSLCompiler();    
    AI ai;
    String sIA1;    
    float result;
    int winner;
    int idIA;
    boolean changeSide;
    //Smart Evaluation Settings
    String initialState;
    private final int CYCLES_LIMIT = 200;    
    String map;
    
    public float getResult() {
        return result;
    }

    public int getWinner() {
        return winner;
    }

    public SmartRRIAxDSLForReport(String sIA1, int idIA, boolean side, String map) {
        this.sIA1 = sIA1;        
        this.idIA = idIA;
        this.changeSide = side;
        this.map = map;        
    }

    @Override
    public Float call() throws Exception {        
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(map, utt);        
        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 4000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 3000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 4000;
            //MAXCYCLES = 1000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 5000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 6000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 8000;
        }
        
        List<AI> ais = new ArrayList(Arrays.asList(new AIWithComputationBudget[]{
            new LightRush(utt),
            new WorkerRush(utt),
            new RangedRush(utt),
            new HeavyRush(utt),
            new NaiveMCTS(utt),            
            new A3NNoWait(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
                decodeScripts(utt, "0;"), "A3N"),
            new StrategyTactics(utt)
        }));        
        AI ai1 = new botEmptyBase(utt, sIA1, "bot");
        AI ai2 = ais.get(idIA);
        if(changeSide){
            AI aiT = ai1;
            ai1 = ai2;
            ai2 = aiT;
        }

        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);        
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                if (smartEvaluationForStop(gs)) {
                    result = 0.5f;
                    return result;
                }
                // simulate:
                gameover = gs.cycle();
                //w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.err.println("ai.synthesis.runners.roundRobinLocal.RoundRobinGrammarAgainstGrammar.run() " + e.getMessage());
                }
            }

        } while (!gameover && (gs.getTime() <= MAXCYCLES));
        SimpleSqrtEvaluationFunction3 ltd3 = new SimpleSqrtEvaluationFunction3();
        winner = gs.winner();
        //w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));        
        
        result = 0.0f;
        if (winner == -1) {
            result = 0.5f;            
        }else if(changeSide){
            if (winner == 1) {
                result = 1.0f;
            }
        }else{
            if (winner == 0) {
                result = 1.0f;
            }
        }  
        return result;
    }

    private AI buildCommandsIA(UnitTypeTable utt, iDSL code) {
        HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
        List<ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI aiscript = new DslAI(utt, commandsGP, "P1", code, counterByFunction);
        return aiscript;
    }    

    /**
     * Check if after a quantity of cycles (defined by CYCLES_LIMIT) the game
     * can be stopped. If the state of the game continues the same for more than
     * CYCLES_LIMIT, the function will return true.
     *
     * @param gs - Game to be considered.
     * @return True - If the game can be stopped and defined as draw. False if the 
     * game changed after CYCLES_LIMIT games cycles.
     */
    private boolean smartEvaluationForStop(GameState gs) {
        if (gs.getTime() == 0) {            
            String cleanState = cleanStateInformation(gs);
            this.initialState = cleanState;
        } else if (gs.getTime() % CYCLES_LIMIT == 0) {
            String cleanState = cleanStateInformation(gs);
            if(cleanState.equals(initialState)){
                //System.out.println("** Smart Stop activate.\n Original state =\n"+initialState+
                //        " verified same state at \n"+cleanState);
                return true;
            }else{
                initialState = cleanState;
            }
        }

        return false;
    }

    private String cleanStateInformation(GameState gs) {
        String sGame = gs.toString().replace("\n", "");
        sGame = sGame.substring(sGame.indexOf("PhysicalGameState:")); 
        sGame = sGame.replace("PhysicalGameState:", "").trim();
        return sGame;
    }

}
