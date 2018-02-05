package Utils;

public class Utils {

    public static String GetWebPage(String url) {
        String result = "";
        try {
            result = HTTPHelper.SendPost(url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
