package Bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author nievabri
 */
public class PostgreSQLClient {

    public PostgreSQLClient() {
        try {
            createUsers();
			createTable();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

	public Account getResults(String email, String password) throws Exception {
        String selectquery = "SELECT * FROM account WHERE email = '" + email + "' and password = '" + password + "';";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(selectquery);
            rs = statement.executeQuery();

            Account account = new Account();
			account.setTheresareturnedvalue(0);
            if ( rs.next() ) {
                account.setFullname(rs.getString(1));
				account.setEmail(rs.getString(2));
				account.setAccountno(rs.getString(3));
				account.setPassword(rs.getString(4));
				account.setCurrency(rs.getString(5));
				account.setCurrentbalance(rs.getString(6));
				account.setAvailablebalance(rs.getString(7));
				account.setTheresareturnedvalue(1);
            }
            return account;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean doesUserExist(String email, String password) {
        String selectquery = "SELECT * FROM account WHERE email = '" + email + "' and password = '" + password + "';";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		try
		{
            ps = connection.prepareStatement(selectquery);
           
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            connection.close();
        } catch (Exception ex) {
            
        }
        return false;
    }

    public static Connection getConnection() throws Exception {
        Map<String, String> env = System.getenv();

        if (env.containsKey("VCAP_SERVICES")) {
            JSONParser parser = new JSONParser();
            JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
            JSONObject service = null;

            for (Object key : vcap.keySet()) {
                String keyStr = (String) key;
                if (keyStr.toLowerCase().contains("postgresql")) {
                    service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
                    break;
                }
            }

            if (service != null) {
                JSONObject creds = (JSONObject) service.get("credentials");
                String name = (String) creds.get("name");
                String host = (String) creds.get("host");
                Long port = (Long) creds.get("port");
                String user = (String) creds.get("user");
                String password = (String) creds.get("password");

                String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;

                return DriverManager.getConnection(url, user, password);
            }
        }
        throw new Exception("No PostgreSQL binded with your app.");
    }

    private void createUsers() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        String createquery = "CREATE TABLE IF NOT EXISTS account "
                + "(fullname varchar(45) NOT NULL, "
                + "email varchar(45) NOT NULL,"
                + "accountno varchar(45) NOT NULL PRIMARY KEY,"
                + "password varchar(45) NOT NULL, "
                + "currency varchar(45) NOT NULL, "
                + "currentbalance varchar(45) NOT NULL, "
                + "availablebalance varchar(45) NOT NULL"
                + ");";

        String insertquery = "INSERT INTO account "
                + "(fullname, email, accountno, password, currency, currentbalance, availablebalance) "
                + "VALUES ('Patricia Hera A. Nieva', 'patricia_nieva@dlsu.edu.ph', '0091-1883-3689', 'blazemeter', 'Peso', '10,500.00', '999.00'),"
                + "('John Even Doe', 'john_doe@gmail.com', '0081-4563-3619', 'johnjojon', 'Dollar', '1,000.00', '34,000.00'),"
				+ "('Doe A. Dear', 'afemaledear@gmail.com', '0041-1553-7822', 'doenut', 'Yen', '15,500.00', '100,909.00')"
				+ ";";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(createquery);
            statement.executeUpdate();
            statement = connection.prepareStatement(insertquery);
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
	
	private void createTable() throws Exception {
		String sql = "CREATE TABLE IF NOT EXISTS words" +
						"(translatedword varchar(145) NOT NULL, "
						+ "wordcount varchar(45) NOT NULL);" ;
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
	
	public int addWords(List<String> words) throws Exception {
		String sql = "INSERT INTO words (translatedword, wordcount) VALUES (?, ?)";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			
			for (String s : words) {
				statement.setString(1, s);
				statement.addBatch();
			}
			int[] rows = statement.executeBatch();
			connection.commit();
			
			return rows.length;
		} catch (SQLException e) {
			SQLException next = e.getNextException();
			
			if (next != null) {
				throw next;
			}
			
			throw e;
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
