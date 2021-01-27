package scriptInterface.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import scriptInterface.InterfaceSettings;

public class ChangeColorPalettController {

	@FXML
    private ToggleGroup groupPalett;
	
    @FXML
    private ToggleButton tbPalett1;
    @FXML
    private ToggleButton tbPalett2;
    @FXML
    private ToggleButton tbPalett3;
    @FXML
    private ToggleButton tbPalett4;

    @FXML
    void clickBtnApplyPalett(ActionEvent event) {
    	if(groupPalett.getSelectedToggle() != null) {
    		if(tbPalett1.isSelected()) 
    			InterfaceSettings.getInstance().setColorPalett(1);
    		if(tbPalett2.isSelected()) 
    			InterfaceSettings.getInstance().setColorPalett(2);
    		if(tbPalett3.isSelected()) 
    			InterfaceSettings.getInstance().setColorPalett(3);
    		if(tbPalett4.isSelected()) 
    			InterfaceSettings.getInstance().setColorPalett(4);
    	}
    		
    }

}
