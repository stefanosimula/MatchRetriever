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
	
    public static void main(String[] args) throws IOException {
    	WPLogger logger = WPLogger.getInstance();
    	
        logger.Log(MatchRetriever.class.getName(), "", "***** MATCH RETRIEVER START *****", LogLevel.DEBUG);
        
        
        //@TODO read line parameter in order to decide which kind of operation do
        
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
        	logger.Log(MatchRetriever.class.getName(), "", "Championships search complete. Found ["+championships.size()+"] championship(s)", LogLevel.DEBUG);
        	
        	logger.Log(MatchRetriever.class.getName(), "", "Start games search... ", LogLevel.INFO);
        	for(int i = 0; i < championships.size(); i++)
            {
        		championship = championships.get(i);
        		games.addAll(Utils.GetAllGamesFromWEB(championship));
        		break;
            }
        }
        else {
        	logger.Log(MatchRetriever.class.getName(), "", "Championships search complete. No championship found", LogLevel.DEBUG);
        }
        
        
        if(games.size() > 0) {
        	logger.Log(MatchRetriever.class.getName(), "", "Games search complete. Found ["+games.size()+"] game(s)", LogLevel.DEBUG);
        	
        	logger.Log(MatchRetriever.class.getName(), "", "Start games operations... ", LogLevel.DEBUG);
        	
        	Partita gameOnFIP;
        	Partita gameOnDB;
        	for(int i = 0; i < games.size(); i++)
            {
        		gameOnFIP = games.get(i);
        		
        		logger.Log(MatchRetriever.class.getName(), "", "Games ["+gameOnFIP.toString()+"]", LogLevel.DEBUG);
        		
        		gameOnDB = Utils.GetGameFromDB(gameOnFIP);
        		
        		if(gameOnDB == null) { // The game does not available on WP -> Create a new Game
        			Utils.CreateGame(gameOnFIP);
      			
        			logger.Log(MatchRetriever.class.getName(), "", "Game created: ["+gameOnFIP+"]", LogLevel.INFO);
        		}
        		else { // The game is already on the WP Site -> Update game 
        			//Utils.UpdateGameOnDB(comitato, regione, provincia, sesso, campionato, fase, girone, andata, turno, numeroGara, squadraA, squadraB, puntiSquadraA, puntiSquadraB, Campo, data, ora, arbitro1, arbitro2, arbitro3, osservatore, udc1, udc2, udc3);
        			
        			logger.Log(MatchRetriever.class.getName(), "", "Game updated: ["+gameOnFIP+"]", LogLevel.INFO);
        		}
            }
        	
        	logger.Log(MatchRetriever.class.getName(), "", "Games operations complete.", LogLevel.DEBUG);
        }
        else {
        	logger.Log(MatchRetriever.class.getName(), "", "Games search complete. No game found", LogLevel.DEBUG);
        }
        
        
        logger.Log(MatchRetriever.class.getName(), "", "***** MATCH RETRIEVER STOP *****", LogLevel.DEBUG);
        
        //WPUtils.SaveGame(1243, "","");

        
    	//FIPWebHelper.GetPartite(comitato, sesso, campionato, fase, girone, andata, turno, regione, provincia)
    	//String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajax-risultati-get-navbar-campionati.aspx?"+time);
    	//String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?"+time+"&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined");
        /*
        for all Regioni
            for all provincia in regione
                for all comitato
                    retrieve partite
                    parsa risultato e ottieni le partite

                    for all partite
                        getPartitaDettaglio
                            if(partita in DB)
                                Aggiorna Partita
                            else
                                Inserisci partita nel DB

         */
        
        //////////////////////////////////////////////////////////////////////////
        
        /*
        long time = new Date().getTime();
        // http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?1617576752999&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined
        String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?"+time+"&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined");
        System.out.println(content);

        String everything = "";
        BufferedReader br = new BufferedReader(new FileReader("./Utils/GetPartiteResult.html"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }

        FIPWebParser.ParseGetPartiteResult(everything);
         */
        
        
    }
}
