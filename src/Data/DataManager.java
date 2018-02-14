package Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Logging.LogLevel;
import Logging.WPLogger;

public class DataManager {
	
	private final String configDataFilePath = "\\conf\\FIPWebREGIONI.json";
	
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
			FileReader reader = new FileReader(currentRelativePath.toAbsolutePath()+configDataFilePath);
			
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonObject = (JSONArray) jsonParser.parse(reader);
			
			JSONObject jsonRegione, jsonComitato, jsonProvincia;
			JSONArray jsonComitati, jsonProvince;
			String nome, regioneID, comitato, provincia;
			Regione regione;
			for(int i =0; i < jsonObject.size(); i++) {
				jsonRegione = (JSONObject) jsonObject.get(i);
				
				nome = (String) jsonRegione.get("name");
				regioneID = (String) jsonRegione.get("IDRegione");
				
				regione = new Regione(regioneID, nome);
				
				jsonComitati =(JSONArray) jsonRegione.get("Comitato");
				for(int j =0; j < jsonComitati.size(); j++) {
					jsonComitato = (JSONObject)jsonComitati.get(j);
					comitato = (String) jsonComitato.get("IDComitato");
					
					regione.AddComitato(new Comitato(comitato));
				}
				
				jsonProvince =(JSONArray) jsonRegione.get("Province");
				for(int j =0; j < jsonProvince.size(); j++) {
					jsonProvincia = (JSONObject)jsonProvince.get(j);
					provincia = (String) jsonProvincia.get("IDProvincia");
					
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
