package Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Logging.LogLevel;
import Logging.WPLogger;

public class DataManager {
	
	private final String configDataFilePath = "./conf/FIPWebREGIONI.json";
	
	private WPLogger logger = WPLogger.getInstance();
	private List<Regione> regioni = new ArrayList<>();
		
	public List<Regione> InitializeData() {
		LoadConf();
		
		return regioni;
	}
	
	private void LoadConf() {
		logger.Log(DataManager.class.getName(), logger.GetMethodName(), "Trying to load data configuration from file: ["+configDataFilePath+"]", LogLevel.INFO);
		
		try {
			
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			System.out.println("Current relative path is: " + s);
			
			FileReader reader = new FileReader(configDataFilePath);
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonObject = (JSONArray) jsonParser.parse(reader);
			
			JSONObject jsonRegione;
			JSONArray jsonProvince;
			String nome, regioneID, comitatoID, provincia;
			Regione regione;
			for(int i =0; i < jsonObject.size(); i++) {
				jsonRegione = (JSONObject) jsonObject.get(i);
				
				nome = (String) jsonRegione.get("name");
				regioneID = (String) jsonRegione.get("IDRegione");
				
				regione = new Regione(regioneID, nome);
				
				comitatoID = (String) jsonRegione.get("Comitato");
				regione.AddComitato(new Comitato(comitatoID));
				
				jsonProvince =(JSONArray) jsonRegione.get("Province");
				for(int j =0; j < jsonProvince.size(); j++) {
					provincia = (String) jsonProvince.get(j);
					regione.AddProvincia(new Provincia(provincia));
				}
				
				regioni.add(regione);
				logger.Log(DataManager.class.getName(), logger.GetMethodName(), "Added Regione: ["+nome+"]", LogLevel.INFO);
			}

        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
			logger.Log(DataManager.class.getName(), logger.GetMethodName(), "FAILED - File not found exception : FILE ["+configDataFilePath+"]", LogLevel.ERROR);
        } catch (IOException ex) {
        	ex.printStackTrace();
			logger.Log(DataManager.class.getName(), logger.GetMethodName(), "FAILED - IO Exception: FILE ["+configDataFilePath+"]", LogLevel.ERROR);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.Log(DataManager.class.getName(), logger.GetMethodName(), "FAILED - Parse Exception : FILE ["+configDataFilePath+"]", LogLevel.ERROR);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			logger.Log(DataManager.class.getName(), logger.GetMethodName(), "FAILED - Null Pointer Exception", LogLevel.ERROR);
		}
		
		logger.Log(DataManager.class.getName(), logger.GetMethodName(), "Load data configuration complete", LogLevel.INFO);
	}
}
