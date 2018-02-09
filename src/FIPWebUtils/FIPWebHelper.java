package FIPWebUtils;

import java.util.Date;

import Utils.Utils;

public class FIPWebHelper {

	private static String FipUrl = "http://www.fip.it/";
    private static String FipWebUrl = "http://www.fip.it/FipWeb";
    private static String GetCampionatiUrl = "ajax-risultati-get-navbar-campionati.aspx";
    private static String GetRisultatiUrl = "risultati.aspx";
    private static String GetPartiteUrl = "ajaxRisultatiGetPartite.aspx";
    
    public static String GetCampionati(String comitato, String regione, String provincia) {
    	long time = new Date().getTime();

    	String url = FipUrl + GetRisultatiUrl+ "?" 
				+ "com" + comitato
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
		   	
    	return Utils.GetWebPage(url);
    }
    
    public static String GetRisultati(String regione, String comitato, String provincia) {
    	String url = FipWebUrl + "/" + GetPartiteUrl + "?" +
    			CreateGetPartiteUrl(regione, comitato, provincia);

    	return Utils.GetWebPage(url);
    }
    
    private static String CreateGetPartiteUrl(String regione, String comitato, String provincia) {
		// http://www.fip.it/risultati.aspx?IDRegione=AB&com=RAB&IDProvincia=PE
		String result = FipUrl + GetRisultatiUrl+ "?" + "IDRegione" + regione+ "&com" + comitato + "&IDProvincia" + provincia;
		
		return result;
	}
    
    // // http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?1617576752999&com=RTO&sesso=M
    // &camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined

    public static String GetPartite(String comitato, String sesso, String campionato, String fase, String girone, Boolean andata,
                             String turno, String regione, String provincia) {
        String url = FipWebUrl + "/" + GetPartiteUrl + "?" +
                CreateGetPartiteUrl(comitato, sesso, campionato, fase, girone, andata, turno, regione, provincia);

        return Utils.GetWebPage(url);
    }

    private static String CreateGetPartiteUrl(String comitato, String sesso, String campionato, String fase, String girone, Boolean andata,
                                       String turno, String regione, String provincia) {
        // com=RTO&sesso=M
        //    // &camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined
        String result = "com="+comitato+"&sesso="+sesso+"&camp="+campionato+"&fase="+fase+
                "&girone="+girone+"&ar="+(andata ? 0 : 1)+"&turno="+turno+"&IDRegione="+regione+
                "&IDProvincia="+provincia;

        return result;

    }

}
