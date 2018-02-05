import FIPWebUtils.FIPWebParser;
import Utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class MatchRetriever {
    public static void main(String[] args) throws IOException {
        System.out.println("***** MATCH RETRIEVER START *****");

        long time = new Date().getTime();
        // http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?1617576752999&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined
        String content = Utils.GetWebPage("http://www.fip.it/FipWeb/ajaxRisultatiGetPartite.aspx?"+time+"&com=RTO&sesso=M&camp=D&fase=1&girone=25770&ar=0&turno=2&IDRegione=TO&IDProvincia=FI&reload=undefined");
        System.out.println(content);

        String everything = "";
        BufferedReader br = new BufferedReader(new FileReader("/Users/Ste/IdeaProjects/MatchRetriever/src/Utils/GetPartiteResult.html"));
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


        System.out.println("***** MATCH RETRIEVER STOP *****");
    }
}
