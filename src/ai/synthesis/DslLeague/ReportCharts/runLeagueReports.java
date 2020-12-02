/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Agent;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.AgentJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.LeagueInfoJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.MapJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.PlayerLeagueJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.TestJpaController;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.LeagueInfo;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Map;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.PlayerLeague;
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
public class runLeagueReports {
    private EntityManagerFactory factory;      
    private TestJpaController jpaTest;    
    private LeagueInfoJpaController jpaLeague;
    private PlayerLeagueJpaController jpaplayerLeague;
    private int id_league;
    private int id_player;

    public runLeagueReports(EntityManagerFactory factory) {
        this.factory = factory;             
        this.jpaTest = new TestJpaController(factory);        
        this.jpaLeague = new LeagueInfoJpaController(factory);
        this.jpaplayerLeague = new PlayerLeagueJpaController(factory);
        this.id_league = 0;
        this.id_player = 0;
    }

    public void perform_actions_for_test(String path_test, String name_file) throws Exception{        
        System.out.println(path_test);
        System.out.println(name_file);
        Test test = check_test_existence(name_file);   
        read_league_for_test(path_test, test);
        
    }
    /**
     * Check if the test exists in the pool. It not, it will create a instance, store
     * and return the instance to be used.      
     * @param name_file name of the file used as key in the store.
     * @return a instance of the class Test
     */
    private Test check_test_existence(String name_file) {
        for (Test test : jpaTest.findTestEntities()) {
            if(test.getNameTest().equals(name_file)){
                return test;
            }
        }
        
        return null;
    }

    private void read_league_for_test(String path_test, Test test) throws Exception {
        int iterat = 0;
        LeagueInfo league = new LeagueInfo(getId_league());
        league.setIteration(iterat);        
        List<LeagueInfo> l_league = new ArrayList();
        File arq = new File(path_test);
        String line;
        try {            
            BufferedReader readArq = new BufferedReader(new FileReader(arq));
            line = readArq.readLine();
            
            while (line != null) {        
                if(line.contains("@@@  League Payoff Players")){
                    jpaLeague.create(league);     
                    line = readArq.readLine();
                    while(!line.equals("________________________________________________________")){
                        PlayerLeague player = new PlayerLeague(getId_player());
                        player.setIdLeague(league);
                        player.setFullLog(line.replace("     @ ", "").trim()); 
                        jpaplayerLeague.create(player);
                        league.add_PlayerLeague(player);
                        line = readArq.readLine();
                    }
                    iterat++;
                    jpaLeague.edit(league);     
                    l_league.add(league);
                    league = new LeagueInfo(getId_league());
                    league.setIteration(iterat);                    
                }
                line = readArq.readLine();
            }

            readArq.close();    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(runDataReports.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        test.setLeagueInfoList(l_league);
        jpaTest.edit(test);
    }

    public int getId_league() {
        this.id_league++;
        return id_league;
    }

    public int getId_player() {
        this.id_player++;
        return id_player;
    }

    
    
    
}
