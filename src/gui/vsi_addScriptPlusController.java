package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Context;

public class vsi_addScriptPlusController {
	
	private VisualScriptInterfaceController principalController;
	ArrayList<String> ScriptPreview = new ArrayList<>();
	ArrayList<String> FuncList = new ArrayList<String>();

    @FXML
    private JFXListView<String> lvScriptPreview;
    @FXML
    private JFXListView<String> lvFuncList;
    @FXML
    private Text txtAlert;
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    }
    
    //Adiciona parêntese ) -- Não está em uso
    //@FXML
    void clickAddBracket(ActionEvent event) {
    	//if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    	//	String s = lvScriptPreview.getSelectionModel().getSelectedItem() + " )";
    	//	ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
    	//	attScriptPreview();
    	//}
    }
    
    //Remove parêntese ) -- Não está em uso
    //@FXML
    void clickRemoveBracket(ActionEvent event) {
    	//if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    	//	if(lvScriptPreview.getSelectionModel().getSelectedItem().endsWith(" )")) {
	    //		String s = lvScriptPreview.getSelectionModel().getSelectedItem();
	    //		s = s.substring(0, s.length()-2);
	    //		ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
	    //		attScriptPreview();
    	//	}
    	//}
    }

    //Adiciona for no começo da lista
    @FXML
    void clickAddFor(ActionEvent event) {
    	boolean hasFor = false;
    	
    	if(!ScriptPreview.isEmpty())
    		for(int i = 0; i < ScriptPreview.size(); i++) {
    			if(ScriptPreview.get(i).contains("for"))
    				hasFor = true;
    		}
    		
	    	//if(ScriptPreview.get(0) != "for(u) (") {
    		if(!hasFor) {
	    		ArrayList<String> temp = new ArrayList<>();
	    		temp.add("for(u)");
	    		temp.addAll(ScriptPreview);
	    		
	    		ScriptPreview.clear();
	    		ScriptPreview.addAll(temp);
	    		attScriptPreview();
	    	}
 
    }

    @FXML
    void clickAddScript(ActionEvent event) {
    	String s = "";
    	int open = 0;
    	int tab = 0;
    	int new_tab = 0;
    	
    	boolean inFor = false;
    	int nvFor = 0;
    	
    	//Contagem de tabs
    	// e adição de parênteses
    	for(int i = 0; i < ScriptPreview.size(); i++) {   		
    		int mult = 0;
    		char l[] = ScriptPreview.get(i).toCharArray();
    		
    		//Impressão de script em análise
    		//System.out.println( Integer.toString(i) + " " + ScriptPreview.get(i));
    		
    		while(true) {
    			if(l[mult*8] == ' ')
    				mult++;
    			else
    				break;
    		}
    		
    		new_tab = mult;
    		
    		//Teste sde nível de identação para o for (acréscimo do u no final dos comandos)
    		if( inFor && nvFor > new_tab) inFor = false;
    		if(ScriptPreview.get(i).contains("for")) { inFor = true; nvFor = new_tab + 1; }
    		
    		if(i > 0) {
    			if(ScriptPreview.get(i-1).contains("for"))
    				s += "(";
    			else if(ScriptPreview.get(i-1).contains("if"))
    				s += "(";
    		}
    		
    		if(new_tab < tab) {
    			//adiciona ")" na string s antes de adicionar o script atual
    			s = s.substring(0,s.length()-1);
    			for(int k = 0; k < tab - new_tab; k++)
    				s += ")";
    			s += " ";
    		}
    		
    		//TESTE
    		//if(inFor) System.out.println("Linha dentro do for");
    		//else System.out.println("Linha fora do for");
    		
    		//adiciona linha atual no script
    		s += ScriptPreview.get(i).trim();
    		
    		//adiciona u caso esteja dentro de um for e o comando o use
    		if(inFor && s.toCharArray()[s.length()-1] == ')') {
    			if( !ScriptPreview.get(i).contains("train") && !ScriptPreview.get(i).contains("for") && !ScriptPreview.get(i).contains("if")){ //não coloca caso o comando não aceite o ,u)
	    			s = s.substring(0,s.length()-1);
	    			s += ",u)"; 
    			}
    			
    			if(ScriptPreview.get(i).contains("if")) {
    				s = s.substring(0,s.length()-2);
    				s += ",u))";
    			}
    		}
    		//System.out.println("Último caracteres:");
    		//System.out.println(s.toCharArray()[s.length()-1]);
    		
    		if( i != ScriptPreview.size() - 1)
    			s += " ";
    		else {
    			char ll[] = ScriptPreview.get(i).toCharArray();
    			if(ll[0] == ' ')
    				for(int k = 0; k < new_tab; k++)
    					s += ")";
    		}
    		
    		
    		tab = new_tab;
    		mult = 0;
    	}
    	
    	//Impressão da string completa s
    	System.out.println("Script completo:");
    	System.out.println(s);
    	System.out.println();

    	//Contagem de parênteses
    	char sc[] = s.toCharArray();
    	for(int i = 0; i < s.length(); i++) {
    		if(sc[i] == '(') open++;
    		else if(sc[i] == ')') open--;
    	}
    	
    	//System.out.println("Script que vai ser adicionado: ");
    	//System.out.println(s);
    	//System.out.println();
    	
    	//Mensagem de falta de parênteses caso o número esteja errado
    	// adiciona na lista principal se estiver correto
    	if(open == 0) {
    		txtAlert.setOpacity(0.0);
    		
    		if(Context.getInstance().getAbaAddScript() == 1) {
    			Context.getInstance().addScriptAI1(s);
    			principalController.attListViewAI1();
    		}else if(Context.getInstance().getAbaAddScript() == 2) {
    			Context.getInstance().addScriptAI2(s);
    			principalController.attListViewAI2();
    		}
    	}else {
    		txtAlert.setOpacity(1.0);
    	}
    }

    @FXML
    void clickIndUp(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		ListIterator<String> itr1 = ScriptPreview.listIterator();
    		ListIterator<String> itr2 = ScriptPreview.listIterator();
    		String a = lvScriptPreview.getSelectionModel().getSelectedItem();
    		String a1, a2 = "";
    		
    		if(itr1.next() != a) {
    			
    			while(itr1.hasNext()) {
    				if(itr1.next() == a) {
    					itr1.previous();
    					break;
    				}
    				itr2.next();
    			}
    			
    			a2 = itr1.next();
    			a1 = itr2.next();
    			itr1.previous();
    			itr2.previous();
    			
    			itr2.set(a2);
    			itr1.set(a1);
    		}
    		attScriptPreview();
    	}
    	
    	
    }

    @FXML
    void clickIndDown(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
	    	ListIterator<String> itr1 = ScriptPreview.listIterator();
			ListIterator<String> itr2 = ScriptPreview.listIterator();
			String a = lvScriptPreview.getSelectionModel().getSelectedItem();
			String a1 = "", a2 = "";
			
			if(ScriptPreview.get(ScriptPreview.size()-1) != a) {
			
				while(itr1.hasNext()) {
					a2 = itr2.next();
					if(itr1.next() == a){
						break;
					}
				}
					
				itr1.previous();
					
				a2 = itr2.next();
				a1 = itr1.next();
	
				itr2.set(a1);
				itr1.set(a2);
			}
			attScriptPreview();
    	}
    }

    
    //Abre janela de adição de Funções Simples
    //Janela ADD Simple Script
  	@FXML
  	void clickNewSimpleScript(ActionEvent event) throws IOException {
  		
  		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vsi_addScript.fxml"));
  		Parent root1 = (Parent) fxmlLoader.load();
  		
  		vsi_addScriptController addScriptController = fxmlLoader.getController();
  		addScriptController.initp(this);
  		
  		Stage stage = new Stage();
  		stage.setTitle("Add Scripts");
  		stage.setScene(new Scene(root1));
  		stage.setHeight(721);
  		stage.setWidth(275);
  		//stage.initModality(Modality.APPLICATION_MODAL);
  		stage.initModality(Modality.WINDOW_MODAL);
  		stage.setResizable(false);
  		
  		stage.showAndWait();	
  	}
  	
    //Janela ADD Conditional Script
  	
  	//Abre janela de adição de Funções Condicionais
  	@FXML
  	void clickNewCondScript(ActionEvent event) throws IOException {
  		
  		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vsi_addConditionalScript.fxml"));
  		Parent root1 = (Parent) fxmlLoader.load();
  		
  		vsi_addCondScriptController addCondScriptController = fxmlLoader.getController();
  		addCondScriptController.initp(this);
  		
  		Stage stage = new Stage();
  		stage.setTitle("Add Scripts");
  		stage.setScene(new Scene(root1));
  		stage.setHeight(721);
  		stage.setWidth(275);
  		//stage.initModality(Modality.APPLICATION_MODAL);
  		stage.initModality(Modality.WINDOW_MODAL);
  		stage.setResizable(false);
  		
  		stage.showAndWait();	
  	}
  	
  	//Adições às listas
  	
  	//Adições às Listas
  	public void addListViewFuncList(String s) {
  		if(!FuncList.contains(s) || !ScriptPreview.contains(s)) {
  			FuncList.add(s);
  			attFuncList();
  		}
  	}
  	
  	
  	public void addListViewScriptPreview(String s) {
  		if(!ScriptPreview.contains(s) || !FuncList.contains(s)) {
  			ScriptPreview.add(s);
  			attScriptPreview();
  		}
  	}
  	
  	//Remoções das listas
  	
  	//Remoções das Listas
  	public void delListViewScriptPreview(String s) {
  		if(ScriptPreview.contains(s))
  			ScriptPreview.remove(s);
  		attScriptPreview();
  	}
  	
  	
  	public void delListViewFuncList(String s) {
  		if(FuncList.contains(s))
  			FuncList.remove(s);
  		attFuncList();
  	}
  	
  	//Atualizações das listas
  	
  	//Atualizações dos ListView
  	private void attFuncList() {
  		lvFuncList.getItems().clear();
  		lvFuncList.getItems().addAll(FuncList);
  	}
  	
  	
  	private void attScriptPreview() {
  		lvScriptPreview.getItems().clear();
  		lvScriptPreview.getItems().addAll(ScriptPreview);
  	}
  	
  	
  	
  	//Move script Functions List --> Script Preview
  	
  	//Move script Funcions List --> Script Preview
  	@FXML
    void clickMoveScriptLeft(ActionEvent event) {
  		if(lvFuncList.getSelectionModel().getSelectedItem() != null) {
  			String s = lvFuncList.getSelectionModel().getSelectedItem();
  			addListViewScriptPreview(s);
  			delListViewFuncList(s);
  			attScriptPreview();
  			attFuncList();
  		}
    }

    //Move script Script Preview --> Functions List
    
    //Move script Script Preview -->  Funcions List
  	@FXML
    void clickMoveScriptRight(ActionEvent event) {
  		if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
  			String s = lvScriptPreview.getSelectionModel().getSelectedItem();
  			addListViewFuncList(s);
  			delListViewScriptPreview(s);
  		}
    	
    }
  	
    //Remove script selecionado no Script Preview
    
  	//Remove script selecionado o Script Preview
  	@FXML
    void clickDeleteScript(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		String s = lvScriptPreview.getSelectionModel().getSelectedItem();
    		delListViewScriptPreview(s);
    	}
    }

  	//Adiciona espaço na identação
  	@FXML
    void clickIndRight(ActionEvent event) {
  		if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		String s = "        " + lvScriptPreview.getSelectionModel().getSelectedItem();
    		ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
    		attScriptPreview();
    	}
    }
  	

  	//Remove espaço na identação
    @FXML
    void clickIndLeft(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		if(lvScriptPreview.getSelectionModel().getSelectedItem().startsWith("        ")) {
	    		String s = lvScriptPreview.getSelectionModel().getSelectedItem();
	    		s = s.substring(8);
	    		ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
	    		attScriptPreview();
    		}
    	}
    }
}
