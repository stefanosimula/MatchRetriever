package Utils.FIPWeb;

import java.sql.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.DataManager;
import Data.Objects.Campionato;
import Data.Objects.Comitato;
import Data.Objects.Partita;
import Data.Objects.Sesso;
import Logging.LogLevel;
import Logging.WPLogger;

public class FIPWebParser {
    private static String DivPartitaClass     = "div-risultato-partita";
    private static String NomeSquadra1        = "nome-squadra nome-squadra-1";
    private static String NomeSquadra2        = "nome-squadra nome-squadra-2";
    private static String PunteggioSquadra1   = "risultato-squadra risultato-squadra-1";
    private static String PunteggioSquadra2   = "risultato-squadra risultato-squadra-2";
    private static String NumeroPartitaClass  = "numero-partita";
    private static String ArbitriClass        = "luogo-arbitri";
    private static String DataOraTag          = "strong";
    private static String LinkCampionatoClass = "menuRisultati";
    private static int    ComitatoLength      = 3;
    private static int    SessoLength         = 1;

    private static WPLogger logger = WPLogger.getInstance();
    
    public static List<Campionato> ParseGetCampionati(String htmlGetCampionatiResult) {
    	
    	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Trying to parse getCampionatiResult: ["+htmlGetCampionatiResult+"]", LogLevel.INFO);
    	
        List<Campionato> result          = new ArrayList<Campionato>();
        Document         doc             = Jsoup.parse(htmlGetCampionatiResult);
        Elements         listCampionati = doc.getElementsByClass(LinkCampionatoClass);
        
        Element          elementCampionato;
        for (int i = 0; i < listCampionati.size(); i++) {
            elementCampionato = listCampionati.get(i);
            
            Elements linksGirone = elementCampionato.getElementsByTag("a");
            if(linksGirone.size() == 3) {
            	Element nomeCampionato = linksGirone.get(0);
            	Element nomeFase = linksGirone.get(1);
            	Element linkGirone = linksGirone.get(2);
            	
        		String hrefValue     = linkGirone.attr("href");
        		String[] campionatoData = hrefValue.split("'");
        		// function getRisultatiPartite(Comitato, Sesso, Campionato, Fase, Girone, AR, Turno, IDRegione, IDProvincia, reload)
        		// javascript:getRisultatiPartite('RTO', 'M', 'PM', '1', '25876', '0', '7', 'TO', 'FI')
        		if(campionatoData.length >= 18) {
        			String comitato = campionatoData[1];
            		Sesso sesso = campionatoData[3] == "M" ? Sesso.Maschile : Sesso.Femminile;
            		String id = campionatoData[5];
            		String regione = campionatoData[15];
            		String provincia = campionatoData[17];	
            		String fase = campionatoData[7];
            		String girone = campionatoData[9];
            		Boolean andata = campionatoData[11] == "0";
            		String turno = campionatoData[13];
            		
            		logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Found campionato: ["+nomeCampionato.text()+" "+ id +" "+nomeFase.text()+" "+comitato+" "+regione+" "+provincia+"]", LogLevel.INFO);
            		
            		result.add(new Campionato(comitato, regione, provincia, id, nomeCampionato.text(), sesso, fase, girone, andata, turno));
        		}
            }
        }

        logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Parse getCampionatiResult complete", LogLevel.INFO);
        
        return result;
    }

	@SuppressWarnings("deprecation")
	public static ArrayList<Partita> ParseGetPartiteResult(String htmlGetPartiteResult) {
    	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Trying to parse GetPartiteResult: ["+htmlGetPartiteResult+"]", LogLevel.INFO);
    	
        ArrayList<Partita> games = new ArrayList<Partita>();
        Document           doc     = Jsoup.parse(htmlGetPartiteResult);
        Element            table   = doc.getElementById("table-risultati");
        Partita            partita;
        String                numeroGara;
        int puntiA = 0, puntiB = 0;
        String            squadraA, squadraB;
        Date               date;
        Time               time;
        Element            row, row2;
        
        if(htmlGetPartiteResult.isEmpty()) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "htmlGetPartiteResult is empty", LogLevel.INFO);
    		return games;
    	}
                
        Elements           rows = table.getElementsByTag("tr");

        for (int i = 0; i < rows.size() + 1; i = i + 2) {
            row = rows.get(i);

            // Avoid empty rows
            if (row.getElementsByClass(DivPartitaClass).size() != 0) {

                // if this is the last row, exit
                if (i == rows.size() - 1) {
                    break;
                }

                // otherwise go to the next row
                i++;
                row = rows.get(i);
            }
            
            

            /*
             * <td class="nome-squadra nome-squadra-1">ENIC PINO DRAGONS FIRENZE</td>
             * <td class="risultato-squadra risultato-squadra-1">72</td>
             */
            Elements teamANameCols = row.getElementsByClass(NomeSquadra1);

            if (teamANameCols.size() == 0) {
                logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Team A Name in :["+NomeSquadra1+"]", LogLevel.WARN);
                break;
            }

            Element teamANameCol = teamANameCols.get(0);
            squadraA = teamANameCol.text();

            Elements teamAPointsCols = row.getElementsByClass(PunteggioSquadra1);

            if (teamAPointsCols.size() == 0) {
                logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Point of Team A :["+PunteggioSquadra1+"]", LogLevel.WARN);
                break;
            }

            Element teamAPointsCol = teamAPointsCols.get(0);
            if (teamAPointsCol != null && !teamAPointsCol.text().isEmpty()) {
            	puntiA = Integer.parseInt(teamAPointsCol.text());
            }            

            /*
             * <td class="risultato-squadra risultato-squadra-2">64</td>
             * <td class="nome-squadra nome-squadra-2">LOREX SPORT VALDISIEVE</td>
             */
            Elements teamBNameCols = row.getElementsByClass(NomeSquadra2);

            if (teamBNameCols.size() == 0) {
            	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Team B Name :["+NomeSquadra2+"]", LogLevel.WARN);
                break;
            }

            Element teamBNameCol = teamBNameCols.get(0);
            squadraB = teamBNameCol.text();

            Elements teamBPointsCols = row.getElementsByClass(PunteggioSquadra2);

            if (teamBNameCols.size() == 0) {
            	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Point of Team B :["+PunteggioSquadra2+"]", LogLevel.WARN);
                break;
            }
            
            Element teamBPointsCol = teamBPointsCols.get(0);
            if (teamBPointsCol != null && !teamBPointsCol.text().isEmpty()) {
            	puntiB = Integer.parseInt(teamBPointsCol.text());
            } 

            row2   = rows.get(i + 1);

            /*
             * <td class="numero-partita"><a title="Visualizza dettaglio gara" href="javascript:getDettaglioPartita('RTO', 'M', 'C1', '1', '25768', '0', '3', 'TO', 'FI', '', '001399');">001399</a></td>
             */
            Element matchNumberCol = row2.getElementsByClass(NumeroPartitaClass).get(0);
            Element gameData = matchNumberCol.getElementsByTag("a").get(0);
            numeroGara = gameData.text();
            
            String hrefValue     = gameData.attr("href");
            String[] campionatoData = hrefValue.split("'");
    		// function getRisultatiPartite(Comitato, Sesso, Campionato, Fase, Girone, AR, Turno, IDRegione, IDProvincia, reload, NumeroGara)
    		// javascript:getDettaglioPartita('RTO', 'F', 'U13/M', '3', '27494', '1', '1', 'TO', 'FI', '', '009380')
    		//if(campionatoData.length >= 18) {
    			String comitato = campionatoData[1];
        		Sesso sesso = campionatoData[3] == "M" ? Sesso.Maschile : Sesso.Femminile;
        		String campionato = campionatoData[5];
        		String regione = campionatoData[15];
        		String provincia = campionatoData[17];	
        		String fase = campionatoData[7];
        		String girone = campionatoData[9];
        		Boolean andata = campionatoData[11] == "0";
        		String turno = campionatoData[13];
    		//}            

            /*
             * <td class="luogo-arbitri" colspan="5">
             *   GIUSTARINI I. (GR) - PIRAM M. (LI) - [<i>guidi d. (fi)</i>]
             *   <br /><strong>27-01-2018 - 18:30</strong>
             * </td>
             */
            Elements refereesCols = row2.getElementsByClass(ArbitriClass);

            if (refereesCols.size() == 0) {
                logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find referees part :["+ArbitriClass+"]", LogLevel.WARN);
                break;
            }

            Element  refereesCol       = refereesCols.get(0);
            Elements matchDateTimeCols = row2.getElementsByTag(DataOraTag);
            
            String referees = refereesCol.text();
            
            referees = referees.substring(0, referees.indexOf(matchDateTimeCols.text()));
            String[] splits            = referees.split(" - ");
            String arbitro1 = "", arbitro2 = "", arbitro3 = "";
            if(splits.length == 2) {
            	arbitro1 = splits[0];
            	arbitro2 = splits[1];
            } else if(splits.length == 3) {
            	arbitro1 = splits[0];
            	arbitro2 = splits[1];
            	arbitro3 = splits[2];
            }
            
            if (matchDateTimeCols.size() == 0) {
                logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find time and date part:["+DataOraTag+"]", LogLevel.WARN);
                break;
            }

            String[] tmp       = matchDateTimeCols.get(0).text().split(" - ");
            String[] dataElems = tmp[0].split("-");

            date = new Date(Integer.parseInt(dataElems[2]) - 1900,
                            Integer.parseInt(dataElems[1]) - 1,
                            Integer.parseInt(dataElems[0]));

            String[] timeElems = tmp[1].split(":");

            time = new Time(Integer.parseInt(timeElems[0]), Integer.parseInt(timeElems[1]), 0);

            
            partita = new Partita("", comitato, regione, provincia, campionato, sesso, fase, girone, andata, turno, 
            		numeroGara, squadraA, squadraB, puntiA, puntiB, "", date, time, 
            		arbitro1, arbitro2, arbitro3, "", "", "", "", "");

            games.add(partita);
            return games;
            
            //logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Game added:["+comitato+" "+regione+" "+provincia+" "+campionato+" "+numeroGara+"]", LogLevel.INFO);
        }

        logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Parse GetPartiteResult complete", LogLevel.INFO);
        
        return games;
    }
    
    @SuppressWarnings("deprecation")
	public static Partita ParseGetDettaglioPartitaResult(String htmlGetDettaglioPartitaResult) {
    	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Trying to parse GetDettaglioPartitaResult: ["+htmlGetDettaglioPartitaResult+"]", LogLevel.INFO);
    	
        Document           doc     = Jsoup.parse(htmlGetDettaglioPartitaResult);
        Element            table   = doc.getElementById("table-risultati");
        Partita            partita;
        
        String             numeroGara;
        int puntiA = 0, puntiB = 0;
        String            squadraA, squadraB, campo;
        String arbitro1, arbitro2, arbitro3, osservatore;
        String udc1, udc2, udc3, provvedimenti = "";
        Date               date;
        Time               time;
        Element            row, row2;
        
        if(htmlGetDettaglioPartitaResult.isEmpty()) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "htmlGetDettaglioPartitaResult is empty", LogLevel.INFO);
    		return null;
    	}
                
        Elements rows = table.getElementsByClass("nome-squadra nome-squadra-1");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Team A Name", LogLevel.ERROR);
        	return null;
        }
        
        squadraA = rows.get(0).text();
        
        rows = table.getElementsByClass("risultato-squadra risultato-squadra-1");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find points of Team A", LogLevel.ERROR);
        	return null;
        }
        
        puntiA = Integer.parseInt(rows.get(0).text());
        
        rows = table.getElementsByClass("nome-squadra nome-squadra-2");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find Team B Name", LogLevel.ERROR);
        	return null;
        }
        
        squadraB = rows.get(0).text();
        
        rows = table.getElementsByClass("risultato-squadra risultato-squadra-2");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find points of Team B", LogLevel.ERROR);
        	return null;
        }
        
        puntiB = Integer.parseInt(rows.get(0).text());
        
        rows = table.getElementsByClass("numero-partita");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find game ID", LogLevel.ERROR);
        	return null;
        }
        
        numeroGara = rows.get(0).text();
        
        
        rows = table.getElementsByClass("luogo-arbitri");
        if(rows.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find game information", LogLevel.ERROR);
        	return null;
        }
        
        Element moreInfo = rows.get(0);
        Elements dateTimeInfo = moreInfo.getElementsByTag("strong");
        if(dateTimeInfo.size() == 0) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find date and time game", LogLevel.ERROR);
        	return null;
        }
        
        String strDateTime = dateTimeInfo.get(0).text();
                
        String[] tmp       = strDateTime.split(" - ");
        String[] dataElems = tmp[0].split("/");

        date = new Date(Integer.parseInt(dataElems[2]) - 1900,
                        Integer.parseInt(dataElems[1]) - 1,
                        Integer.parseInt(dataElems[0]));

        String[] timeElems = tmp[1].split(":");

        time = new Time(Integer.parseInt(timeElems[0]), Integer.parseInt(timeElems[1]), 0);
        
        
        if(rows.size() < 4) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find field game information", LogLevel.ERROR);
        	return null;
        }
        
        Element fieldEl = rows.get(3);
        // <strong>Campo di gioco</strong>: PALASPORT &#39;B. MACCHIA&#39;, Via S. Allende  2 LIVORNO (LI)
        String strFieldElement = fieldEl.text();
        System.out.println(strFieldElement);
        campo = strFieldElement.substring(strFieldElement.indexOf(":"), strFieldElement.length());
        // TODO clean string from special character
        campo = campo.replaceAll("&#39;", "");
        
        if(rows.size() < 11) {
        	logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "WARNING: Not able to find referees game information", LogLevel.ERROR);
        	return null;
        }
        Element fieldReferee = rows.get(4);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        String strRefElement = fieldEl.text();
        arbitro1 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(5);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        arbitro2 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(6);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        arbitro3 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(7);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        osservatore = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(8);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        udc1 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(9);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        udc2 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
        
        fieldReferee = rows.get(10);
        // <strong>1&deg; Arbitro</strong>: CORSO MARCO di PISA (PI)
        strRefElement = fieldEl.text();
        udc3 = strRefElement.substring(strRefElement.indexOf(":"), strRefElement.length());
            
            partita = new Partita("", "", "", "", "", Sesso.Maschile, "", "", true, "", 
            		numeroGara, squadraA, squadraB, puntiA, puntiB, campo, date, time, 
            		arbitro1, arbitro2, arbitro3, osservatore, udc1, udc2, udc3, provvedimenti);
        
        logger.Log(FIPWebParser.class.getName(), logger.GetMethodName(), "Parse GetPartiteResult complete", LogLevel.INFO);
        
        return partita;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
