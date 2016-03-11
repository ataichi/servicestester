package Bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;
import org.cloudfoundry.runtime.env.*;
import java.io.*;
import org.json.*;

public class DashDBConnector {

   
    public DashDBConnector() {
		try{
			getConnection();
			createTable();
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
		throw new Exception("No DashDB service binded with your app in bluemix.");
    }

	
	private void createTable() throws Exception {
		String sql = "CREATE TABLE IF NOT EXISTS words" +
						"(translatedword varchar(145) NOT NULL);" ;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
		} finally {			
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	public void addWords(String words) throws Exception {
		String sql = "INSERT INTO words (translatedword) VALUES ('"+ 
		words+"');";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
           
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
		
	}


}