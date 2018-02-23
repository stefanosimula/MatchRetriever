package Utils.WordPress;

//Java Libraries
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Data.Objects.Partita;
import Data.Objects.Sesso;
import Logging.LogLevel;
import Logging.WPLogger;

public class WPUtils {
	public static final String URL_BASE = "http://www.pisagap.it/wp-json/wp/v2/games";
	public static final String WP_LOGIN = "stefano.simula";
	public static final String WP_PSS = "Eugenio27!";

	public static final String WP_ACF_SEC = "acf";
	public static final String WP_ID = "id";
	public static final String WP_COMITATO = "comitato";
	public static final String WP_REGIONE = "regione";
	public static final String WP_PROVINCIA = "provincia";
	public static final String WP_CAMPIONATO = "campionato";
	public static final String WP_SESSO = "sesso";
	public static final String WP_FASE = "fase";
	public static final String WP_GIRONE = "girone";
	public static final String WP_ANDATA = "andata";
	public static final String WP_TURNO = "turno";

	public static final String WP_NUMERO_GARA = "numero_gara";
	public static final String WP_SQUADRA_A = "squadra_a";
	public static final String WP_SQUADRA_B = "squadra_b";
	public static final String WP_PUNTI_SQUADRA_A = "punti_squadra_a";
	public static final String WP_PUNTI_SQUADRA_B = "punti_squadra_b";
	public static final String WP_ARBITRO_1 = "arbitro_1";
	public static final String WP_ARBITRO_2 = "arbitro_2";
	public static final String WP_ARBITRO_3 = "arbitro_3";
	public static final String WP_OSSERVATORE = "osservatore";
	public static final String WP_UDC_1 = "udc_1";
	public static final String WP_UDC_2 = "udc_2";
	public static final String WP_UDC_3 = "udc_3";
	public static final String WP_DATA = "data";
	public static final String WP_ORA = "ora";
	public static final String WP_CAMPO = "campo";
	public static final String WP_PROVVEDIMENTI = "provvedimenti";

	public static List<Partita> RetrieveGames(String strJSON) {
		int counter = 0;
		WPLogger logger = WPLogger.getInstance();
		List<Partita> games = new ArrayList<Partita>();

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"Trying to retrieve all games available: [" + strJSON + "]", LogLevel.INFO);

			JSONParser jsonParser = new JSONParser();
			JSONArray jsonObject = (JSONArray) jsonParser.parse(strJSON);
			JSONObject jsonGame, customFields;
			String squadraA;

			for (int i = 0; i < jsonObject.size(); i++) {
				counter++;
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games Found", LogLevel.INFO);
				jsonGame = (JSONObject) jsonObject.get(i);
				customFields = (JSONObject) jsonGame.get(WP_ACF_SEC);
				squadraA = (String) customFields.get(WP_SQUADRA_A);
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Squadra A: " + squadraA, LogLevel.INFO);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"FAILED - Parse Exception : JSON [" + strJSON + "]", LogLevel.ERROR);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Null Pointer Exception",
					LogLevel.ERROR);
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games founded : [" + counter + "]", LogLevel.INFO);

		return games;
	}

	private static String GetJSONFromWP() {
		WPLogger logger = WPLogger.getInstance();
		String result = "";

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to retrieve: [" + URL_BASE + "]",
					LogLevel.INFO);

			URL url = new URL(URL_BASE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
						"FAILED - HTTP error code : [" + conn.getResponseCode() + "]", LogLevel.ERROR);

				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;

			while ((output = br.readLine()) != null) {
				result += output;
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"FAILED - Malformed URL Exception : URL [" + URL_BASE + "]", LogLevel.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
			e.printStackTrace();
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + result + "]", LogLevel.INFO);

		return result;
	}

	// http://www.pisagap.it/wp-json/wp/v2/games?slug=rto_fi_1234
	public static Partita GetGame(Partita game) {
		// WPLogger logger = WPLogger.getInstance();

		String gameJSON = GetGameJSON(game);
		Partita partita = ParseGetGameJSON(gameJSON);

		return partita;
	}

	private static String GetGameJSON(Partita game) {
		String gameJSON = "";
		WPLogger logger = WPLogger.getInstance();

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Search this game: [" + game.getComitato() + " "
					+ game.getProvincia() + " " + game.getNumeroGara() + "]", LogLevel.INFO);

			URL url = new URL(
					URL_BASE + "?slug=" + game.getComitato() + "_" + game.getProvincia() + "_" + game.getNumeroGara());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "URL: [" + url + "]", LogLevel.INFO);

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
						"FAILED - HTTP error code : [" + conn.getResponseCode() + "]", LogLevel.ERROR);

				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;

			while ((output = br.readLine()) != null) {
				gameJSON += output;
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"FAILED - Malformed URL Exception : URL [" + URL_BASE + "]", LogLevel.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
			e.printStackTrace();
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + gameJSON + "]", LogLevel.INFO);

		return gameJSON;
	}

	@SuppressWarnings("deprecation")
	private static Partita ParseGetGameJSON(String getGameJSON) {
		Partita partita = null;

		JSONParser jsonParser = new JSONParser();
		JSONArray jsonObjects;
		try {
			jsonObjects = (JSONArray) jsonParser.parse(getGameJSON);

			String id, comitato, regione, provincia;
			String campionato, fase, girone, turno;
			Sesso sesso;
			Boolean andata;
			int numeroGara, puntiA, puntiB;
			String squadraA, squadraB;
			String arbitro1, arbitro2, arbitro3, osservatore, udc1, udc2, udc3;
			String data, ora, campo, provvedimenti;
			if (jsonObjects.size() == 1) {

				JSONObject jsonObj = (JSONObject) jsonObjects.get(0);
				JSONObject customFields = (JSONObject) jsonObj.get(WP_ACF_SEC);

				id = jsonObj.get(WP_ID).toString();
				comitato = (String) customFields.get(WP_COMITATO);
				regione = (String) customFields.get(WP_REGIONE);
				provincia = (String) customFields.get(WP_PROVINCIA);
				campionato = (String) customFields.get(WP_CAMPIONATO);
				sesso = (String) customFields.get(WP_SESSO) == "M" ? Sesso.Maschile : Sesso.Femminile;
				fase = (String) customFields.get(WP_FASE);
				girone = (String) customFields.get(WP_GIRONE);
				andata = (String) customFields.get(WP_ANDATA) == "0";
				turno = (String) customFields.get(WP_TURNO);

				numeroGara = Integer.parseInt((String) customFields.get(WP_NUMERO_GARA));
				squadraA = (String) customFields.get(WP_SQUADRA_A);
				squadraB = (String) customFields.get(WP_SQUADRA_B);
				puntiA = Integer.parseInt((String) customFields.get(WP_PUNTI_SQUADRA_A));
				puntiB = Integer.parseInt((String) customFields.get(WP_PUNTI_SQUADRA_B));
				arbitro1 = (String) customFields.get(WP_ARBITRO_1);
				arbitro2 = (String) customFields.get(WP_ARBITRO_2);
				arbitro3 = (String) customFields.get(WP_ARBITRO_3);
				osservatore = (String) customFields.get(WP_OSSERVATORE);
				udc1 = (String) customFields.get(WP_UDC_1);
				udc2 = (String) customFields.get(WP_UDC_2);
				udc3 = (String) customFields.get(WP_UDC_3);
				provvedimenti = (String) customFields.get(WP_PROVVEDIMENTI);

				campo = (String) customFields.get(WP_CAMPO);
				
				data = (String) customFields.get(WP_DATA);
				String[] dataSplit = data.split("/");
				
				Date date = new Date(Integer.parseInt(dataSplit[2]) - 1900,
                        Integer.parseInt(dataSplit[1]) - 1,
                        Integer.parseInt(dataSplit[0]));

				
				ora = (String) customFields.get(WP_ORA);
				String[] timeSplit = ora.split(":");
				
				Time time = new Time(Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), 0);

				partita = new Partita(id, comitato, regione, provincia, campionato, sesso, fase, girone, andata, turno,
						numeroGara, squadraA, squadraB, puntiA, puntiB, campo, date, time, arbitro1, arbitro2, arbitro3,
						osservatore, udc1, udc2, udc3, provvedimenti);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return partita;
	}

	public static boolean UpdateGame(String id, Partita game) {
		boolean result = false;
		WPLogger logger = WPLogger.getInstance();

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to update game: [" + game + "]",
					LogLevel.INFO);

			JSONObject gameJSON = CreateJSONGame4WP(game);
			String strUpdateRes = UpdateJSONOnWP(id, gameJSON);
			
			result = strUpdateRes.isEmpty();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Null Pointer Exception",
					LogLevel.ERROR);
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games updated correctly", LogLevel.INFO);
		
		return result;
	}
	
	private static String UpdateJSONOnWP(String id, JSONObject gameJSON) {
		WPLogger logger = WPLogger.getInstance();
		String result = "";

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to add: [" + gameJSON + "]",
					LogLevel.INFO);

			URL url = new URL(URL_BASE+ "/" + id);
			String authStr = WP_LOGIN + ":" + WP_PSS;
			String encoding = Base64.getEncoder().encodeToString(authStr.getBytes());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Basic " + encoding);

			OutputStream os = connection.getOutputStream();

			os.write(gameJSON.toJSONString().getBytes());
			os.flush();

			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;

			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

			connection.disconnect();
		} catch (MalformedURLException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"FAILED - Malformed URL Exception : URL [" + URL_BASE + "]", LogLevel.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
			e.printStackTrace();
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + result + "]", LogLevel.INFO);

		return result;
	}
	
	public static void SaveGame(Partita game) {
		WPLogger logger = WPLogger.getInstance();

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to save game: [" + game + "]",
					LogLevel.INFO);

			JSONObject gameJSON = CreateJSONGame4WP(game);

			SetJSONOnWP(gameJSON);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Null Pointer Exception",
					LogLevel.ERROR);
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games Added correctly", LogLevel.INFO);
	}

	@SuppressWarnings({ "unchecked" })
	private static JSONObject CreateJSONGame4WP(Partita game) {
		JSONObject gameJSON = new JSONObject();

		gameJSON.put("slug", game.getComitato() + "_" + game.getProvincia() + "_" + game.getNumeroGara());
		gameJSON.put("status", "publish");
		gameJSON.put("type", "games");

		gameJSON.put("title", game.getComitato() + "_" + game.getProvincia() + "_" + game.getNumeroGara());

		JSONObject customFields = new JSONObject();

		customFields.put(WP_COMITATO, game.getComitato());
		customFields.put(WP_REGIONE, game.getRegione());
		customFields.put(WP_PROVINCIA, game.getProvincia());
		customFields.put(WP_CAMPIONATO, game.getCampionato());
		customFields.put(WP_SESSO, game.getSesso().toString());
		customFields.put(WP_FASE, game.getFase());
		customFields.put(WP_GIRONE, game.getGirone());
		customFields.put(WP_ANDATA, game.getAndata() ? 0 : 1);
		customFields.put(WP_TURNO, game.getTurno());

		customFields.put(WP_NUMERO_GARA, game.getNumeroGara());
		customFields.put(WP_SQUADRA_A, game.getSquadraA());
		customFields.put(WP_SQUADRA_B, game.getSquadraB());
		customFields.put(WP_PUNTI_SQUADRA_A, game.getPuntiA());
		customFields.put(WP_PUNTI_SQUADRA_B, game.getPuntiB());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String sDate= dateFormat.format(game.getData());
		customFields.put(WP_DATA, sDate);
		
		SimpleDateFormat dateTime = new SimpleDateFormat("HH:mm");
		String sTime= dateTime.format(game.getOra());
		customFields.put(WP_ORA, sTime);
		
		customFields.put(WP_CAMPO, game.getCampo());
		customFields.put(WP_ARBITRO_1, game.getArbitro1());
		customFields.put(WP_ARBITRO_2, game.getArbitro2());
		customFields.put(WP_ARBITRO_3, game.getArbitro3());
		customFields.put(WP_UDC_1, game.getUdC1());
		customFields.put(WP_UDC_2, game.getUdC2());
		customFields.put(WP_UDC_3, game.getUdC3());
		customFields.put(WP_PROVVEDIMENTI, game.getProvvedimenti());

		gameJSON.put("fields", customFields);

		return gameJSON;
	}

	private static String SetJSONOnWP(JSONObject gameJSON) {
		WPLogger logger = WPLogger.getInstance();
		String result = "";

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to add: [" + gameJSON + "]",
					LogLevel.INFO);

			URL url = new URL(URL_BASE);
			String authStr = WP_LOGIN + ":" + WP_PSS;
			String encoding = Base64.getEncoder().encodeToString(authStr.getBytes());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Basic " + encoding);

			OutputStream os = connection.getOutputStream();

			os.write(gameJSON.toJSONString().getBytes());
			os.flush();

			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;

			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

			connection.disconnect();
		} catch (MalformedURLException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(),
					"FAILED - Malformed URL Exception : URL [" + URL_BASE + "]", LogLevel.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
			e.printStackTrace();
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + result + "]", LogLevel.INFO);

		return result;
	}
}
