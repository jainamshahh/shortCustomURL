package com.shortCustomURL;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class URLHttpServer {
    public static void main(String[] args)  throws IOException{
        
        int port = 8080;
        int backlog = 50;
        InetAddress address =InetAddress.getByName("localhost");
	    ServerSocket ss=new ServerSocket(port,backlog,address);
        RouteRequest rr = new RouteRequest();
        System.out.println("Server is listening");

        while (true) {
            try {
                Socket connection = ss.accept();
                rr.processRequest(connection);
                
            } catch (Exception e) {
                ss.close();
            }
            
            
        }

    }
    
}
