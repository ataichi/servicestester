package Servlet;

import Bean.TwitterConnector;
import java.io.*;
import java.net.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Kurt's Lenovo
 */
@WebServlet(name = "twitterSearchServlet", urlPatterns = {"/twitterSearchServlet"})
public class twitterSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TwitterConnector connector = new TwitterConnector();
		
		StringBuilder sb = new StringBuilder();
		
		String input_query = (String) request.getParameter("tQuery");
		String input_size = (String) request.getParameter("tSize");
		
		String urlstring = connector.getUrl() + "/api/v1/messages/search?q=" + URLEncoder.encode(input_query, "UTF-8") + "&size=" + input_size;
		
		String userandpass = connector.getUsername() + ":" + connector.getPassword();
		
		String basicAuthorization = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userandpass.getBytes());
		
		
		 URL finalurl = new URL(urlstring);
         URLConnection urlconnection = finalurl.openConnection();
 
         urlconnection.setRequestProperty ("Authorization", basicAuthorization);
 
         InputStream inputstream = urlconnection.getInputStream();
         InputStreamReader isreader = new InputStreamReader(inputstream);
         BufferedReader in = new BufferedReader(isreader);
 
         String inputline;
 
         while ((inputline = in.readLine()) != null)
         {
             sb.append(inputline);
         }
         in.close();
		
				
		request.setAttribute("finalsearch",sb.toString());

        response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }

}
