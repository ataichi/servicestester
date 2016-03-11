package Servlet;

import Bean.LanguageTranslatorConnector;
import Bean.DBHelper;
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
import org.json.*;

@WebServlet(name = "LanguageTranslationServlet2", urlPatterns = {"/LanguageTranslationServlet2"})
  
public class LanguageTranslationServlet2 extends HttpServlet {
	
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
			String test = connector.getUsername();
			request.setAttribute("outputText",test);
			*/
			//db.addEntry(translatedText);
			
			JSONObject obj = new JSONObject(translatedText);
			final JSONArray trans = obj.getJSONArray("translations");
			final int n = trans.length();
			for (int i = 0; i < n; ++i) {
				final JSONObject words = trans.getJSONObject(i);
				db.insertInto(input, words.getString("translation"));
				
			}
			
			response.setContentType("text/html");
			response.setStatus(200);
			request.getRequestDispatcher("index.jsp").forward(request,response);
        //processRequest(request, response);
		}
		
		catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
			e.printStackTrace(System.err);
		}
    }
	
	

}
