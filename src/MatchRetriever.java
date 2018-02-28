import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Data.DataManager;
import Data.Objects.Campionato;
import Data.Objects.Comitato;
import Data.Objects.Partita;
import Data.Objects.Provincia;
import Data.Objects.Regione;
import Logging.LogLevel;
import Logging.WPLogger;
import Utils.Utils;

public class MatchRetriever {
	
    public static void main(String[] args) throws IOException, InterruptedException {
    	WPLogger logger = WPLogger.getInstance();
    	
        logger.Log(MatchRetriever.class.getName(), "MAIN", "***** MATCH RETRIEVER START *****", LogLevel.DEBUG);
        
        //TODO read line parameter in order to decide which kind of operation do
        
        logger.Log(MatchRetriever.class.getName(), "", "Start championships search... ", LogLevel.DEBUG);

        List<Campionato> championships = null;
        
        DataManager dataManager = new DataManager();
        List<Regione> regioni = dataManager.InitializeData();
        
        Iterator<Regione> regIt = regioni.iterator();
        while(regIt.hasNext()) {
            Regione regione = (Regione) regIt.next();
            
            for(int i = 0; i < regione.getProvince().size(); i++)
            {
            	Provincia prov = regione.getProvince().get(i);
            	Comitato com = regione.getComitati().get(i);
            	
            	championships = Utils.GetAllChampionshipsFromWEB(com.getNome(), regione.getID(), prov.getNome());
            }
        }
        
        List<Partita> games = new ArrayList<Partita>();
        Campionato championship;
        if(championships != null && championships.size() > 0) {
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Championships search complete. Found ["+championships.size()+"] championship(s)", LogLevel.DEBUG);
        	
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Start games search... ", LogLevel.INFO);
        	for(int i = 0; i < championships.size(); i++)
            {
        		championship = championships.get(i);
        		games.addAll(Utils.GetAllGamesFromWEB(championship));
            }
        }
        else {
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Championships search complete. No championship found", LogLevel.DEBUG);
        }
        
        
        if(games.size() > 0) {
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Games search complete. Found ["+games.size()+"] game(s)", LogLevel.DEBUG);
        	
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Start games operations... ", LogLevel.DEBUG);
        	
        	Partita gameOnFIP;
        	Partita gameOnDB;
        	for(int i = 0; i < games.size(); i++)
            {
        		gameOnFIP = games.get(i);
        		
        		logger.Log(MatchRetriever.class.getName(), "MAIN", "Games ["+gameOnFIP.toString()+"]", LogLevel.DEBUG);
        		       		
        		gameOnDB = Utils.GetGameFromDB(gameOnFIP);
        		
        		if(gameOnDB == null) { // The game does not available on WP -> Create a new Game
        			if(Utils.CreateGame(gameOnFIP)) {
        				logger.Log(MatchRetriever.class.getName(), "MAIN", "Game created: ["+gameOnFIP+"]", LogLevel.INFO);	
        			} else {
        				logger.Log(MatchRetriever.class.getName(), "MAIN", "Game NOT created: ["+gameOnFIP+"]", LogLevel.ERROR);	
        			}
        		}
        		else { // The game is already on the WP Site -> Update game 
        			Utils.UpdateGameOnDB(gameOnFIP);
        			
        			logger.Log(MatchRetriever.class.getName(), "MAIN", "Game updated: ["+gameOnFIP+"]", LogLevel.INFO);
        		}
            }
        	
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Games operations complete.", LogLevel.DEBUG);
        }
        else {
        	logger.Log(MatchRetriever.class.getName(), "MAIN", "Games search complete. No game found", LogLevel.DEBUG);
        }
        
        
        logger.Log(MatchRetriever.class.getName(), "MAIN", "***** MATCH RETRIEVER STOP *****", LogLevel.DEBUG);
    }
}
