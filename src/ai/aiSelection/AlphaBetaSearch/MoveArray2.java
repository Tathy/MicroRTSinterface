/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.aiSelection.AlphaBetaSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author rubens
 */
public class MoveArray2 {

    // the array which contains all the moves
    HashMap<Integer, ArrayList<Action>> _moves;

    // the number of units that have moves;
    ArrayList<Integer> _numUnits;
    boolean _hasMoreMoves;
    
    // trigger to simplify the move generation
    HashMap<Integer, Integer> _currentMovesIndex;
    

    public MoveArray2() {
        this._moves = new HashMap<>();
        this._hasMoreMoves = true;
        this._numUnits = new ArrayList<>();
        this._currentMovesIndex = new HashMap<>();
        
    }

    public void clear() {
        // only clear things if they need to be cleared
        if (_numUnits.isEmpty()) {
            return;
        }
        _moves.clear();
        _numUnits.clear();
        resetMoveIterator();
    }

    public void resetMoveIterator() {
        _hasMoreMoves = true;
        _currentMovesIndex.clear();
        for (Integer _numUnit : _numUnits) {
            _currentMovesIndex.put(_numUnit, 0);
        }
    }

    // shuffle the MOVE unit actions to prevent bias in experiments
    // this function assumes that all MOVE actions are contiguous in the moves array
    // this should be the case unless you change the move generation ordering
    public void shuffleMoveActions() {
        // for each unit
        for (int u : _numUnits) {
            random_shuffle(u);
        }
    }

    public void random_shuffle(int u) {
        //Check if the unit exist
        if (_moves.containsKey(u)) {
            ArrayList<Action> tAction = _moves.get(u);
            Collections.shuffle(tAction);
        }
        resetMoveIterator();
    }

    // returns a given move from a unit
    public Action getMove(int unit, int move) {
        return _moves.get(unit).get(move);
    }

    public Action[] getMoves(int unit) {
        return (Action[]) _moves.get(unit).toArray();
    }

    
    public void printCurrentMoveIndex() {
        for (Integer _numUnit : _numUnits) {
            System.out.print(_numUnit+":"+_currentMovesIndex.get(_numUnit)+" ");
        }
        System.out.println(" ");
    }

    //check
    public void incrementMove(int unit) {
        //thinking if it is necessary
        _currentMovesIndex.put(unit, ( (_currentMovesIndex.get(unit)+1) % _moves.get(unit).size() ));
        // if the value rolled over, we need to do the carry calculation
        if(_currentMovesIndex.get(unit) == 0){
            // the next unit index
            // if we have space left to increment, do it
            try {
                int nextUnit = _numUnits.get(_numUnits.indexOf(unit)+1);
                incrementMove(nextUnit);
            } catch (Exception e) { // otherwise we have no more moves
                // stop
                _hasMoreMoves = false;
            }
        }
    }

    public boolean hasMoreMoves() {
        return _hasMoreMoves;
    }
    
    public ArrayList<Action> getNextMoveVec(){
        ArrayList<Action> tempActions = new ArrayList<>();
        
        for (Integer m : _numUnits) {
            Action act = _moves.get(m).get(_currentMovesIndex.get(m));
            if(act != null){
                tempActions.add(act);
            }
        }
        
        if(_numUnits.size() > 0){
            incrementMove(_numUnits.get(0));
        }
        return tempActions;
    }

    //check
    public int maxUnits() {
        return 200;
    }

    // adds a Move to the unit specified
    public void add(int unit, Action move) {
        ArrayList<Action> tAction;
        //check if unit exist in map
        if (_moves.containsKey(unit)) {
            //just update the arraylist
            tAction = _moves.get(unit);
            tAction.add(move);
        } else {
            //insert a new tuple
            tAction = new ArrayList<>();
            tAction.add(move);
            _moves.put(unit, tAction);
        }
    }

    public boolean validateMoves() {
        for (int u : _numUnits) {
            for (Action move : _moves.get(u)) {
                if (move.getUnit() > 200) {
                    System.out.println("Unit Move Incorrect! Something will be wrong");
                    return false;
                }
            }
        }
        return true;
    }

    public int getUnitID(int unit) {
        return getMove(unit, 0).getUnit();
    }

    public int getPlayerID(int unit) {
        return getMove(unit, 0).getPlayer();
    }

    public void addUnit(int unit) {
        checkAndInsert(unit);
    }

    public int numUnits() {
        return _numUnits.size();
    }
    
    public int numUnitsInTuple(){
        return numUnits();
    }
    
    public int numMoves(int unit){
        return _moves.get(unit).size();
    }
    
    public void replaceMovimentUnit(int unit, Action move){
        ArrayList<Action> newAction = new ArrayList<>();
        newAction.add(move);
        _moves.put(unit, newAction); 
        _currentMovesIndex.put(unit, 0);
        checkAndInsert(unit);
    }
    
    public void print(){
        for (Integer _numUnit : _numUnits) {
            for(Action ac : _moves.get(_numUnit)){
                try {
                    System.out.println(ac.debugString());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    /**
     * It will check if the unit exist in _numUnits and will insert if not.
     * @param unit 
     */
    private void checkAndInsert(int unit) {
        if(!_numUnits.contains(unit)){
            _numUnits.add(unit);
        }
    }

}
