package scriptInterface.gui;

import java.io.IOException;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SendQuestionnaire2Controller {

	// Respostas Mapa 1
    @FXML
    private JFXTextArea txtQ1Map1;
    @FXML
    private JFXTextArea txtQ2Map1;
    @FXML
    private JFXTextArea txtQ3Map1;
    @FXML
    private JFXTextArea txtQ4Map1;

    @FXML
    private RadioButton rb5Scale1Map1;
    @FXML
    private RadioButton rb5Scale2Map1;
    @FXML
    private RadioButton rb5Scale3Map1;
    @FXML
    private RadioButton rb5Scale4Map1;
    @FXML
    private RadioButton rb5Scale5Map1;
    @FXML
    private ToggleGroup groupQ5Map1;

    // Respostas Mapa 2
    @FXML
    private JFXTextArea txtQ1Map2;
    @FXML
    private JFXTextArea txtQ2Map2;
    @FXML
    private JFXTextArea txtQ3Map2;
    @FXML
    private JFXTextArea txtQ4Map2;

    @FXML
    private RadioButton rb5Scale1Map2;
    @FXML
    private RadioButton rb5Scale2Map2;
    @FXML
    private RadioButton rb5Scale3Map2;
    @FXML
    private RadioButton rb5Scale4Map2;
    @FXML
    private RadioButton rb5Scale5Map2;
    @FXML
    private ToggleGroup groupQ5Map2;
    
    //Respostas Mapa 3
    @FXML
    private JFXTextArea txtQ1Map3;
    @FXML
    private JFXTextArea txtQ2Map3;
    @FXML
    private JFXTextArea txtQ3Map3;
    @FXML
    private JFXTextArea txtQ4Map3;

    @FXML
    private RadioButton rb5Scale1Map3;
    @FXML
    private RadioButton rb5Scale2Map3;
    @FXML
    private RadioButton rb5Scale3Map3;
    @FXML
    private RadioButton rb5Scale4Map3;
    @FXML
    private RadioButton rb5Scale5Map3;
    @FXML
    private ToggleGroup groupQ5Map3;

    //Resposta comum
    @FXML
    private RadioButton rbGeralScale1;
    @FXML
    private RadioButton rbGeralScale2;
    @FXML
    private RadioButton rbGeralScale3;
    @FXML
    private RadioButton rbGeralScale4;
    @FXML
    private RadioButton rbGeralScale5;
    @FXML
    private ToggleGroup groupQgeral;
    
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSubmit;

    @FXML
    void clickBtnCancel(ActionEvent event) {
    	Stage stage = (Stage) btnCancel.getScene().getWindow();
    	stage.hide();
    }

    @FXML
    void clickBtnClear(ActionEvent event) {
    	txtQ1Map1.clear();
    	txtQ2Map1.clear();
    	txtQ3Map1.clear();
    	txtQ4Map1.clear();
    	groupQ5Map1.getSelectedToggle().setSelected(false);
    	txtQ1Map2.clear();
    	txtQ2Map2.clear();
    	txtQ3Map2.clear();
    	txtQ4Map2.clear();
    	groupQ5Map2.getSelectedToggle().setSelected(false);
    	txtQ1Map3.clear();
    	txtQ2Map3.clear();
    	txtQ3Map3.clear();
    	txtQ4Map3.clear();
    	groupQ5Map3.getSelectedToggle().setSelected(false);
    	groupQgeral.getSelectedToggle().setSelected(false);
    }

    @FXML
    void clickBtnSubmit(ActionEvent event) throws IOException {
    	String txtQ5Map1 = null;
    	String txtQ5Map2 = null;
    	String txtQ5Map3 = null;
    	String txtQGeral = null;
    	
    	if(groupQ5Map1.getSelectedToggle() != null) {
    		if(rb5Scale1Map1.isSelected()) txtQ5Map1 = "Absolutely Agree";
    		else if(rb5Scale2Map1.isSelected()) txtQ5Map1 = "Agree";
    		else if(rb5Scale3Map1.isSelected()) txtQ5Map1 = "Indifferent";
    		else if(rb5Scale4Map1.isSelected()) txtQ5Map1 = "Disagree";
    		else if(rb5Scale5Map1.isSelected()) txtQ5Map1 = "Absolutely Disagree";
    	}
    	
    	if(groupQ5Map2.getSelectedToggle() != null) {
    		if(rb5Scale1Map2.isSelected()) txtQ5Map2 = "Absolutely Agree";
    		else if(rb5Scale2Map2.isSelected()) txtQ5Map2 = "Agree";
    		else if(rb5Scale3Map2.isSelected()) txtQ5Map2 = "Indifferent";
    		else if(rb5Scale4Map2.isSelected()) txtQ5Map2 = "Disagree";
    		else if(rb5Scale5Map2.isSelected()) txtQ5Map2 = "Absolutely Disagree";
    	}
    	
    	if(groupQ5Map3.getSelectedToggle() != null) {
    		if(rb5Scale1Map3.isSelected()) txtQ5Map3 = "Absolutely Agree";
    		else if(rb5Scale2Map3.isSelected()) txtQ5Map3 = "Agree";
    		else if(rb5Scale3Map3.isSelected()) txtQ5Map3 = "Indifferent";
    		else if(rb5Scale4Map3.isSelected()) txtQ5Map3= "Disagree";
    		else if(rb5Scale5Map3.isSelected()) txtQ5Map3 = "Absolutely Disagree";
    	}
    	
    	if(groupQgeral.getSelectedToggle() != null) {
    		if(rbGeralScale1.isSelected()) txtQGeral = "Absolutely Agree";
    		else if(rbGeralScale2.isSelected()) txtQGeral = "Agree";
    		else if(rbGeralScale3.isSelected()) txtQGeral = "Indifferent";
    		else if(rbGeralScale4.isSelected()) txtQGeral= "Disagree";
    		else if(rbGeralScale5.isSelected()) txtQGeral = "Absolutely Disagree";
    	}
    	
    	System.out.println("----- Map 1 -----");
    	System.out.println("Resposta Q1: " + txtQ1Map1.getText());
    	System.out.println("Resposta Q2: " + txtQ2Map1.getText());
    	System.out.println("Resposta Q3: " + txtQ3Map1.getText());
    	System.out.println("Resposta Q4: " + txtQ4Map1.getText());
    	System.out.println("Resposta Q5: " + txtQ5Map1);
    	System.out.println("----- Map 2 -----");
    	System.out.println("Resposta Q1: " + txtQ1Map2.getText());
    	System.out.println("Resposta Q2: " + txtQ2Map2.getText());
    	System.out.println("Resposta Q3: " + txtQ3Map2.getText());
    	System.out.println("Resposta Q4: " + txtQ4Map2.getText());
    	System.out.println("Resposta Q5: " + txtQ5Map2);
    	System.out.println("----- Map 3 -----");
    	System.out.println("Resposta Q1: " + txtQ1Map3.getText());
    	System.out.println("Resposta Q2: " + txtQ2Map3.getText());
    	System.out.println("Resposta Q3: " + txtQ3Map3.getText());
    	System.out.println("Resposta Q4: " + txtQ4Map3.getText());
    	System.out.println("Resposta Q5: " + txtQ5Map3);
    	System.out.println("----- Geral -----");
    	System.out.println("Resposta: " + txtQGeral);
    	
    	//ConfirmQuestionnaireController
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfirmQuestionnaire.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		ConfirmQuestionnaireController confirmQuestionnaireController = fxmlLoader.getController();
		//confirmQuestionnaireController.initialize(this,3);
		
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setHeight(200);
		stage.setWidth(400);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    	
    	Stage st = (Stage) btnSubmit.getScene().getWindow();
    	st.hide();
    }

}
