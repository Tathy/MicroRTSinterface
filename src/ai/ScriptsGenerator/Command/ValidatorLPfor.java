package ai.ScriptsGenerator.Command;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import rts.units.UnitTypeTable;

public class ValidatorLPfor {
    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        ICompiler compiler = new MainGPCompiler(); 
        String path = "C:\\Users\\tassi\\Documents\\Teste cluster microRTS-GA\\ScriptsTable1.txt";
        File file = new File(path);
        String linha = "";
        
        try {
        	
            FileReader arq = new FileReader(file);
            java.io.BufferedReader learArq = new BufferedReader(arq);
            linha = learArq.readLine();
            int counterOpen=0;
            int counterClose=0;
            int counterU = 0;
            int closedCommands = 0;
            int nCommands = 0;
            boolean insideFor = false;
            boolean hasU = false;
            
            while (linha != null) {
            	counterOpen = 0;
            	counterClose = 0;
            	counterU = 0;
            	closedCommands = 0;
            	hasU = false;
            	nCommands = 0;
            	
            	if(!linha.contains("for")) {
            		if(linha.contains(",u"))
            			System.out.println("u fora de for: " + linha);
            		
            		for(int i=0;i<linha.length();i++){
            			// Identifica parenteses
                    	if(linha.charAt(i)=='(') counterOpen++;
                    	if(linha.charAt(i)==')') counterClose++;
                    }

            	} else {
            		for(int i = 0; i < linha.length(); i++){
            			// Identifica parenteses
            			if(linha.charAt(i)=='(') counterOpen++;
                    	if(linha.charAt(i)==')') counterClose++;
                    	
                    	// Indentifica for na linha
                    	if(i >= 7) {
	                    	if(linha.charAt(i-7) == 'f' && linha.charAt(i-6) == 'o' && linha.charAt(i-5) == 'r' && linha.charAt(i-4) == '(' && linha.charAt(i-3) == 'u' && linha.charAt(i-2) == ')' && linha.charAt(i-1) == ' ' && linha.charAt(i) == '(' ) {
	                    		insideFor = true;
	                    		//System.out.println("Encontrou for!");
	                    	}
                    	}
                    	
                    	// Identifica funções (atualizar de acordo com o necessário)
                    	if(insideFor == true) {
                    		//Have
                    		if(linha.charAt(i-3) == 'H' && linha.charAt(i-2) == 'a' && linha.charAt(i-1) == 'v' && linha.charAt(i) == 'e') {
                    			nCommands++;
	                    	}
                    		//attack
                    		if(linha.charAt(i-7) == 'a' && linha.charAt(i-6) == 't' && linha.charAt(i-5) == 't' && linha.charAt(i-4) == 'a' && linha.charAt(i-3) == 'c' && linha.charAt(i-2) == 'k') {
                    			nCommands++;
	                    	}
                    		//harvest
                    		if(linha.charAt(i-7) == 'h' && linha.charAt(i-6) == 'a' && linha.charAt(i-5) == 'r' && linha.charAt(i-4) == 'v' && linha.charAt(i-3) == 'e' && linha.charAt(i-2) == 's' && linha.charAt(i-1) == 't') {
                    			nCommands++;
	                    	}
                    		//move
                    		if(linha.charAt(i-3) == 'm' && linha.charAt(i-2) == 'o' && linha.charAt(i-1) == 'v' && linha.charAt(i) == 'e') {
                    			nCommands++;
	                    	}
                    		//train
                    		if(linha.charAt(i-7) == 't' && linha.charAt(i-6) == 'r' && linha.charAt(i-5) == 'a' && linha.charAt(i-4) == 'i' && linha.charAt(i-3) == 'n') {
                    			nCommands++;
	                    	}
                    	}
                    	
                    	// Identifica u próximo
                    	if(i >= 1) {
	                    	if(linha.charAt(i-1) == ',' && linha.charAt(i) == 'u') {
	                    		hasU = true;
	                    		counterU++;
	                    		
	                    		if(insideFor == false) 
	                    			System.out.println("u fora de for: " + linha);
	                    	}
                    	}
                    	
                    	//if(insideFor == true && linha.charAt(i) == ')' && counterU > 0) {
                    	//	counterU--;
                    		//System.out.println("identificou fim de comando");
                    	//}
                    	
                    	// Identifica saída do for
                    	if(insideFor == true && (counterOpen-counterClose == 0)) {
                    		// Verifica se todos os comandos possuem u dentro do for
                    		if(counterU != nCommands || hasU == false) {
                    			System.out.println("sem u dentro de for: " + linha);
                    			//System.out.println("counterU = " + counterU + "; nCommands = " + nCommands + ";");
                    			//System.out.println("hasU = " + hasU);
                    		}
                    		
                    		//System.out.println("Saiu do for");
                    		
                    		//System.out.println("counterU = " + counterU + "; nCommands = " + nCommands + ";");
                    		
                    		insideFor = false;
                    		hasU = false;
                    		counterU = 0;
                    		closedCommands = 0;
                    		nCommands = 0;
                    	}
                    	
            		}
            	}
     
                if(counterOpen-counterClose!=0){
                	System.out.println("Parenteses errados: " + linha);
                }                
                
                //System.out.println("-------------------");
                linha = learArq.readLine();
            }
            
            arq.close();
        } catch (Exception e) {
            System.err.printf("Erro na leitura da linha de configuração");
            System.out.println(e.toString());
        }
        
    }
}