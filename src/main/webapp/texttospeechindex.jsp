<%-- 
    Document   : index
    Created on : Feb 16, 2016, 8:59:06 AM
    Author     : jao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Text to Speech</title>
		
    </head>
    <body>
        <div>
            <h1>Text to Speech:</h1>
            <form action ="TexttoSpeechServlet" method="get">
                <input type="text" name="inputText">
                <input type="SUBMIT" value="Submit" />
            </form>	
        </div>
    </body>
</html>
