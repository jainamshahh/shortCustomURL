package com.shortCustomURL.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private Hashtable<String, String> responseHeaders;
    private byte[] messageBody;
    private final String httpVersion = "HTTP/1.1";
    
    
    public HttpResponse() {
        responseHeaders = new Hashtable<String, String>();
        messageBody = new byte[0];
    }

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Hashtable<String, String> getResponseHeaders() {
        return responseHeaders;
    }
    public void setResponseHeaders(Hashtable<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public byte[] getMessageBody() {
        return messageBody;
    }
    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
    
    public String getHttpVersion() {
        return httpVersion;
    }

    public byte[] getBytes(){
        byte[] byteResponse;
        StringBuilder sb = new StringBuilder();

        sb.append(getHttpVersion()+" ");
        sb.append(getStatusCode()+" ");
        sb.append(getStatusMessage()+"\r\n");

        Set<String> headers = responseHeaders.keySet();
        for(String header : headers){
            sb.append(header+": "+responseHeaders.get(header)+"\r\n");
        }
        sb.append("\r\n");

        byte[] temp = sb.toString().getBytes(StandardCharsets.US_ASCII);

        int lenA = temp.length;
        int lenB = messageBody.length;
        byteResponse = Arrays.copyOf(temp, lenA + lenB);
        System.arraycopy(messageBody, 0, byteResponse, lenA, lenB);

        return(byteResponse);

    }

    
}
