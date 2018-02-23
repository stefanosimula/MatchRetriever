package Utils;

import java.util.ArrayList;
import java.util.List;

import Data.Objects.Campionato;
import Data.Objects.Partita;
import Utils.FIPWeb.FIPWebHelper;
import Utils.FIPWeb.FIPWebParser;
import Utils.WordPress.WPUtils;

public class Utils {

    public static List<Campionato> GetAllChampionshipsFromWEB(String comitato, String regione, String provincia) {
    	
    	String htmlGetCampionatiResult = FIPWebHelper.GetCampionati(comitato, regione, provincia);
    	List<Campionato> result = FIPWebParser.ParseGetCampionati(htmlGetCampionatiResult);
    	
    	return result;
    }
    
    public static List<Partita> GetAllGamesFromWEB(Campionato championship) {
    	
    	String htmlGetPartiteResult = FIPWebHelper.GetPartite(championship);
    	List<Partita> gamesRecap = FIPWebParser.ParseGetPartiteResult(htmlGetPartiteResult);
    	
    	Partita gameDetails;
    	for(int i = 0; i < gamesRecap.size(); i++) {
    		gameDetails = GetGameDetailsFromWEB(gamesRecap.get(i));
    		
    		gamesRecap.get(i).setArbitro1(gameDetails.getArbitro1());
    		gamesRecap.get(i).setArbitro2(gameDetails.getArbitro2());
    		gamesRecap.get(i).setArbitro3(gameDetails.getArbitro3());
    		gamesRecap.get(i).setOsservatore(gameDetails.getOsservatore());
    		
    		gamesRecap.get(i).setUdC1(gameDetails.getUdC1());
    		gamesRecap.get(i).setUdC2(gameDetails.getUdC2());
    		gamesRecap.get(i).setUdC3(gameDetails.getUdC3());
    		
    		gamesRecap.get(i).setCampo(gameDetails.getCampo());
        }
    	    	
    	return gamesRecap;
    }
    
    private static Partita GetGameDetailsFromWEB(Partita game) {
    	
    	String htmlGetPartiteResult = FIPWebHelper.GetDettaglioPartita(game);
    	Partita result = FIPWebParser.ParseGetDettaglioPartitaResult(htmlGetPartiteResult);
    	
    	return result;
    }
    
    public static Partita GetGameFromDB(Partita game) {
    	Partita result = WPUtils.GetGame(game);
    	    	
    	return result;
    }
    
    public static Boolean UpdateGameOnDB(Partita game) {
    	Partita oldGame = WPUtils.GetGame(game);
    	return WPUtils.UpdateGame(oldGame.getID(), game);
    }
    
    public static void CreateGame(Partita game) {
    	
    	WPUtils.SaveGame(game);
    }
}
