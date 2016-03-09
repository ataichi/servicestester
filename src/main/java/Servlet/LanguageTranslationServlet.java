package Servlet;

import Bean.CloudantClientClass;
import Bean.DashDBConnector;
import Bean.LanguageTranslatorConnector;
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

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

@WebServlet(name = "LanguageTranslationServlet", urlPatterns = {"/LanguageTranslationServlet"})
  
public class LanguageTranslationServlet extends HttpServlet {
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
			
			LanguageTranslatorConnector connector = new LanguageTranslatorConnector();
			
			LanguageTranslation languageTranslation = new LanguageTranslation();
				
			languageTranslation.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
            TranslationResult translated = languageTranslation.translate(request.getParameter("inputText"), "en", "es");
            //TranslationResult translated = languageTranslation.translate("hello", "en", "es");
			String translatedText = translated.toString();
			
			request.setAttribute("outputText",translatedText);
			try{
				CloudantClientClass db = new CloudantClientClass();
				
				int addStat;
				addStat = db.addEntry(translatedText);
				
				DashDBConnector dashdb = new DashDBConnector();
				
				
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(translatedText);
				String product = (String) jsonObject.get("translation");
				
								
				dashdb.addWords(product);
				
				
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
			
			/*
			String test = connector.getUsername();
			request.setAttribute("outputText",test);
			*/
			
			response.setContentType("text/html");
			response.setStatus(200);
			request.getRequestDispatcher("langtransindex.jsp").forward(request,response);
        //processRequest(request, response);
    }
    
    public String test(){
    	LanguageTranslatorConnector connector = new LanguageTranslatorConnector();
    	LanguageTranslation languageTranslation = new LanguageTranslation();
    	languageTranslation.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
    	TranslationResult translated = languageTranslation.translate("hello", "en", "es");
    	String translatedText = translated.toString();
    	
    	return translatedText;
    }
	
	

}
