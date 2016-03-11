package Servlet;

import Bean.LanguageTranslatorConnector;
import Bean.*;
import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "LanguageTranslationServlet", urlPatterns = {"/LanguageTranslationServlet"})
  
public class LanguageTranslationServlet extends HttpServlet {
	
    @Override
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try{
			String input;
		
			LanguageTranslatorConnector connector = new LanguageTranslatorConnector();
			LanguageTranslation languageTranslation = new LanguageTranslation();
			
			PrintWriter writer = response.getWriter();
			DBHelper db = new DBHelper(writer);
			
			input = request.getParameter("inputText");
			
			languageTranslation.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
            TranslationResult translated = languageTranslation.translate(request.getParameter("inputText"), "en", "es");
            //TranslationResult translated = languageTranslation.translate("hello", "en", "es");
			String translatedText = translated.toString();
			
			request.setAttribute("outputText",translatedText);
			
			/*
			JSONObject obj = new JSONObject(translatedText);
			final JSONArray trans = obj.getJSONArray("translations");
			final int n = trans.length();
			for (int i = 0; i < n; ++i) {
				final JSONObject words = trans.getJSONObject(i);
				db.insertInto(input, words.getString("translation"));
				
			}*/
			
			String langtrans = translatedText;
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(langtrans);
            
            JSONObject jsonObject = (JSONObject) obj;
            String character_count = jsonObject.get("character_count").toString();
      
            JSONArray jsonArray = (JSONArray) jsonObject.get("translations");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            String translation = jsonObject1.get("translation").toString();
            String word_count = jsonObject.get("word_count").toString();
            db.insertInto(input, translation);
			
			response.setContentType("text/html");
			response.setStatus(200);
			request.getRequestDispatcher("langtransindex.jsp").forward(request,response);
        //processRequest(request, response);
		}
		
		catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
			e.printStackTrace(System.err);
		}
    }
	
	

}
