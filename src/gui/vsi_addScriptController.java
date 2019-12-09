package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Context;

public class vsi_addScriptController {
	
	private boolean fecharJanela = false;
	private VisualScriptInterfaceController principalController;
	private vsi_addScriptPlusController addPlusController;
	
	// Build
	
    @FXML
    private ToggleButton tbBase;
    @FXML
    private ToggleButton tbBarrack;
    @FXML
    private ToggleButton tbBuildRight;
    @FXML
    private ToggleButton tbBuildLeft;
    @FXML
    private ToggleButton tbBuildUp;
    @FXML
    private ToggleButton tbBuildDown;
    @FXML
    private TextField edtBuildQnt;
    @FXML
    private ToggleGroup groupBuildTypes;
    @FXML
    private ToggleGroup groupBuildDir;
    @FXML
    private Button closeAddBuild;
    
    // Train
    
    @FXML
    private ToggleButton tbTrainWorker;
    @FXML
    private ToggleButton tbTrainLight;
    @FXML
    private ToggleButton tbTrainHeavy;
    @FXML
    private ToggleButton tbTrainRanged;
    @FXML
    private TextField edtTrainQnt;
    @FXML
    private ToggleButton tbTrainRight;
    @FXML
    private ToggleButton tbTrainLeft;
    @FXML
    private ToggleButton tbTrainUp;
    @FXML
    private ToggleButton tbTrainDown;
    @FXML
    private ToggleButton tbTrainEnemyDir;
    @FXML
    private ToggleGroup groupTrainTypes;
    @FXML
    private ToggleGroup groupTrainDir;
    @FXML
    private Button closeAddTrain;

    // Harvest
    
    @FXML
    private TextField edtHarvestQnt;
    @FXML
    private Button closeAddHarvest;
    
    // Attack
 
    @FXML
    private ToggleButton tbAttackWorker;
    @FXML
    private ToggleButton tbAttackLight;
    @FXML
    private ToggleButton tbAttackHeavy;
    @FXML
    private ToggleButton tbAttackRanged;
    @FXML
    private ToggleButton tbAttackAll;
    @FXML
    private ToggleButton tbAttackStrongest;
    @FXML
    private ToggleButton tbAttackWeakest;
    @FXML
    private ToggleButton tbAttackClosest;
    @FXML
    private ToggleButton tbAttackFarthest;
    @FXML
    private ToggleButton tbAttackLessHealthy;
    @FXML
    private ToggleButton tbAttackMostHealthy;
    @FXML
    private ToggleButton tbAttackRandom;
    @FXML
    private ToggleGroup groupAttackTypes;
    @FXML
    private ToggleGroup groupAttackEnemy;
    @FXML
    private Button closeAddAttack;
    
    //Move to Unit
    
    @FXML
    private ToggleButton tbMoveToUnitWorker;
    @FXML
    private ToggleButton tbMoveToUnitLight;
    @FXML
    private ToggleButton tbMoveToUnitHeavy;
    @FXML
    private ToggleButton tbMoveToUnitRanged;
    @FXML
    private ToggleButton tbMoveToUnitAll;
    @FXML
    private ToggleButton tbMoveToUnitAlly;
    @FXML
    private ToggleButton tbMoveToUnitEnemy;
    @FXML
    private ToggleButton tbMoveToUnitStrongest;
    @FXML
    private ToggleButton tbMoveToUnitWeakest;
    @FXML
    private ToggleButton tbMoveToUnitClosest;
    @FXML
    private ToggleButton tbMoveToUnitFarthest;
    @FXML
    private ToggleButton tbMoveToUnitLessHealthy;
    @FXML
    private ToggleButton tbMoveToUnitMostHealthy;
    @FXML
    private ToggleButton tbMoveToUnitRandom;
    @FXML
    private ToggleGroup groupMoveToUnitTypes;
    @FXML
    private ToggleGroup groupMoveToUnitTargets;
    @FXML
    private ToggleGroup groupMoveToUnitBehaviour;
    
    //Move to Coordinates
    
    @FXML
    private ToggleButton tbMoveToCoordWorker;
    @FXML
    private ToggleButton tbMoveToCoordLight;
    @FXML
    private ToggleButton tbMoveToCoordHeavy;
    @FXML
    private ToggleButton tbMoveToCoordRanged;
    @FXML
    private ToggleButton tbMoveToCoordAll;
    @FXML
    private TextField edtCoordX;
    @FXML
    private TextField edtCoordY;
    @FXML
    private ToggleGroup groupMoveToCoordTypes;
    
    // Move Away
    
    @FXML
    private ToggleButton tbMoveAwayWorker;
    @FXML
    private ToggleButton tbMoveAwayLight;
    @FXML
    private ToggleButton tbMoveAwayHeavy;
    @FXML
    private ToggleButton tbMoveAwayRanged;
    @FXML
    private ToggleButton tbMoveAwayAll;
    @FXML
    private ToggleGroup groupMoveAwayTypes;
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    	System.out.println("Chamada pela principal");
    }
    
    public void initp(vsi_addScriptPlusController m) {
    	addPlusController = m;
    	System.out.println("Chamada pela ADD+");
    }


    // Attack
    @FXML
    void clickBtnAttackAdd(ActionEvent event) throws IOException {

    	String s = "";
    	if(Context.getInstance().getAbaAddScript() == 1) {
    		if(groupAttackTypes.getSelectedToggle() != null && groupAttackEnemy.getSelectedToggle() != null) {
    			if(tbAttackWorker.isSelected())
    				s += "attack(Worker,";
    			else if(tbAttackLight.isSelected())
    				s += "attack(Light,";
    			else if(tbAttackHeavy.isSelected())
    				s += "attack(Heavy,";
    			else if(tbAttackRanged.isSelected())
    				s += "attack(Ranged,";
    			else if(tbAttackAll.isSelected())
    				s += "attack(All,";

    			if(tbAttackStrongest.isSelected())
    				s += "strongest,u)"; 
    			else if(tbAttackWeakest.isSelected())
    				s += "weakest,u)"; 
    			else if(tbAttackClosest.isSelected())
    				s += "closest,u)"; 
    			else if(tbAttackFarthest.isSelected())
    				s += "farthest,u)"; 
    			else if(tbAttackLessHealthy.isSelected())
    				s += "lessHealthy,u)"; 
    			else if(tbAttackMostHealthy.isSelected())
    				s += "mostHealthy,u)"; 
    			else if(tbAttackRandom.isSelected())
    				s += "random,u)"; 
    		
    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
    		}else {
    			System.out.println("Faltam parâmetros");
    		}
    	}else if(Context.getInstance().getAbaAddScript() == 2) {
    		if(groupAttackTypes.getSelectedToggle() != null && groupAttackEnemy.getSelectedToggle() != null) {
    			if(tbAttackWorker.isSelected())
    				s += "attack(Worker,";
    			else if(tbAttackLight.isSelected())
    				s += "attack(Light,";
    			else if(tbAttackHeavy.isSelected())
    				s += "attack(Heavy,";
    			else if(tbAttackRanged.isSelected())
    				s += "attack(Ranged,";
    			else if(tbAttackAll.isSelected())
    				s += "attack(All,";
    			
    			if(tbAttackStrongest.isSelected())
    				s += "strongest,u)"; 
    			else if(tbAttackWeakest.isSelected())
    				s += "weakest,u)"; 
    			else if(tbAttackClosest.isSelected())
    				s += "closest,u)"; 
    			else if(tbAttackFarthest.isSelected())
    				s += "farthest,u)"; 
    			else if(tbAttackLessHealthy.isSelected())
    				s += "lessHealthy,u)"; 
    			else if(tbAttackMostHealthy.isSelected())
    				s += "mostHealthy,u)"; 
    			else if(tbAttackRandom.isSelected())
    				s += "random,u)"; 
    		
    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
    		}else {
    			System.out.println("Faltam parâmetros");
    		}
    	}

    	if(fecharJanela) {
    		Stage stage = (Stage) closeAddAttack.getScene().getWindow();
        	stage.hide();
    	}
    	
    }

    //Build
    @FXML
    void clickBtnBuildAdd(ActionEvent event) {
    	if(Context.getInstance().getAbaAddScript() == 1){
    		Integer q = Integer.parseInt(edtBuildQnt.getText());
			String s = "";
			
    		if(groupBuildTypes.getSelectedToggle() != null && groupBuildDir.getSelectedToggle() != null && edtBuildQnt.getText() != null && Integer.parseInt(edtBuildQnt.getText()) != 0) {
    			if(tbBase.isSelected())
    				s = "build(Base,";// + Integer.toString(q) + ")";
    			if(tbBarrack.isSelected())
    				s = "build(Barrack,";// + Integer.toString(q) + ")";
    			
    			s += Integer.toString(q) + ",";
    			
    			if(tbBuildRight.isSelected())
    				s += "Right,u)";
    			else if(tbBuildLeft.isSelected())
    				s += "Left,u)";
    			else if(tbBuildUp.isSelected())
    				s += "Up,u)";
    			if(tbBuildDown.isSelected())
    				s += "Down,u)";
    			
    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
    		} else {
    			System.out.println("Faltam parâmetros");
    		}
    		
    	}else if(Context.getInstance().getAbaAddScript() == 2) {
    		Integer q = Integer.parseInt(edtBuildQnt.getText());
			String s = "";
			
    		if(groupBuildTypes.getSelectedToggle() != null && edtBuildQnt.getText() != null && Integer.parseInt(edtBuildQnt.getText()) != 0) {
    			if(tbBase.isSelected())
    				s = "build(Base," + Integer.toString(q) + ")";
    			if(tbBarrack.isSelected())
    				s = "build(Barrack," + Integer.toString(q) + ")";

    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
    		} else {
    			System.out.println("Faltam parâmetros");
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddBuild.getScene().getWindow();
	    	stage.hide();
    	}
    	
    }

    //Harvest
    @FXML
    void clickBtnHarvestAdd(ActionEvent event) {
    	if(Context.getInstance().getAbaAddScript() == 1) {
	    	if(edtHarvestQnt.getText() != null && Integer.parseInt(edtHarvestQnt.getText()) != 0) {
	    		Integer q = Integer.parseInt(edtHarvestQnt.getText());
	    		String s = "harvest(" + Integer.toString(q) + ")";
	    		
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
	    	}
    	} else if(Context.getInstance().getAbaAddScript() == 2) {
	    	if(edtHarvestQnt.getText() != null && Integer.parseInt(edtHarvestQnt.getText()) != 0) {
	    		Integer q = Integer.parseInt(edtHarvestQnt.getText());
	    		String s = "harvest(" + Integer.toString(q) + ")";
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
	    	}
	    	
	    	
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    
    //Move Away
    @FXML
    void clickBtnAddMoveAway(ActionEvent event) {
    	System.out.println("teste Move Away");
    	if(Context.getInstance().getAbaAddScript() == 1) {
	    	if(groupMoveAwayTypes.getSelectedToggle() != null) {
	    		String s = "moveaway(";
	    		
	    		//Type
	    		if(tbMoveAwayWorker.isSelected())
    				s += "Worker,u)";
    			else if(tbMoveAwayLight.isSelected())
    				s += "Light,u)";
    			else if(tbMoveAwayHeavy.isSelected())
    				s += "Heavy,u)";
    			else if(tbMoveAwayRanged.isSelected())
    				s += "Ranged,u)";
    			else if(tbMoveAwayAll.isSelected())
    				s += "All,u)";
	    		
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
	    	}
    	} else if(Context.getInstance().getAbaAddScript() == 2) {
	    	if(groupMoveAwayTypes.getSelectedToggle() != null) {
	    		String s = "moveaway(";
	    		
	    		//Type
	    		if(tbMoveAwayWorker.isSelected())
    				s += "Worker,u)";
    			else if(tbMoveAwayLight.isSelected())
    				s += "Light,u)";
    			else if(tbMoveAwayHeavy.isSelected())
    				s += "Heavy,u)";
    			else if(tbMoveAwayRanged.isSelected())
    				s += "Ranged,u)";
    			else if(tbMoveAwayAll.isSelected())
    				s += "All,u)";
	    		
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
	    	}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    //Move to Coordinates
    @FXML
    void clickBtnAddMoveToCoord(ActionEvent event) {
    	if(Context.getInstance().getAbaAddScript() == 1) {
	    	if(groupMoveToCoordTypes.getSelectedToggle() != null) {
	    		Integer x = Integer.parseInt(edtCoordX.getText());
	    		Integer y = Integer.parseInt(edtCoordY.getText());
	    		String s = "moveToCoord(";
	    		
	    		//Type
	    		if(tbMoveToCoordWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToCoordLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToCoordHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToCoordRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToCoordAll.isSelected())
    				s += "All,";
	    		
	    		s += x.toString() + "," + y.toString() + ",u)";
	    		
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
	    	}
    	} else if(Context.getInstance().getAbaAddScript() == 2) {
	    	if(groupMoveToCoordTypes.getSelectedToggle() != null) {
	    		Integer x = Integer.parseInt(edtCoordX.getText());
	    		Integer y = Integer.parseInt(edtCoordY.getText());
	    		String s = "moveToCoord(";
	    		
	    		//Type
	    		if(tbMoveToCoordWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToCoordLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToCoordHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToCoordRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToCoordAll.isSelected())
    				s += "All,";
	    		
	    		s += x.toString() + "," + y.toString() + ",u)";
	    		
	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
	    	}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    //Move to Unit
    @FXML
    void clickBtnAddMoveToUnit(ActionEvent event) {
    	if(Context.getInstance().getAbaAddScript() == 1) {
	    	if(groupMoveToUnitTypes.getSelectedToggle() != null && groupMoveToUnitTargets.getSelectedToggle() != null && groupMoveToUnitBehaviour.getSelectedToggle() != null) {
	    		//moveToUnit(Worker,Ally,closest,u)
	    		String s = "moveToUnit(";
	    		
	    		//Type
	    		if(tbMoveToUnitWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToUnitLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToUnitHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToUnitRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToUnitAll.isSelected())
    				s += "All,";
	    		
	    		//Target
	    		if(tbMoveToUnitAlly.isSelected())
    				s += "Ally,";
    			else if(tbMoveToUnitEnemy.isSelected())
    				s += "Enemy,";
	    		
	    		//Behaviour
	    		if(tbMoveToUnitStrongest.isSelected())
    				s += "strongest,u)"; 
    			else if(tbMoveToUnitWeakest.isSelected())
    				s += "weakest,u)"; 
    			else if(tbMoveToUnitClosest.isSelected())
    				s += "closest,u)"; 
    			else if(tbMoveToUnitFarthest.isSelected())
    				s += "farthest,u)"; 
    			else if(tbMoveToUnitLessHealthy.isSelected())
    				s += "lessHealthy,u)"; 
    			else if(tbMoveToUnitMostHealthy.isSelected())
    				s += "mostHealthy,u)"; 
    			else if(tbMoveToUnitRandom.isSelected())
    				s += "random,u)"; 
	    		

	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
	    	}
    	} else if(Context.getInstance().getAbaAddScript() == 2) {
	    	if(groupMoveToUnitTypes.getSelectedToggle() != null && groupMoveToUnitTargets.getSelectedToggle() != null && groupMoveToUnitBehaviour.getSelectedToggle() != null) {
	    		String s = "moveToUnit(";
	    		
	    		//Type
	    		if(tbMoveToUnitWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToUnitLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToUnitHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToUnitRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToUnitAll.isSelected())
    				s += "All,";
	    		
	    		//Target
	    		if(tbMoveToUnitAlly.isSelected())
    				s += "Ally,";
    			else if(tbMoveToUnitEnemy.isSelected())
    				s += "Enemy,";
	    		
	    		//Behaviour
	    		if(tbMoveToUnitStrongest.isSelected())
    				s += "strongest,u)"; 
    			else if(tbMoveToUnitWeakest.isSelected())
    				s += "weakest,u)"; 
    			else if(tbMoveToUnitClosest.isSelected())
    				s += "closest,u)"; 
    			else if(tbMoveToUnitFarthest.isSelected())
    				s += "farthest,u)"; 
    			else if(tbMoveToUnitLessHealthy.isSelected())
    				s += "lessHealthy,u)"; 
    			else if(tbMoveToUnitMostHealthy.isSelected())
    				s += "mostHealthy,u)"; 
    			else if(tbMoveToUnitRandom.isSelected())
    				s += "random,u)"; 
	    		

	    		//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
	    	}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }

    //Train
    @FXML
    void clickBtnTrainAdd(ActionEvent event) {
    	if(Context.getInstance().getAbaAddScript() == 1) {
    		Integer q = Integer.parseInt(edtTrainQnt.getText());
			String s = "";
			
    		if(groupTrainTypes.getSelectedToggle() != null && groupTrainDir.getSelectedToggle() != null && edtTrainQnt.getText() != null && Integer.parseInt(edtTrainQnt.getText()) != 0) {
    			if(tbTrainWorker.isSelected())
    				s = "train(Worker," + Integer.toString(q) + ",";
    			else if(tbTrainLight.isSelected())
    				s = "train(Light," + Integer.toString(q) + ",";
    			else if(tbTrainHeavy.isSelected())
    				s = "train(Heavy," + Integer.toString(q) + ",";
    			else if(tbTrainRanged.isSelected())
    				s = "train(Ranged," + Integer.toString(q) + ",";	
    			
    			if(tbTrainRight.isSelected())
    				s += "Right)";
    			else if(tbTrainLeft.isSelected())
    				s += "Left)";
    			else if(tbTrainUp.isSelected())
    				s += "Up)";
    			else if(tbTrainDown.isSelected())
    				s += "Down)";
    			else if(tbTrainEnemyDir.isSelected())
    				s += "EnemyDir)";
    			
    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			
    		}else {
    			System.out.println("Faltam parâmetros");
    		}
    		
    	}else if(Context.getInstance().getAbaAddScript() == 2) {
    		Integer q = Integer.parseInt(edtTrainQnt.getText());
			String s = "";
			
    		if(groupTrainTypes.getSelectedToggle() != null && groupTrainDir.getSelectedToggle() != null && edtTrainQnt.getText() != null && Integer.parseInt(edtTrainQnt.getText()) != 0) {
    			if(tbTrainWorker.isSelected())
    				s = "train(Worker," + Integer.toString(q) + ",";
    			else if(tbTrainLight.isSelected())
    				s = "train(Light," + Integer.toString(q) + ",";
    			else if(tbTrainHeavy.isSelected())
    				s = "train(Heavy," + Integer.toString(q) + ",";
    			else if(tbTrainRanged.isSelected())
    				s = "train(Ranged," + Integer.toString(q) + ",";	
    			
    			if(tbTrainRight.isSelected())
    				s += "Right)";
    			else if(tbTrainLeft.isSelected())
    				s += "Left)";
    			else if(tbTrainUp.isSelected())
    				s += "Up)";
    			else if(tbTrainDown.isSelected())
    				s += "Down)";
    			else if(tbTrainEnemyDir.isSelected())
    				s += "EnemyDir)";
    			
    			//Atualização das listas
    			if(principalController != null) {
    				Context.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    		}else {
    			System.out.println("Faltam parâmetros");
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddBuild.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
}
