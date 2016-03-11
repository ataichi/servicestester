package Servlet;

import com.ibm.db2.jcc.DB2SimpleDataSource;
import java.io.PrintWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.parser.ParseException;

public class DBHelper {

    
    private static final long serialVersionUID = 1L;
    // set defaults
    private String databaseHost = "localhost";
    private long port = 50000;
    private String databaseName = "mydb";
    private String user = "myuser";
    private String password = "mypass";
    private String url = "myurl";
    private Connection con;
    PrintWriter writer;
    private final boolean statusVCAP;

    public DBHelper(PrintWriter writer) throws ParseException {
        this.writer = writer;
        this.statusVCAP = processVCAP();
        getConnection();
        createTable();

        // createSchema();
    }

    private boolean processVCAP() throws ParseException {
        // VCAP_SERVICES is a system environment variable
        // Parse it to obtain the for DB2 connection info
        Map<String, String> env = System.getenv();
        if (env.containsKey("VCAP_SERVICES")) {
            // parse the VCAP JSON structure
            JSONParser parser = new JSONParser();
            JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
            JSONObject service = null;
            writer.println("Searching through VCAP keys");
            for (Object key : vcap.keySet()) {
                String keyStr = (String) key;
                if (keyStr.toLowerCase().contains("sqldb")) {
                    service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
                    break;
                }
            }
            if (service != null) {
                JSONObject creds = (JSONObject) service.get("credentials");
                databaseHost = (String) creds.get("host");
                databaseName = (String) creds.get("db");
                port = (long) creds.get("port");
                user = (String) creds.get("username");
                password = (String) creds.get("password");
                url = (String) creds.get("jdbcurl");
            } else {
                writer.println("VCAP_SERVICES is null");
                return false;
            }
            writer.println("database host: " + databaseHost);
            writer.println("database port: " + port);
            writer.println("database name: " + databaseName);
            writer.println("username: " + user);
            writer.println("password: " + password);
            writer.println("url: " + url);
            return true;
        }
        return false;
    }

    private void getConnection() {
        // process the VCAP env variable and set all the global connection parameters
        if (statusVCAP) {
            // Connect to the Database
            try {
                writer.println();
                writer.println("Connecting to the database");
                DB2SimpleDataSource dataSource = new DB2SimpleDataSource();
                dataSource.setServerName(databaseHost);
                dataSource.setPortNumber((int) port);
                dataSource.setDatabaseName(databaseName);
                dataSource.setUser(user);
                dataSource.setPassword(password);
                dataSource.setDriverType(4);
                writer.println();
                this.con = dataSource.getConnection();
				System.out.println("Con success!");
                if (con == null) {
                    System.out.println("con is null");
                    writer.println("con is null");
                }
            } catch (SQLException e) {
                writer.println("Error connecting to database");
                writer.println("SQL Exception: " + e);
                this.con = null;
            }
        }
    }
	
    // Try out some dynamic SQL Statements
    PreparedStatement stmt = null;
    String tableName = "Translations";
    String sqlStatement = "";

    // create a unique table name to make sure we deal with our own table
    // If another version of this sample app binds to the same database, 
    // this gives us some level of isolation
    public void createTable() {
        // create a table
        if (con != null) {
            try {
                // Create the CREATE TABLE SQL statement and execute it
                sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName
                        + "(eng VARCHAR(100), esp VARCHAR(100))";
				stmt = con.prepareStatement(sqlStatement);
                stmt.executeUpdate();
				System.out.println("Table Created.");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }

    public void insertInto(String en, String es) {
        if (con != null) {
            try {
                sqlStatement = "INSERT INTO " + tableName
                        + "(eng, esp) VALUES (?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sqlStatement);
                preparedStatement.setString(1, en);
                preparedStatement.setString(2, es);
                writer.println("Executing: " + sqlStatement);
                preparedStatement.executeUpdate();
				System.out.println("Data Inserted.");
            } catch (SQLException e) {
                writer.println("Error " + e);
            }
        }
    }
}