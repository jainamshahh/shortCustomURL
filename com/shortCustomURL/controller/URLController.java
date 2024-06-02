package com.shortCustomURL.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import com.shortCustomURL.http.HttpRequest;
import com.shortCustomURL.http.HttpResponse;
import com.shortCustomURL.model.DbConnectionException;
import com.shortCustomURL.model.DbFunctions;
import com.shortCustomURL.model.URL;
import com.shortCustomURL.views.HttpResponseBuilder;


public class URLController {
    public HttpResponse getIndexFile(){

        //return index file

        HttpResponse response =new HttpResponse();
	    HttpResponseBuilder rb = new HttpResponseBuilder(response);

        rb.setStatusCode(200);
	    rb.setStatusMessage("OK");
	    try {
            rb.addHTMLContent(".\\\\com\\\\shortCustomURL\\\\resources\\\\index.html");
        } catch (IOException e) {
            // internal server error
            
            rb.setStatusCode(500);
	        rb.setStatusMessage("Internal Server Error");
            return(response);

        }

        return(response);


    }
    public HttpResponse redirectURL(HttpRequest request){

        String shortURL = (request.getRequestTarget()).substring(1);
        URL url = new URL();
        url.setShortURL(shortURL);

        HttpResponse response =new HttpResponse();
	    HttpResponseBuilder rb = new HttpResponseBuilder(response);

        DbFunctions db = new DbFunctions();
        String dbname = "";
        String user = "";
        String password = "";
        try (Connection dbcon = db.connect_to_db(dbname, user, password)) {
            db.getLongURL(dbcon, url);
            if(url.getLongURL()==null){
                    //not found
                    rb.setStatusCode(404);
	                rb.setStatusMessage("Not Found");

                    return(response);
            }
            else{
                    //found
                    rb.setStatusCode(302);
	                rb.setStatusMessage("Found");
                    rb.addResponseHeader("Location", url.getLongURL());

                    return(response);

            }
        } catch (DbConnectionException | SQLException e) {
            // internal server error
            rb.setStatusCode(500);
	        rb.setStatusMessage("Internal Server Error");
            return(response);
        }
        

    }
    public HttpResponse shortenURL(HttpRequest request){

        Hashtable<String, String> queryParameter = request.getQueryParameters();
        URL url = new URL();
        String shortURL = queryParameter.get("shortURL");
        String longURL = queryParameter.get("longURL");
        longURL = URLDecoder.decode(longURL, StandardCharsets.UTF_8);
        url.setShortURL(shortURL);
        url.setLongURL(longURL);

        HttpResponse response =new HttpResponse();
	    HttpResponseBuilder rb = new HttpResponseBuilder(response);

        DbFunctions db = new DbFunctions();
        String dbname = "";
        String user = "";
        String password = "";
        try (Connection dbcon = db.connect_to_db(dbname, user, password)) {
            
            db.getLongURL(dbcon, url);
            if(!db.checkURLEntry(dbcon,url)){
                db.addNewURLEntry(dbcon,url);
                String jsonResponse = "{\"success\":true, \"shortURL\":\"http://localhost:8080/"+url.getShortURL()+"\"}";

                rb.setStatusCode(200);
	            rb.setStatusMessage("OK");
                rb.addJSONContent(jsonResponse);

                return(response);
                
            }
            else{
                //entry already exists
                
                String jsonResponse = "{\"success\":false, \"message\":\"shortURL already taken\"}";

                rb.setStatusCode(200);
	            rb.setStatusMessage("OK");
                rb.addJSONContent(jsonResponse);

                return(response);

            }
        } catch (DbConnectionException | SQLException e) {
            //internal server error
            e.printStackTrace();
            rb.setStatusCode(500);
	        rb.setStatusMessage("Internal Server Error");
            return(response);

        }


    }
}
