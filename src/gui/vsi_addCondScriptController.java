package gui;

import com.jfoenix.controls.JFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import model.Context;

public class vsi_addCondScriptController {
	
	//private boolean fecharJanela = false;
	private VisualScriptInterfaceController principalController;
	private vsi_addScriptPlusController addPlusController;

    //Ally Conditional
    
    @FXML
    private JFXRadioButton rbHaveQtdUnitsbyType;
    @FXML
    private JFXRadioButton rbHaveQtdUnitsHarversting;
    @FXML
    private JFXRadioButton rbHaveUnitsStrongest;
    @FXML
    private JFXRadioButton rbHaveUnitsinEnemyRange;
    @FXML
    private JFXRadioButton rbHaveUnitsToDistantToEnemy;
    @FXML
    private ToggleGroup groupConditionalAllies;

    //Ally Type

    @FXML
    private ToggleButton tbCondAllyWorker;
    @FXML
    private ToggleButton tbCondAllyLight;
    @FXML
    private ToggleButton tbCondAllyHeavy;
    @FXML
    private ToggleButton tbCondAllyRanged;
    @FXML
    private ToggleButton tbCondAllyAll;
    @FXML
    private ToggleGroup groupCondAllyTypes;

    //Ally Quantity
    
    @FXML
    private TextField edtCondAllyQnt;
    @FXML
    private Text txtQntDistAlly;
    
    //Enemy Conditional
    
    @FXML
    private JFXRadioButton rbHaveQtdEnemiesbyType;
    @FXML
    private JFXRadioButton HaveQtdUnitsAttacking;
    @FXML
    private JFXRadioButton rbHaveEnemiesStrongest;
    @FXML
    private JFXRadioButton rbHaveEnemiesinUnitsRange;
    @FXML
    private ToggleGroup groupConditionalEnemies;
    
    //Enemy Type

    @FXML
    private ToggleButton tbCondEnemyWorker;
    @FXML
    private ToggleButton tbCondEnemyLight;
    @FXML
    private ToggleButton tbCondEnemyHeavy;
    @FXML
    private ToggleButton tbCondEnemyRanged;
    @FXML
    private ToggleButton tbCondEnemyAll;
    @FXML
    private ToggleGroup groupCondEnemyTypes;

    //Enemy Quantity
    
    @FXML
    private TextField edtCondEnemyQnt;
    @FXML
    private Text txtQntDistEnemy;
    
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    }
    
    public void initp(vsi_addScriptPlusController m) {
    	addPlusController = m;
    }

    @FXML
    void verifyParameters(ActionEvent event) {
    	
    	//define quais estruturas devem ficar ativadas ou não
    	//aba Allies
    	if(groupConditionalAllies.getSelectedToggle() != null) {

	        if(rbHaveQtdUnitsbyType.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		txtQntDistAlly.setText("Quantity");
	       		edtCondAllyQnt.setDisable(false);
	       		
        	}else if(rbHaveQtdUnitsHarversting.isSelected()) {
	        	tbCondAllyWorker.setDisable(true);
	       		tbCondAllyLight.setDisable(true);
	      		tbCondAllyHeavy.setDisable(true);
	       		tbCondAllyRanged.setDisable(true);
	       		tbCondAllyAll.setDisable(true);
	       		txtQntDistAlly.setText("Quantity");
	       		edtCondAllyQnt.setDisable(false);
	       		
        	}else if(rbHaveUnitsStrongest.isSelected()) {
	      		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		edtCondAllyQnt.setDisable(true);
	        		
	       	}else if(rbHaveUnitsinEnemyRange.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		edtCondAllyQnt.setDisable(true);
	       		
	       	}else if(rbHaveUnitsToDistantToEnemy.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		txtQntDistAlly.setText("Distance");
	       		edtCondAllyQnt.setDisable(false);
	        		
	       	}
    	}
    	
    	//aba Enemies
    	if(groupConditionalEnemies.getSelectedToggle() != null) {

	        if(rbHaveQtdEnemiesbyType.isSelected()) {
	        	tbCondEnemyWorker.setDisable(false);
	        	tbCondEnemyLight.setDisable(false);
	        	tbCondEnemyHeavy.setDisable(false);
	        	tbCondEnemyRanged.setDisable(false);
	        	tbCondEnemyAll.setDisable(false);
	        	txtQntDistEnemy.setText("Quantity");
	       		edtCondEnemyQnt.setDisable(false);
	       		
        	}else if(HaveQtdUnitsAttacking.isSelected()) {
        		tbCondEnemyWorker.setDisable(false);
        		tbCondEnemyLight.setDisable(false);
        		tbCondEnemyHeavy.setDisable(false);
        		tbCondEnemyRanged.setDisable(false);
        		tbCondEnemyAll.setDisable(false);
        		txtQntDistEnemy.setText("Quantity");
	       		edtCondEnemyQnt.setDisable(false);
	       		
        	}else if(rbHaveEnemiesStrongest.isSelected()) {
        		tbCondEnemyWorker.setDisable(false);
        		tbCondEnemyLight.setDisable(false);
        		tbCondEnemyHeavy.setDisable(false);
        		tbCondEnemyRanged.setDisable(false);
        		tbCondEnemyAll.setDisable(false);
        		edtCondEnemyQnt.setDisable(true);
	        		
	       	}else if(rbHaveEnemiesinUnitsRange.isSelected()) {
	       		tbCondEnemyWorker.setDisable(false);
	       		tbCondEnemyLight.setDisable(false);
	       		tbCondEnemyHeavy.setDisable(false);
	       		tbCondEnemyRanged.setDisable(false);
	       		tbCondEnemyAll.setDisable(false);
	       		edtCondEnemyQnt.setDisable(true);
	        		
	       	}
    	}
    	
    }
    
    @FXML
    void clickBtnAddCondAllies(ActionEvent event) {
    	String s = "if(";
    	
    	if(Context.getInstance().getAbaAddScript() == 1) {
    		
    		//HaveQtdUnitsbyType
    		if( rbHaveQtdUnitsbyType.isSelected() ) {
    			s += "HaveQtdUnitsbyType(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveQtdUnitsHarvesting
    		else if( rbHaveQtdUnitsHarversting.isSelected() ) {
    			s += "HaveQtdUnitsHarversting(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    	    		
    	    		//Quantity
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsStrongest
    		else if( rbHaveUnitsStrongest.isSelected() ) {
    			s += "HaveUnitsStrongest(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsinEnemyRange
    		else if( rbHaveUnitsinEnemyRange.isSelected() ) {
    			s += "HaveUnitsinEnemyRange(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {

    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsToDistantToEnemy
    		else if( rbHaveUnitsToDistantToEnemy.isSelected() ) {
    			s += "HaveUnitsToDistantToEnemy(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//Atualização das listas
			if(principalController != null) {
				Context.getInstance().addScriptAI1(s);
				principalController.attListViewAI1();
			}else if(addPlusController != null) {
				addPlusController.addListViewFuncList(s);
			}
    		
    	}else if(Context.getInstance().getAbaAddScript() == 2) {
    		//HaveQtdUnitsbyType
    		if( rbHaveQtdUnitsbyType.isSelected() ) {
    			s += "HaveQtdUnitsbyType(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveQtdUnitsHarvesting
    		else if( rbHaveQtdUnitsHarversting.isSelected() ) {
    			s += "HaveQtdUnitsHarvesting(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    	    		
    	    		//Quantity
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsStrongest
    		else if( rbHaveUnitsStrongest.isSelected() ) {
    			s += "HaveUnitsStrongest(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsinEnemyRange
    		else if( rbHaveUnitsinEnemyRange.isSelected() ) {
    			s += "HaveUnitsinEnemyRange(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {

    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveUnitsToDistantToEnemy
    		else if( rbHaveUnitsToDistantToEnemy.isSelected() ) {
    			s += "HaveUnitsToDistantToEnemy(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//Atualização das listas
			if(principalController != null) {
				Context.getInstance().addScriptAI2(s);
				principalController.attListViewAI2();
			}else if(addPlusController != null) {
				addPlusController.addListViewFuncList(s);
			}
    	}
    }
    
    @FXML
    void clickBtnAddCondEnemies(ActionEvent event) {
    	String s = "if(";
    	
    	if(Context.getInstance().getAbaAddScript() == 1) {
    		
    		//HaveQtdEnemiesbyType
    		if( rbHaveQtdEnemiesbyType.isSelected() ) {
    			System.out.println("teste HaveQtdEnemiesbyType");
    			s += "HaveQtdEnemiesbyType(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveQtdEnemiesAttacking
    		else if( HaveQtdUnitsAttacking.isSelected() ) {
    			System.out.println("teste HaveQtdEnemiesAttacking");
    			s += "HaveQtdUnitsAttacking(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveEnemiesStrongest
    		else if( rbHaveEnemiesStrongest.isSelected() ) {
    			System.out.println("teste HaveEnemiesStrongest");
    			s += "HaveEnemiesStrongest(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveEnemiesinUnitsRange
    		else if( rbHaveEnemiesinUnitsRange.isSelected() ) {
    			System.out.println("teste HaveEnemiesinUnitsRange");
    			s += "HaveEnemiesinUnitsRange(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//Atualização das listas
			if(principalController != null) {
				Context.getInstance().addScriptAI1(s);
				principalController.attListViewAI1();
			}else if(addPlusController != null) {
				addPlusController.addListViewFuncList(s);
			}
    		
    	}else if(Context.getInstance().getAbaAddScript() == 2) {
    		
    		//HaveQtdEnemiesbyType
    		if( rbHaveQtdEnemiesbyType.isSelected() ) {
    			System.out.println("teste HaveQtdEnemiesbyType");
    			s += "HaveQtdEnemiesbyType(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveQtdEnemiesAttacking
    		else if( HaveQtdUnitsAttacking.isSelected() ) {
    			System.out.println("teste HaveQtdUnitsAttacking");
    			s += "HaveQtdUnitsAttacking(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ",u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveEnemiesStrongest
    		else if( rbHaveEnemiesStrongest.isSelected() ) {
    			System.out.println("teste HaveEnemiesStrongest");
    			s += "HaveEnemiesStrongest(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//HaveEnemiesinUnitsRange
    		else if( rbHaveEnemiesinUnitsRange.isSelected() ) {
    			System.out.println("teste HaveEnemiesinUnitsRange");
    			s += "HaveEnemiesinUnitsRange(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,u)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,u)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,u)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,u)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,u)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ") (";
    			}
    		}
    		
    		//Atualização das listas
			if(principalController != null) {
				Context.getInstance().addScriptAI2(s);
				principalController.attListViewAI2();
			}else if(addPlusController != null) {
				addPlusController.addListViewFuncList(s);
			}
    		
    		
    	}
    	
    }
    
    

}
