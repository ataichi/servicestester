package Bean;

import java.util.Map;
import java.text.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TwitterConnector {

    private String pWord;
    private String uName;
	private String uRl;

    public TwitterConnector() {
        setConnection();
    }

    private void setConnection() {
        Map<String, String> env = System.getenv();

        if (env.containsKey("VCAP_SERVICES")) {

            try {
                JSONParser parser = new JSONParser();
                JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
                JSONObject service = null;

                for (Object key : vcap.keySet()) {
                    String keyStr = (String) key;
                    if (keyStr.toLowerCase().contains("twitterinsights")) {
                        service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
                        break;
                    }
                }

                if (service != null) {
                    JSONObject creds = (JSONObject) service.get("credentials");
                    String username = (String) creds.get("username");
                    String password = (String) creds.get("password");
                    String host = (String) creds.get("host");
                    Long port = (Long) creds.get("port");
                    String url = (String) creds.get("url");

                    this.uName = username;
                    this.pWord = password;
					this.uRl = url;
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public String getUsername() {
        return uName;
    }

    public String getPassword() {
        return pWord;

    }
	
    public String getUrl() {
        return uRl;

    }	
	

}
