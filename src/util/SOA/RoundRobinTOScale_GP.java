/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA;

import ai.RandomBiasedAI;
import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;

import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.asymmetric.GAB.SandBox.GABScriptChoose;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.competition.capivara.CmabAssymetricMCTS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class RoundRobinTOScale_GP {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;
    private HashMap<BigDecimal, String> scriptsTable;
    String pathTableScripts;
    ICompiler compiler = new MainGPCompiler(); 

    public RoundRobinTOScale_GP(String pathTableScripts) {
        this.pathTableScripts = pathTableScripts;
        buildScriptsTable();
    }

    public boolean run(String tupleAi1, String tupleAi2, Integer IDMatch, Integer Generation, String pathLog, int iMap) throws Exception {
        this.pathTableScripts = pathTableScripts;
        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        log.add("Tupla A1 = " + tupleAi1);
        log.add("Tupla A2 = " + tupleAi2);

        List<String> maps = new ArrayList<>(Arrays.asList(
                //"maps/24x24/basesWorkers24x24A.xml"
                //"maps/32x32/basesWorkers32x32A.xml"
                "maps/8x8/basesWorkers8x8A.xml"
        		//"maps/NoWhereToRun9x8.xml"
        //"maps/BroodWar/(4)BloodBath.scmB.xml"
        ));

        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(iMap), utt);

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 9000;
        }
        if (pgs.getHeight() == 9) {
            MAXCYCLES = 9000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 10000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 11000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 12000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 17000;
        }

        //decompõe a tupla
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = tupleAi1.replace("(", "").replace(")", "").split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }

        ArrayList<Integer> iScriptsAi2 = new ArrayList<>();
        itens = tupleAi2.replace("(", "").replace(")", "").split(";");

        for (String element : itens) {
            iScriptsAi2.add(Integer.decode(element));
        }

        //check for possible updates in scriptsTable
        updateTableIfnecessary();

        //pgs 
        //pgs 
//      AI ai1 = new PGSSCriptChoiceRandom(utt, decodeScripts(utt, iScriptsAi1), "PGSR", 2, 200);
//      AI ai2 = new PGSSCriptChoiceRandom(utt, decodeScripts(utt, iScriptsAi2), "PGSR", 2, 200);
      
//      	AI ai1 = new LightPGSSCriptChoice(utt, decodeScripts(utt, iScriptsAi1),200, "PGSR");
//      	AI ai2 = new LightPGSSCriptChoice(utt, decodeScripts(utt, iScriptsAi2),200, "PGSR");
      
      AI ai1 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 
                                           0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                           new SimpleSqrtEvaluationFunction3(), true, utt, 
                                          "ManagerClosestEnemy", 1,decodeScripts(utt, iScriptsAi1));
      
      AI ai2 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 
                                           0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                           new SimpleSqrtEvaluationFunction3(), true, utt, 
                                          "ManagerClosestEnemy", 1,decodeScripts(utt, iScriptsAi2));
      
//      AI ai1 = new GABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi1), "GAB");
//      AI ai2 = new GABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi2), "GAB");
//      AI ai1 = new SABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi1), "SAB");
//      AI ai2 = new SABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi2), "SAB");

        /*
            Variáveis para coleta de tempo
         */
        double ai1TempoMin = 9999, ai1TempoMax = -9999;
        double ai2TempoMin = 9999, ai2TempoMax = -9999;
        double sumAi1 = 0, sumAi2 = 0;
        int totalAction = 0;

        log.add("---------AIs---------");
        log.add("AI 1 = " + ai1.toString());
        log.add("AI 2 = " + ai2.toString() + "\n");

        log.add("---------Mapa---------");
        log.add("Mapa= " + maps.get(iMap) + "\n");

        //método para fazer a troca dos players
        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 840, 840, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);
        long startTime;
        long timeTemp;
        //System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                totalAction++;
                startTime = System.currentTimeMillis();

                PlayerAction pa1 = ai1.getAction(0, gs);
                //dados de tempo ai1
                timeTemp = (System.currentTimeMillis() - startTime);
                sumAi1 += timeTemp;
                //coleto tempo mínimo
                if (ai1TempoMin > timeTemp) {
                    ai1TempoMin = timeTemp;
                }
                //coleto tempo maximo
                if (ai1TempoMax < timeTemp) {
                    ai1TempoMax = timeTemp;
                }

                startTime = System.currentTimeMillis();
                PlayerAction pa2 = ai2.getAction(1, gs);
                //dados de tempo ai2
                timeTemp = (System.currentTimeMillis() - startTime);
                sumAi2 += timeTemp;
                //coleto tempo mínimo
                if (ai2TempoMin > timeTemp) {
                    ai2TempoMin = timeTemp;
                }
                //coleto tempo maximo
                if (ai2TempoMax < timeTemp) {
                    ai2TempoMax = timeTemp;
                }

                gs.issueSafe(pa1);
                gs.issueSafe(pa2);

                // simulate:
                gameover = gs.cycle();
                //w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //avaliacao de tempo
            duracao = Duration.between(timeInicial, Instant.now());

        } while (!gameover && (gs.getTime() < MAXCYCLES));

        log.add("Total de actions= " + totalAction + " sumAi1= " + sumAi1 + " sumAi2= " + sumAi2 + "\n");

        log.add("Tempos de AI 1 = " + ai1.toString());
        log.add("Tempo minimo= " + ai1TempoMin + " Tempo maximo= " + ai1TempoMax + " Tempo medio= " + (sumAi1 / (long) totalAction));

        log.add("Tempos de AI 2 = " + ai2.toString());
        log.add("Tempo minimo= " + ai2TempoMin + " Tempo maximo= " + ai2TempoMax + " Tempo medio= " + (sumAi2 / (long) totalAction) + "\n");

        log.add("Winner " + Integer.toString(gs.winner()));
        log.add("Game Over");

        if (gs.winner() == -1) {
            System.out.println("Empate!" + ai1.toString() + " vs " + ai2.toString() + " Max Cycles =" + MAXCYCLES + " Time:" + duracao.toMinutes());
        }
        String stMatch = Integer.toString(IDMatch) + "" + Integer.toString(iMap);
        gravarLog(log, tupleAi1, tupleAi2, stMatch, Generation, pathLog);
        //System.exit(0);
        return true;
    }

    public void updateTableIfnecessary() {
        int currentSizeTable = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "SizeTable.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                currentSizeTable = Integer.valueOf(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (scriptsTable.size() < currentSizeTable) {
            buildScriptsTable();
        }

    }

    public List<AI> decodeScripts(UnitTypeTable utt, ArrayList<Integer> iScripts) {
        List<AI> scriptsAI = new ArrayList<>();

        for (Integer idSc : iScripts) {
            //System.out.println("tam tab"+scriptsTable.size());
            //System.out.println("id "+idSc+" Elems "+scriptsTable.get(BigDecimal.valueOf(idSc)));
            scriptsAI.add(buildCommandsIA(utt, scriptsTable.get(BigDecimal.valueOf(idSc))));
        }

        return scriptsAI;
    }

    public static AI buildScript(UnitTypeTable utt, ArrayList<Integer> iRules) {
        //System.out.println("laut");
        TableCommandsGenerator tcg = TableCommandsGenerator.getInstance(utt);
        List<ICommand> commands = new ArrayList<>();
        //System.out.println("sizeeiRules "+iRules.size());
        for (Integer idSc : iRules) {
            //System.out.println("idSc "+idSc);
            commands.add(tcg.getCommandByID(idSc));;
        }
        AI aiscript = new ChromosomeAI(utt, commands, "P1");

        return aiscript;
    }

    public HashMap<BigDecimal, String> buildScriptsTable() {
        scriptsTable = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "/ScriptsTable.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String code = line.substring(line.indexOf(" "), line.length());
                String[] strArray = line.split(" ");
                int idScript = Integer.decode(strArray[0]);
                scriptsTable.put(BigDecimal.valueOf(idScript), code);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return scriptsTable;
    }

    private void gravarLog(ArrayList<String> log, String tupleAi1, String tupleAi2, String IDMatch, Integer Generation, String pathLog) throws IOException {
        if (!pathLog.endsWith("/")) {
            pathLog += "/";
        }
        String nameArquivo = pathLog + "Eval_" + tupleAi1 + "_" + tupleAi2 + "_" + IDMatch + "_" + Generation + ".txt";
        File arqLog = new File(nameArquivo);
        if (!arqLog.exists()) {
            arqLog.createNewFile();
        }
        //abre o arquivo e grava o log
        try {
            FileWriter arq = new FileWriter(arqLog, false);
            PrintWriter gravarArq = new PrintWriter(arq);
            for (String l : log) {
                gravarArq.println(l);
            }

            gravarArq.flush();
            gravarArq.close();
            arq.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI aiscript = new ChromosomeAI(utt, commandsGP , "P1");
        return aiscript;
    }
}
