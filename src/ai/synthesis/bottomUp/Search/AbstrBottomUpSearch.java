/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.bottomUp.Search;

import ai.synthesis.bottomUp.Utils.DSLManager;

/**
 *
 * @author rubens
 */
public abstract class AbstrBottomUpSearch implements iBottonUpSearch{
    protected DSLManager utils;

    public AbstrBottomUpSearch() {
        utils = DSLManager.getInstance();
    }
    
}
