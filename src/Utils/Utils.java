package Utils;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import Data.Objects.Campionato;
import Data.Objects.Partita;
import Data.Objects.Sesso;
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
    	List<Partita> result = FIPWebParser.ParseGetPartiteResult(htmlGetPartiteResult);
    	
    	return result;
    }
    
    public static Partita GetGameDetailsFromWEB(String comitato,  String regione, String provincia, String sesso, 
    		String campionato, String fase, String girone, Boolean andata, String turno, int numeroGara) {
    	
    	String htmlGetPartiteResult = FIPWebHelper.GetDettaglioPartita(comitato, regione, provincia, sesso, campionato, fase, girone, andata, turno, numeroGara);
    	Partita result = FIPWebParser.ParseGetDettaglioPartitaResult(htmlGetPartiteResult);
    	
    	return result;
    }
    
    public static Partita GetGameFromDB(Partita game) {
    	
    	Partita result = WPUtils.GetGame(game);
    	
    	return result;
    }
    
    public static Boolean UpdateGameOnDB(String comitato,  String regione, String provincia, String sesso, 
    		String campionato, String fase, String girone, Boolean andata, String turno, int numeroGara, 
    		String squadraA, String squadraB, int puntiSquadraA, int puntiSquadraB, String Campo, String data, String ora,
    		String arbitro1, String arbitro2, String arbitro3, String osservatore,
    		String udc1, String udc2, String udc3) {
    	String htmlGetPartiteResult = FIPWebHelper.GetDettaglioPartita(comitato, regione, provincia, sesso, campionato, fase, girone, andata, turno, numeroGara);
    	Partita result = FIPWebParser.ParseGetDettaglioPartitaResult(htmlGetPartiteResult);
    	
    	return true;
    }
    
    public static void CreateGame(Partita game) {
    	
    	WPUtils.SaveGame(game);
    }
}
