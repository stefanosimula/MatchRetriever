package WPUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Logging.LogLevel;
import Logging.WPLogger;

public class WPUtils {
	public static void RetrieveGames(String strJSON) {
		int counter = 0;
		WPLogger logger = WPLogger.getInstance();
		
		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to retrieve all games available: ["+strJSON+"]", LogLevel.INFO);

			JSONParser jsonParser = new JSONParser();
			JSONArray jsonObject = (JSONArray) jsonParser.parse(strJSON);

			JSONObject jsonGame, customFields;
			String squadraA;
			for(int i =0; i < jsonObject.size(); i++) {
				counter++;
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games Found", LogLevel.INFO);
				
				jsonGame = (JSONObject) jsonObject.get(i);
				customFields = (JSONObject) jsonGame.get("acf");
				
				squadraA = (String) customFields.get("squadra_a");
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Squadra A: "+squadraA, LogLevel.INFO);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Parse Exception : JSON ["+strJSON+"]", LogLevel.ERROR);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Null Pointer Exception", LogLevel.ERROR);
		}
		
		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games founded : ["+counter+"]", LogLevel.INFO);
	}
	
	
	public static String GetJSONFromWP(String strUrl) {
		
		WPLogger logger = WPLogger.getInstance();

		String result = "";

		try {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Trying to retrieve: ["+strUrl+"]", LogLevel.INFO);

			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - HTTP error code : ["+conn.getResponseCode()+"]", LogLevel.ERROR);

				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;

			while ((output = br.readLine()) != null) {
				result += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - Malformed URL Exception : URL ["+strUrl+"]", LogLevel.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
			e.printStackTrace();
		}

		logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : ["+result+"]", LogLevel.INFO);

		return result;
	}
}

