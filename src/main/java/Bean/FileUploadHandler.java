import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
 import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import bean.SpeechtoTextConnector;
import javax.servlet.annotation.WebServlet;


 @WebServlet(name = "FileUploadHandler", urlPatterns = {"/FileUploadHandler"})
public class FileUploadHandler extends HttpServlet {
    
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
      	 SpeechtoTextConnector connector = new SpeechtoTextConnector();
         SpeechToText service = new SpeechToText();
         service.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
         
       if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        
                        InputStream in = item.getInputStream();
                        String PREFIX = "temporary";
                        String SUFFIX = ".wav";

                        File tempFile = File.createTempFile(PREFIX, SUFFIX);
                        FileOutputStream out = new FileOutputStream(tempFile);
                        IOUtils.copy(in, out);
        
                        File audio = tempFile;
                        
                        SpeechResults output = service.recognize(audio, "audio/wav");
                        String print = output.toString();
                        
                        request.setAttribute("message", print);     
    
                    }
                }
           
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }       
         
        }else{
            request.setAttribute("message",
                                 "Sorry this Servlet only handles file upload request");
        }
    
        request.getRequestDispatcher("/result.jsp").forward(request, response);
     
    }
  
}