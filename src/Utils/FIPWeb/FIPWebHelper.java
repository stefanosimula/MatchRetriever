package Utils.FIPWeb;

import java.util.Date;

import Data.Objects.Campionato;
import Utils.HTTP.HTTPHelper;

public class FIPWebHelper {

	private static String FipUrl = "http://www.fip.it/";
    private static String FipWebUrl = "http://www.fip.it/FipWeb/";
    private static String GetCampionatiUrl = "ajaxRisultatiGetMenuCampionati.aspx";
    private static String GetRisultatiUrl = "risultati.aspx";
    private static String GetPartiteUrl = "ajaxRisultatiGetPartite.aspx";
    
    public static String GetCampionati(String comitato, String regione, String provincia) {
    	long time = new Date().getTime();

    	String url = FipWebUrl + GetCampionatiUrl+ "?" 
				+ "com=" + comitato
				+ "&sesso="
				+ "&camp="
				+ "&fase="
				+ "&girone="
				+ "&ar=1"
				+ "&turno=1"
				+ "&IDRegione="+ regione
				+ "&IDProvincia=" + provincia
				+ "&reload="
				+ time;
		   	
    	return HTTPHelper.SendPost(url);
    }
    
    public static String GetRisultati(String regione, String comitato, String provincia) {
    	String url = FipWebUrl + "/" + GetPartiteUrl + "?" +
    			CreateGetPartiteUrl(regione, comitato, provincia);

    	return HTTPHelper.SendGet(url);
    }
    
    private static String CreateGetPartiteUrl(String regione, String comitato, String provincia) {
		// http://www.fip.it/risultati.aspx?IDRegione=AB&com=RAB&IDProvincia=PE
		String result = FipUrl + GetRisultatiUrl+ "?" + "IDRegione" + regione+ "&com" + comitato + "&IDProvincia" + provincia;
		
		return result;
	}
    
    // // http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?1617576752999&com=RTO&sesso=M
    // &camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined

    public static String GetPartite(Campionato championship) {
        String url = FipWebUrl + "/" + GetPartiteUrl + "?" +
                CreateGetPartiteUrl(championship);

        return HTTPHelper.SendGet(url);
    }

    private static String CreateGetPartiteUrl(Campionato championship) {
        // com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined
        String result = "com="+championship.getComitato()+"&sesso="+championship.getSesso()+"&camp="+championship.getId()+"&fase="+championship.getFase()+
                "&girone="+championship.getGirone()+"&ar="+(championship.getAndata() ? 0 : 1)+"&turno="+championship.getTurno()+"&IDRegione="+championship.getRegione()+
                "&IDProvincia="+championship.getProvincia();

        return result;

    }
    
    public static String GetDettaglioPartita(String comitato,  String regione, String provincia, 
    		String sesso, String campionato, String fase, String girone, Boolean andata, String turno, int numeroGara) {
		String url = FipWebUrl + "/" + GetPartiteUrl + "?" + "com="+comitato+"&sesso="+sesso+"&camp="+campionato+"&fase="+fase+
                "&girone="+girone+"&ar="+(andata ? 0 : 1)+"&turno="+turno+"&IDRegione="+regione+
                "&IDProvincia="+provincia+"&IDGara="+numeroGara;
		
		
		return HTTPHelper.SendGet(url);
	}
}
