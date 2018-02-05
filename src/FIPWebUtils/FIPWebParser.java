package FIPWebUtils;

import Data.Partita;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class FIPWebParser {

    private static String DivPartitaClass = "div-risultato-partita";
    private static String NomeSquadra1 = "nome-squadra nome-squadra-1";
    private static String NomeSquadra2 = "nome-squadra nome-squadra-2";
    private static String PunteggioSquadra1 = "risultato-squadra risultato-squadra-1";
    private static String PunteggioSquadra2 = "risultato-squadra risultato-squadra-2";
    private static String NumeroPartitaClass = "numero-partita";

    public static ArrayList<Partita> ParseGetPartiteResult(String htmlGetPartiteResult) {
        ArrayList<Partita> matches = new ArrayList<Partita>();

        Document doc = Jsoup.parse(htmlGetPartiteResult);

        Element table = doc.getElementById("table-risultati");

        Partita partita;
        Element row, row2;

        Elements rows = table.getElementsByTag("tr");
        for (int i = 0; i < rows.size() + 1; i = i + 2) {
            row = rows.get(i);

            // Avoid empty rows
            if (row.getElementsByClass(DivPartitaClass).size() != 0) {
                // if this is the last row, exit
                if(i == rows.size() - 1) {
                    break;
                }

                // otherwise go to the next row
                i++;
                row = rows.get(i);
            }

            partita = new Partita();

            /*
            <td class="nome-squadra nome-squadra-1">ENIC PINO DRAGONS FIRENZE</td>
            <td class="risultato-squadra risultato-squadra-1">72</td>
             */

            Elements teamANameCols = row.getElementsByClass(NomeSquadra1);
            if (teamANameCols.size() == 0) {
                System.out.println("[ParseGetPartiteResult] ERROR : not able to find: " + NomeSquadra1);
                break;
            }

            Element teamANameCol = teamANameCols.get(0);
            partita.setSquadraA(teamANameCol.text());

            Elements teamAPointsCols = row.getElementsByClass(PunteggioSquadra1);
            if (teamAPointsCols.size() == 0) {
                System.out.println("[ParseGetPartiteResult] ERROR : not able to find: " + PunteggioSquadra1);
                break;
            }

            Element teamAPointsCol = teamAPointsCols.get(0);
            partita.setPuntiA(Integer.parseInt(teamAPointsCol.text()));

            /*
            <td class="risultato-squadra risultato-squadra-2">64</td>
            <td class="nome-squadra nome-squadra-2">LOREX SPORT VALDISIEVE</td>
             */

            Elements teamBNameCols = row.getElementsByClass(NomeSquadra2);
            if (teamBNameCols.size() == 0) {
                System.out.println("[ParseGetPartiteResult] ERROR : not able to find: " + NomeSquadra2);
                break;
            }

            Element teamBNameCol = teamBNameCols.get(0);
            partita.setSquadraB(teamBNameCol.text());

            Elements teamBPointsCols = row.getElementsByClass(PunteggioSquadra2);
            if (teamBNameCols.size() == 0) {
                System.out.println("[ParseGetPartiteResult] ERROR : not able to find: " + PunteggioSquadra2);
                break;
            }

            Element teamBPointsCol = teamBPointsCols.get(0);
            partita.setPuntiB(Integer.parseInt(teamBPointsCol.text()));

            row2 = rows.get(i + 1);

            /*
            <td class="numero-partita"><a title="Visualizza dettaglio gara" href="javascript:getDettaglioPartita('RTO', 'M', 'C1', '1', '25768', '0', '3', 'TO', 'FI', '', '001399');">001399</a></td>
             */

            Element matchNumberCol = row2.getElementsByClass(NumeroPartitaClass).get(0);
            int matchNumber = Integer.parseInt(matchNumberCol.getElementsByTag("a").get(0).text());
            partita.setID(matchNumber);

            /*
            <td class="luogo-arbitri" colspan="5">
                GIUSTARINI I. (GR) - PIRAM M. (LI) - [<i>guidi d. (fi)</i>]
                <br /><strong>27-01-2018 - 18:30</strong>
            </td>
             */

            System.out.println("[ParseGetPartiteResult] SUCCESS - find: " + partita.toString());

            matches.add(partita);
        }

        return matches;
    }
}
