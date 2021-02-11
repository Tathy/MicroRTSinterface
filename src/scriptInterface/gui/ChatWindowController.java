package scriptInterface.gui;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;

class Message {
    private String sender;
    private String m;
    
    public Message(String s, String t) {
    	this.sender = s;
    	this.m = t;
    }
    public void setSender(String s) {
        this.sender = s;
    }
    public void setMessage(String m) {
        this.m = m;
    }
    public String getSender() {
        return this.sender;
    }
    public String getMessage() {
        return this.m;
    }
}

public class ChatWindowController {
	
	Queue<Message> messages = new LinkedList<>();

    @FXML
    private JFXTextArea txtMessages;
    @FXML
    private JFXTextArea txtSend;

    // Inicialização do chat
	public void initialize(URL location, ResourceBundle resources) {
		//Resgatar histórico de mensagens pelo webservice
		
		attHistoric();
	}
    
    @FXML
    void clickBtnSend(ActionEvent event) {
    	String sender = "MIAU";
    	String message = txtSend.getText();

    	if(message != null && message != "") {
    		Message m = new Message(sender, message);
    		this.messages.add(m);
    	}

    	txtSend.clear();
    	attHistoric();
    }
    
    public void attHistoric() {
    	Message temp;
    	txtMessages.clear();
    	Queue<Message> mgs = new LinkedList<>();
    	
    	Iterator<Message> it = this.messages.iterator();
    	while(it.hasNext())  {
    	   mgs.add(it.next());
    	}
    	
    	Iterator<Message> it2 = mgs.iterator();
    	while(it2.hasNext()) {
    		temp = mgs.remove();
    		txtMessages.setText( txtMessages.getText() + temp.getSender() + ": " + temp.getMessage() + "\n\n");

    	}
    }

}
