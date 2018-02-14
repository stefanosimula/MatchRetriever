package WPUtils;

//Java Libraries
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Logging.LogLevel;
import Logging.WPLogger;

public class WPUtils {
    public static final String URL_BASE = "http://www.pisagap.it/wp-json/wp/v2/games";
    public static final String WP_LOGIN = "stefano.simula";
    public static final String WP_PSS   = "Eugenio27!";


    public static void RetrieveGames(String strJSON) {
        int      counter = 0;
        WPLogger logger  = WPLogger.getInstance();

        try {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "Trying to retrieve all games available: [" + strJSON + "]",
                       LogLevel.INFO);

            JSONParser jsonParser = new JSONParser();
            JSONArray  jsonObject = (JSONArray) jsonParser.parse(strJSON);
            JSONObject jsonGame, customFields;
            String     squadraA;

            for (int i = 0; i < jsonObject.size(); i++) {
                counter++;
                logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games Found", LogLevel.INFO);
                jsonGame     = (JSONObject) jsonObject.get(i);
                customFields = (JSONObject) jsonGame.get("acf");
                squadraA     = (String) customFields.get("squadra_a");
                logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Squadra A: " + squadraA, LogLevel.INFO);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "FAILED - Parse Exception : JSON [" + strJSON + "]",
                       LogLevel.ERROR);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "FAILED - Null Pointer Exception",
                       LogLevel.ERROR);
        }

        logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games founded : [" + counter + "]", LogLevel.INFO);
    }
    
    private static String GetJSONFromWP() {
        WPLogger logger = WPLogger.getInstance();
        String   result = "";

        try {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "Trying to retrieve: [" + URL_BASE + "]",
                       LogLevel.INFO);

            URL               url  = new URL(URL_BASE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                logger.Log(WPUtils.class.getName(),
                           logger.GetMethodName(),
                           "FAILED - HTTP error code : [" + conn.getResponseCode() + "]",
                           LogLevel.ERROR);

                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String         output;

            while ((output = br.readLine()) != null) {
                result += output;
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "FAILED - Malformed URL Exception : URL [" + URL_BASE + "]",
                       LogLevel.ERROR);
            e.printStackTrace();
        } catch (IOException e) {
            logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
            e.printStackTrace();
        }

        logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + result + "]", LogLevel.INFO);

        return result;
    }


    public static void SaveGame(int numeroGara, String squadraA, String squadraB) throws IOException {
        WPLogger logger = WPLogger.getInstance();

        try {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "Trying to save game: [" + numeroGara + "]",
                       LogLevel.INFO);

            JSONObject gameJSON = new JSONObject();

            gameJSON.put("slug", "rto_fi_1234");
            gameJSON.put("status", "publish");
            gameJSON.put("type", "games");

            JSONObject gameTitle = new JSONObject();

            gameTitle.put("rendered", "rto_fi_1234");
            gameJSON.put("title", gameTitle);

            JSONArray acfArray = new JSONArray();

            acfArray.add(new JSONObject().put("numero_gara", "1234"));
            acfArray.add(new JSONObject().put("squadra_a", "Squadra A"));
            acfArray.add(new JSONObject().put("squadra_b", "Squadra B"));
            acfArray.add(new JSONObject().put("punti_squadra_a", "63"));
            acfArray.add(new JSONObject().put("punti_squadra_b", "89"));
            acfArray.add(new JSONObject().put("arbitro_1", "Simula Stefano"));
            acfArray.add(new JSONObject().put("arbitro_2", "Bani Matteo"));
            acfArray.add(new JSONObject().put("data", "12/02/2018"));
            acfArray.add(new JSONObject().put("ora", "21.30"));
            gameJSON.put("acf", acfArray);
            SetJSONOnWP(gameJSON);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "FAILED - Null Pointer Exception",
                       LogLevel.ERROR);
        }

        logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Games Added correctly", LogLevel.INFO);
    }

    public static String SetJSONOnWP(JSONObject gameJSON) {
        WPLogger logger = WPLogger.getInstance();
        String   result = "";

        try {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "Trying to add: [" + gameJSON + "]",
                       LogLevel.INFO);

            URL               url        = new URL(URL_BASE);
            String            authStr    = WP_LOGIN + ":" + WP_PSS;
            String            encoding   = Base64.getEncoder().encodeToString(authStr.getBytes());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + encoding);

            OutputStream os = connection.getOutputStream();

            os.write(gameJSON.toJSONString().getBytes());
            os.flush();

            InputStream    content = (InputStream) connection.getInputStream();
            BufferedReader in      = new BufferedReader(new InputStreamReader(content));
            String         line;

            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            connection.disconnect();
        } catch (MalformedURLException e) {
            logger.Log(WPUtils.class.getName(),
                       logger.GetMethodName(),
                       "FAILED - Malformed URL Exception : URL [" + URL_BASE + "]",
                       LogLevel.ERROR);
            e.printStackTrace();
        } catch (IOException e) {
            logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "FAILED - IO Exception", LogLevel.ERROR);
            e.printStackTrace();
        }

        logger.Log(WPUtils.class.getName(), logger.GetMethodName(), "Complete : [" + result + "]", LogLevel.INFO);

        return result;
    }
}
