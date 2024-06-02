package com.shortCustomURL.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

import com.shortCustomURL.http.HttpResponse;





public class HttpResponseBuilder {

    private HttpResponse response;

    public HttpResponseBuilder(HttpResponse response) {
        this.response = response; 
    }

    public int getStatusCode(){
        return(response.getStatusCode());
    }
    public void setStatusCode(int statusCode){
        response.setStatusCode(statusCode);
    }

    public String getStatusMessage(){
        return(response.getStatusMessage());
    }
    public void setStatusMessage(String statusMessage){
        response.setStatusMessage(statusMessage);
    }

    public void addResponseHeader(String header, String headerValue){
        
        Hashtable<String, String> responseHeaders = response.getResponseHeaders();
        responseHeaders.put(header,headerValue);
    }

    public Hashtable<String, String> getResponseHeader(){
        return(response.getResponseHeaders());
    }

    public void addHTMLContent(String filePath) throws IOException{
        File f = new File(filePath);
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        int count=0;
        
        try (FileInputStream fis = new FileInputStream(f);) {
            
            int b;
            while ((b=fis.read())>=0) {
                ba.write(b);
                count++;
            }
        }
        byte[] tempMessageBody= ba.toByteArray();
        response.setMessageBody(tempMessageBody);

        addResponseHeader("Content-Type","text/html; charset=utf-8");
        addResponseHeader("Content-Length",String.valueOf(count));

    }

    public void addJSONContent(String json){

        byte[] tempMessageBody = json.getBytes(StandardCharsets.UTF_8);
        response.setMessageBody(tempMessageBody);

        addResponseHeader("Content-Type","application/json");
        addResponseHeader("Content-Length",String.valueOf(tempMessageBody.length));


    }


    
}
