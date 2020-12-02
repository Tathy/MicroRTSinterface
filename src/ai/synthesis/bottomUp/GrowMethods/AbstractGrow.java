/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.GrowMethods;

import ai.synthesis.bottomUp.Utils.DSLManager;

/**
 *
 * @author rubens
 */
public abstract class AbstractGrow implements iGrowCommand {

    protected DSLManager utils;

    public AbstractGrow() {
        utils = DSLManager.getInstance();
    }
    
    

}
