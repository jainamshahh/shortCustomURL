package com.shortCustomURL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.shortCustomURL.controller.URLController;
import com.shortCustomURL.http.HttpParsingException;
import com.shortCustomURL.http.HttpRequest;
import com.shortCustomURL.http.HttpRequestParser;
import com.shortCustomURL.http.HttpResponse;
import com.shortCustomURL.views.HttpResponseBuilder;


public class RouteRequest {
    public void processRequest(Socket connection) throws IOException{
        
        InputStream input;
        OutputStream output;
        HttpRequestParser parser =new HttpRequestParser();
        HttpRequest request;
        
        HttpResponse response =new HttpResponse();
	    HttpResponseBuilder rb = new HttpResponseBuilder(response);


        try {
            input = connection.getInputStream();
            output = connection.getOutputStream();
        } catch (Exception e) {
            
            connection.close();
            return;
        }
        
        try {
            request = parser.parseHttpRequest(input);
        } catch (HttpParsingException e) {
            rb.setStatusCode(400);
	        rb.setStatusMessage("Bad Request");
            output.write(response.getBytes());
            connection.close();
            return;
        } catch(Exception e){
            
            rb.setStatusCode(500);
	        rb.setStatusMessage("Internal Server Error");
            output.write(response.getBytes());
            connection.close();
            return;
        }

        response=routeRequest(request);
        output.write(response.getBytes());
        connection.close();



    }

    private HttpResponse routeRequest(HttpRequest request) {

        String httpMethod = request.getHttpMethod();
        URLController controller = new URLController();
        if (httpMethod.equals("GET")) {
            return(routeGETRequest(request,controller));
            
        } else if (httpMethod.equals("POST")) {
            return(routePOSTRequest(request,controller));
        } else {
            //METHOD NOT IMPLEMENTED
            HttpResponse response =new HttpResponse();
	        HttpResponseBuilder rb = new HttpResponseBuilder(response);
            rb.setStatusCode(501);
	        rb.setStatusMessage("Not Implemented");
            return(response);

        }
        
    
    }

    private HttpResponse routeGETRequest(HttpRequest request, URLController controller) {
        String requestURL = request.getRequestTarget();
        if(requestURL.length()==1 && requestURL.equals("/")){
            //index.html
            return(controller.getIndexFile());
        }
        else if(requestURL.length()>1 && requestURL.charAt(0)=='/'){
            //redirect
            return(controller.redirectURL(request));
        }
        else{
            HttpResponse response =new HttpResponse();
	        HttpResponseBuilder rb = new HttpResponseBuilder(response);
            rb.setStatusCode(400);
	        rb.setStatusMessage("Bad Request");
            return(response);
        }
        
    }

    private HttpResponse routePOSTRequest(HttpRequest request, URLController controller) {
        
        String requestURL = request.getRequestTarget();
        if(requestURL.equals("/shorten")){
            //validate and add entry
            return(controller.shortenURL(request));
        }
        else{

            HttpResponse response =new HttpResponse();
	        HttpResponseBuilder rb = new HttpResponseBuilder(response);
            rb.setStatusCode(400);
	        rb.setStatusMessage("Bad Request");
            return(response);

        }
        
        
    }
}
