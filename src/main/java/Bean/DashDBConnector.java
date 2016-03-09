package Bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.text.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DashDBConnector {

   
    public DashDBConnector() {
		try{
			getConnection();
		}catch (Exception e){
			e.printStackTrace(System.err);
		}
    }

    public static Connection getConnection() throws Exception {
        Map<String, String> env = System.getenv();

        if (env.containsKey("VCAP_SERVICES")) {
			JSONParser parser = new JSONParser();
            JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
            JSONObject service = null;

            for (Object key : vcap.keySet()) {
                String keyStr = (String) key;
                if (keyStr.toLowerCase().contains("dashDB")) {
                    service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
                    break;
                }
            }

            if (service != null) {
                    JSONObject creds = (JSONObject) service.get("credentials");
                    String username = (String) creds.get("username");
					String password = (String) creds.get("password");
					String port = (String) creds.get("port");
					String db = (String) creds.get("db");
					String host = (String) creds.get("host");
					String jdbcurl = "jdbc:db2://" + host + ":" + port + "/" + db;
                   
					return DriverManager.getConnection(jdbcurl, username, password);
            }
            
        }
		throw new Exception("No PostgreSQL binded with your app.");
    }

	
	
 
}