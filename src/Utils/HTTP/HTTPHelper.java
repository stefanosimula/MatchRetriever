package Utils.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import Logging.LogLevel;
import Logging.WPLogger;
import Utils.FIPWeb.FIPWebParser;

public class HTTPHelper {
    private static String USER_AGENT = "Mozilla/5.0";
    private static WPLogger logger = WPLogger.getInstance();
    
    // HTTP GET request
    public static String SendGet(String url) {
    	String result = "";

        try {
            URL               obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            logger.Log(HTTPHelper.class.getName(), logger.GetMethodName(), "Sending 'GET' request to URL: ["+url+"]", LogLevel.INFO);
            logger.Log(HTTPHelper.class.getName(), logger.GetMethodName(), "Response Code: ["+responseCode+"]", LogLevel.INFO);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String         inputLine;
            StringBuffer   response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            result = response.toString();
        } catch (Exception e) {}

        return result;
    }

    // HTTP POST request
    public static String SendPost(String url) {
        String result = "";

        try {
            URL               obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            // Send post request
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            // wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            logger.Log(HTTPHelper.class.getName(), logger.GetMethodName(), "Sending 'POST' request to URL: ["+url+"]", LogLevel.INFO);
            logger.Log(HTTPHelper.class.getName(), logger.GetMethodName(), "Response Code: ["+responseCode+"]", LogLevel.INFO);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String         inputLine;
            StringBuffer   response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            result = response.toString();
        } catch (Exception e) {}

        return result;
    }
}
