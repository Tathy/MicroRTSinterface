/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Agent;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.AgentJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.MapJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.TestJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Map;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class runDataReports {
    private EntityManagerFactory factory;
    private HashMap<String, Test> log_tests;
    private AgentJpaController jpaAgent;
    private TestJpaController jpaTest;
    private MapJpaController jpaMap;
    
    private final int QTD_RR = 5;

    public runDataReports(EntityManagerFactory factory) {
        this.factory = factory;
        this.log_tests = new HashMap<>();
        this.jpaAgent = new AgentJpaController(factory);
        this.jpaTest = new TestJpaController(factory);
        this.jpaMap = new MapJpaController(factory);
    }

    public void perform_actions_for_test(String path_test, String name_file) throws Exception{        
        System.out.println(path_test);
        System.out.println(name_file);
        Test test = check_test_existence(name_file);
        List<Agent> agents = get_agents_by_test_iteration(path_test, test);
        if(agents.size() > 0){
            System.out.println(" # Running tests for "+test.getNameTest()+" map "+test.getMapId().getPath());
            test.add_agents(agents);
            perform_battles(agents);
            increment_test(test);
            System.out.println("  # Finished tests for "+test.getNameTest()+" map "+test.getMapId().getPath());
            System.out.println("___________________________________________________________________________");
        }
    }
    /**
     * Check if the test exists in the pool. It not, it will create a instance, store
     * and return the instance to be used.      
     * @param name_file name of the file used as key in the store.
     * @return a instance of the class Test
     */
    private Test check_test_existence(String name_file) {
        if(this.log_tests.containsKey(name_file)){
            return log_tests.get(name_file);
        }else{
            //create a new log, save and store it in the repository.
            int id = Integer.decode(name_file.substring(name_file.lastIndexOf("-")+1).replace(".out", ""));
            Test nTest = new Test(id);
            nTest.setNameTest(name_file);
            nTest.setLastIteration(0);
            nTest.setMapId(getMapFromNameFile(name_file));
            try {
                jpaTest.create(nTest);
                this.log_tests.put(name_file, nTest);                
                return nTest;
            } catch (Exception ex) {
                Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return null;
    }
    /**
     * Returns an instance of the map based on the file.
     * @param name_file with the information of the map size.
     * @return the correct instance of the map.
     */
    private Map getMapFromNameFile(String name_file) {     
        name_file = name_file.trim();
        if (name_file.contains("LB")) {
            return jpaMap.findMap(2);
        }else if(name_file.contains("L8")) {
            return jpaMap.findMap(1);
        }else if(name_file.contains("L9")) {
            return jpaMap.findMap(3);
        }else if(name_file.contains("L16")) {
            return jpaMap.findMap(5);
        }
        return jpaMap.findMap(4);
    }

    /**
     * Based in the file (path_test), this method read the file and get the data 
     * from the four agents considering the iteration of the test.
     * @param path_test
     * @param test
     * @return 
     */
    private List<Agent> get_agents_by_test_iteration(String path_test, Test test) {
        List<Agent> agents = new ArrayList<>();
        
        File arq = new File(path_test);
        String line;
        try {            
            BufferedReader readArq = new BufferedReader(new FileReader(arq));
            line = readArq.readLine();
            
            while (line != null) {        
                if(line.contains("Initial  Actors generation")){
                    int iterat = Integer.decode(line.replace("Initial  Actors generation","").trim());
                    if(iterat == test.getLastIteration()){
                        agents.add(build_agent(test, readArq.readLine()));
                        agents.add(build_agent(test, readArq.readLine()));
                        agents.add(build_agent(test, readArq.readLine()));
                        agents.add(build_agent(test, readArq.readLine()));
                    }
                }
                line = readArq.readLine();
            }

            readArq.close();    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return agents;
    }
    /**
     * Based on the string with the code, create an agent instance, store it 
     * and return.
     * @param test
     * @param data
     * @return 
     */
    private Agent build_agent(Test test, String data) {
        data = data.replace("   ** ", "");
        String type = data.substring(0, data.indexOf(" "));
        String code = data.replace(type+" ", "").trim();
        Agent agent = new Agent();
        agent.setIdTest(test);
        agent.setIteration(test.getLastIteration());
        agent.setCode(code);
        agent.setType(type);
        try {
            jpaAgent.create(agent);
            return agent;
        } catch (Exception ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Increment the iteration to control the tests and save the test 
     * with all agents and dependencies.
     * @param test 
     */
    private void increment_test(Test test) {
        test.increment_iteration();
        try {
            jpaTest.edit(test);
        } catch (Exception ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perform_battles(List<Agent> agents) throws Exception {
        for (Agent agent : agents) {
            System.out.println("  * Running tests for agent "+ agent.getType()+" "+ agent.getCode() +" iteration "+ agent.getIdTest().getLastIteration());
            run_battles_against_IAS(agent);            
            if(agent.getType().equals("MainPlayer")){
                run_league_battle(agent, agents);
            }
        }
    }

    private void run_battles_against_IAS(Agent agent) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //against LIR        
        Double score = agent.getLir();
        Collection<Callable<Float>> tasks = new ArrayList<>();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 0, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 0, false, agent.getIdTest().getMapId().getPath()));            
        }
        List<Future<Float>> taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setLir(score);
        
        //against WOR        
        score = agent.getWor();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 1, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 1, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setWor(score);
        
        //against rar
        score = agent.getRar();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 2, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 2, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setRar(score);
        
        //against HER
        score = agent.getHer();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 3, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 3, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setHer(score);
        
        //against NS
        score = agent.getNs();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 4, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 4, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setNs(score);
        
        //against A3N
        score = agent.getA3n();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 5, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 5, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setA3n(score);
        
        //against STT
        score = agent.getStt();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 6, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRRIAxDSLForReport(agent.getCode(), 6, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setStt(score);
        
        executor.shutdown();
        
        jpaAgent.edit(agent);
    }

    private void run_league_battle(Agent agent, List<Agent> agents) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Collection<Callable<Float>> tasks = new ArrayList<>();
        
        String meCode = null;
        String le1 = null;
        String le2 = null;
        for (Agent enemy : agents) {
            if(enemy.getType().equals("MainExploiter")){
                meCode = enemy.getCode();
            }else if(enemy.getType().equals("LeagueExploiter")){
                if(le1 == null){
                    le1 = enemy.getCode();
                }else{
                    le2 = enemy.getCode();
                }
            }
        }
        //against MainExploiter        
        Double score = agent.getMe();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), meCode, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), meCode, false, agent.getIdTest().getMapId().getPath()));            
        }
        List<Future<Float>> taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setMe(score);
        
        //against LE1
        score = agent.getLe1();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), le1, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), le1, false, agent.getIdTest().getMapId().getPath()));
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setLe1(score);
        
        //against LE2
        score = agent.getLe2();
        tasks.clear();
        for (int i = 0; i < QTD_RR; i++) {            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), le2, true, agent.getIdTest().getMapId().getPath()));            
            tasks.add(new SmartRR_DSLxDSLForReport(agent.getCode(), le2, false, agent.getIdTest().getMapId().getPath()));            
        }
        taskFutureList =executor.invokeAll(tasks);
        for (Future<Float> future : taskFutureList) {
            score += future.get();
        }
        agent.setLe2(score);  
        
        executor.shutdown();
        jpaAgent.edit(agent);
    }

    
}
