/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.settings;

import ai.synthesis.DslLeague.Runner.*;

/**
 *
 * @author rubens
 */
public class SettingsBottomUp {

    private static boolean mode_debug = false;
    
    private static int LIMIT_OF_PLIST_BATTLE = 4;
    //define how many iterations the search will perform.
    private static int NUMBER_OF_BU_ITERATIONS = 4;
    

    public static void setMode_debug(boolean mode_debug) {
        SettingsBottomUp.mode_debug = mode_debug;
    }

    public static boolean isMode_debug() {
        return mode_debug;
    }

    public static String get_map() {
        SettingsAlphaDSL.setMode_debug(mode_debug);
        return SettingsAlphaDSL.get_map();        
    }
    
    public static int get_limit_of_plist_battle_correct(){
        return LIMIT_OF_PLIST_BATTLE;
    }

    public static int get_number_of_bu_iterations(){
        return NUMBER_OF_BU_ITERATIONS;
    }
}
