package FIPWebUtils;

import Utils.Utils;

public class FIPWebHelper {

    private static String FipWebUrl = "http://www.fip.it/FipWeb";
    private static String GetPartiteUrl = "ajaxRisultatiGetPartite.aspx";

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
