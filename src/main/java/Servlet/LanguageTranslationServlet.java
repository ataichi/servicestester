package Servlet;

import Bean.CloudantClientClass;
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
			
			private CloudantClientClass db = new CloudantClientClass();
			String JSONString = translatedText;
			int addStat;
			addStat = db.addEntry(JSONString);
			
			
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
