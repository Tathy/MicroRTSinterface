/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

class comp_key {

    PlayerDSL home;
    PlayerDSL away;

    public comp_key(PlayerDSL home, PlayerDSL away) {
        this.home = home;
        this.away = away;
    }
    
    public String to_string(){
        String ret ="";
        ret += home.getClass().getSimpleName()+" "+home.getAgent().translate()+"____";
        ret += away.getClass().getSimpleName()+" "+away.getAgent().translate();
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.home);
        hash = 97 * hash + Objects.hashCode(this.away);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final comp_key other = (comp_key) obj;
        if (!Objects.equals(this.home, other.home)) {
            return false;
        }
        if (!Objects.equals(this.away, other.away)) {
            return false;
        }
        return true;
    }

}

/**
 *
 * @author rubens
 */
public class Payoff {

    private HashSet<String> lock_players = new HashSet<>();
    private List<PlayerDSL> players = new ArrayList<>();
    private HashMap<comp_key, Double> wins = new HashMap<>();
    private HashMap<comp_key, Double> draws = new HashMap<>();
    private HashMap<comp_key, Double> losses = new HashMap<>();
    private HashMap<comp_key, Double> games = new HashMap<>();
    private double decay = 0.99;

    private double win_rate(PlayerDSL home, PlayerDSL away) {
        if (games.get(new comp_key(home, away)) == null) {
            return 0.5;
        }

        double wins_rate = wins.get(new comp_key(home, away));
        double draw_rate = draws.get(new comp_key(home, away));
        double total_games = games.get(new comp_key(home, away));
        return (wins_rate + (0.5 * draw_rate)) / total_games;
    }

    //check __getitem__ in python pseudocode to fix it.
    public List<Double> getItem(PlayerDSL home, PlayerDSL away) {
        //fix implementation
        List<Double> ret = new ArrayList<>();
        ret.add(win_rate(home, away));
        return ret;
    }

    public List<Double> getItem(PlayerDSL home, List<PlayerDSL> aways) {
        List<Double> ret = new ArrayList<>();
        for (PlayerDSL away : aways) {
            ret.add(win_rate(home, away));
        }
        return ret;
    }

    public List<Double> getItem(List<PlayerDSL> homes, PlayerDSL away) {
        List<Double> ret = new ArrayList<>();
        for (PlayerDSL home : homes) {
            ret.add(win_rate(home, away));
        }
        return ret;
    }

    public List<Double> getItem(List<PlayerDSL> homes, List<PlayerDSL> aways) {
        List<Double> ret = new ArrayList<>();
        for (PlayerDSL home : homes) {
            for (PlayerDSL away : aways) {
                ret.add(win_rate(home, away));
            }
        }
        return ret;
    }

    public synchronized void update(PlayerDSL home, PlayerDSL away, String result) {
        //System.err.println("Matrix games size "+games.size()+" "+games.toString());
        //System.err.println("Matrix wins size "+wins.size()+" "+wins.toString());
        //System.err.println("Matrix draws size "+draws.size()+" "+draws.toString());
        //System.err.println("Matrix losses size "+losses.size()+" "+losses.toString());

        comp_key key = new comp_key(home, away);
        comp_key reverse_key = new comp_key(away, home);
        update_full_decay(key);
        update_full_decay(reverse_key);

        this.games.put(key, this.games.get(key) + 1.0);
        this.games.put(reverse_key, this.games.get(reverse_key) + 1.0);

        if (result.equals("win")) {
            this.wins.put(key, this.wins.get(key) + 1.0);
            this.losses.put(reverse_key, this.losses.get(reverse_key) + 1.0);
        } else if (result.equals("draw")) {
            this.draws.put(key, this.draws.get(key) + 1.0);
            this.draws.put(reverse_key, this.draws.get(reverse_key) + 1.0);
        } else {
            this.wins.put(reverse_key, this.wins.get(reverse_key) + 1.0);
            this.losses.put(key, this.losses.get(key) + 1.0);
        }
    }

    /**
     * equal to for stats in (self._games, self._wins, self._draws,
     * self._losses): stats[home, away] *= self._decay
     *
     * @param key
     */
    private void update_full_decay(comp_key key) {
        if (this.games.containsKey(key)) {
            this.games.put(key, this.games.get(key) * this.decay);
        } else {
            this.games.put(key, 0.0);
        }

        if (this.wins.containsKey(key)) {
            this.wins.put(key, this.wins.get(key) * this.decay);
        } else {
            this.wins.put(key, 0.0);
        }

        if (this.draws.containsKey(key)) {
            this.draws.put(key, this.draws.get(key) * this.decay);
        } else {
            this.draws.put(key, 0.0);
        }

        if (this.losses.containsKey(key)) {
            this.losses.put(key, this.losses.get(key) * this.decay);
        } else {
            this.losses.put(key, 0.0);
        }
    }

    public synchronized void add_player(PlayerDSL player) {
        if (player instanceof HistoricalDSL) {
            if(this.lock_players.contains(player.agent.translate())){
                return;
            }else{
                this.lock_players.add(player.agent.translate());
            }
        }        
        this.players.add(player);
    }

    public List<PlayerDSL> getPlayers() {
        return players;
    }
    
    public void printMatrixValues(){
        System.out.println("  & Games:");
        for (comp_key key : this.games.keySet()) {
            System.out.println("    &  "+key.to_string());
            System.out.println("      &  "+this.games.get(key));
        }
        System.out.println("  & Wins:");
        for (comp_key key : this.wins.keySet()) {
            System.out.println("    &  "+key.to_string());
            System.out.println("      &  "+this.wins.get(key));
        }
        System.out.println("  & Losses:");
        for (comp_key key : this.losses.keySet()) {
            System.out.println("    &  "+key.to_string());
            System.out.println("      &  "+this.losses.get(key));
        }
        System.out.println("  & Draws:");
        for (comp_key key : this.draws.keySet()) {
            System.out.println("    &  "+key.to_string());
            System.out.println("      &  "+this.draws.get(key));
        }
    }

}
