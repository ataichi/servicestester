
package Servlet;

import Bean.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;

import java.util.List;
import java.lang.reflect.Type;
import java.awt.Component;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.util.ResponseUtil;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/TranslateTexttospeechObjectStorage"})
public class TranslateTexttospeechObjectStorage extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
			LanguageTranslatorConnector connector = new LanguageTranslatorConnector();
			LanguageTranslation languageTranslation = new LanguageTranslation();
			PrintWriter writer = response.getWriter();
			String input = request.getParameter("inputText");
			languageTranslation.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
            TranslationResult translated = languageTranslation.translate(request.getParameter("inputText"), "en", "es");
			String translatedText = translated.toString();
			
			ObjectStorageConnector connect = new ObjectStorageConnector();
			connect.createContainer("container1");
			String filename = null;
            Payload upfile = null;

			TexttoSpeechConnector connector = new TexttoSpeechConnector();      
  			TextToSpeech service = new TextToSpeech();
  			service.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
        
        	String text = translatedText;
        	String format = "audio/wav";
			InputStream speech = service.synthesize(text, format);
            //^needed to create the wav file. no need to download so removed output stream buf
            upfile = Payloads.create(speech);
			filename = "gustokoetoname.wav";
            //payloads are used to upload 
            connect.uploadFile("container1", filename, upfile);
                   
               
			response.sendRedirect("texttospeechstoragehome.jsp");
        }catch(Exception e){
			e.printStackTrace(System.err);
		}
    }

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
