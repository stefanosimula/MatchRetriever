import java.io.IOException;

import WPUtils.WPUtils;

public class MatchRetriever {
	
    public static void main(String[] args) throws IOException {
        System.out.println("***** MATCH RETRIEVER START *****");
/*
        DataManager dataManager = new DataManager();
        List<Regione> regioni = dataManager.InitializeData();
        
        Iterator<Regione> regIt = regioni.iterator();
        while(regIt.hasNext()) {
            Regione regione = (Regione) regIt.next();
            
            for(int i = 0; i < regione.getProvince().size(); i++)
            {
            	Provincia prov = regione.getProvince().get(i);
            	Comitato com = regione.getComitati().get(i);
            	
            	String content = FIPWebHelper.GetCampionati(com.getNome(), regione.getID(), prov.getNome());
                System.out.println(content);
                
            	//FIPWebHelper.GetPartite(comitato, sesso, campionato, fase, girone, andata, turno, regione, provincia)
            	//String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajax-risultati-get-navbar-campionati.aspx?"+time);
            	//String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?"+time+"&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined");
            }
        }
               
        */
        //WPUtils.SaveGame(1243, "","");
        
        try {
			WPUtils.SaveGame(1324, "", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
        
        System.out.println("***** MATCH RETRIEVER STOP *****");
    }
}
