package com.shortCustomURL.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

public class HttpRequestParser{
	
	private static final int SP = 32;
    private static final int CR = 13;
    private static final int LF = 10;

	public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException,HttpParsingException {
        	
		InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		
		HttpRequest request = new HttpRequest();

		parseRequestLine(reader,request);
		parseRequestHeaders(reader, request);

		Hashtable<String, String> requestHeaders = request.getRequestHeaders();
		if(requestHeaders.contains("Content-Length")){
			int length = Integer.valueOf((requestHeaders.get("Content-Length")));
			parseRequestBody(reader,request,length);
		}
		
		
		return(request);
		
		
	
	}

	private void parseRequestLine(InputStreamReader reader,HttpRequest request) throws IOException,HttpParsingException{
				
		StringBuffer dataBuffer = new StringBuffer();
		boolean methodParsed = false;
		boolean requestTargetParsed = false;
		int b;
		while((b=reader.read()) >= 0){
			if(b==CR){
				b = reader.read();
				if(b==LF){
					if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(400, "Bad Request");
                    }
					else{
						request.setHttpVersion(dataBuffer.toString());
						return;
					}
				}
				else{
					throw new HttpParsingException(400, "Bad Request");
				}

			}
			else if(b==SP){
				if(!methodParsed){
					request.setHttpMethod(dataBuffer.toString());
					methodParsed = true;
				}
				else if(!requestTargetParsed){
					//request.setRequestTarget(dataBuffer.toString());
					parseRequestTarget(dataBuffer.toString(),request);
					requestTargetParsed = true;
				}
				else{
					throw new HttpParsingException(400, "Bad Request");
				}
				dataBuffer.delete(0, dataBuffer.length());
			}
			else{
				dataBuffer.append((char) b);
			}		

		}
		throw new HttpParsingException(400, "Bad Request");
		

	}

	private void parseRequestTarget(String requestTarget,HttpRequest request) throws HttpParsingException{

		if (requestTarget.contains("?")) {
			String[] queryTargetParts = requestTarget.split("\\?");
			request.setRequestTarget(queryTargetParts[0]);
			parseQueryParams(queryTargetParts[1],request);
			
		} else {
			
			request.setRequestTarget(requestTarget);
		}


	}

	private void parseQueryParams(String params, HttpRequest request) throws HttpParsingException {

		Hashtable<String, String> queryParameters = new Hashtable<String,String>();
		String[] queryParamsPart = params.split("&");
		for(String param : queryParamsPart ){
			String[] paramPart = param.split("=");
			if(paramPart.length==2){
				queryParameters.put(paramPart[0], paramPart[1]);
			}
			else{
				throw new HttpParsingException(400, "Bad Request");
			}
		}
		request.setQueryParameters(queryParameters);
	
	}

	private void parseRequestHeaders(InputStreamReader reader,HttpRequest request) throws IOException,HttpParsingException{
		StringBuffer dataBuffer = new StringBuffer();
		boolean headerKeyParsed = false;
		boolean headerLineParsed = true;
		String headerKey=null, headerValue;
		Hashtable<String, String> requestHeaders = new Hashtable<String,String>();

		int b;
		while((b=reader.read()) >= 0){
			if(b==CR){
				b = reader.read();
				if(b==LF){
					if (headerLineParsed) {
						request.setRequestHeaders(requestHeaders);
						return;
					}
					else{
						if(headerKeyParsed){
							headerValue = (dataBuffer.toString()).trim();
							requestHeaders.put(headerKey.toString(), headerValue.toString());
							dataBuffer.delete(0, dataBuffer.length());
							headerLineParsed = true;
							headerKeyParsed = false;
						}
						else{
							throw new HttpParsingException(400, "Bad Request");
						}
					}
				}
				else{
					throw new HttpParsingException(400, "Bad Request");
				}
			}
			else if(b==':'){
				if(!headerKeyParsed){
					headerKey = dataBuffer.toString();
					dataBuffer.delete(0, dataBuffer.length());
					if(headerKey.length()!=0){
						headerKeyParsed = true;
					}
					else{
						throw new HttpParsingException(400, "Bad Request");
					}
				}
				else{
					dataBuffer.append((char) b);
				}
			}
			else{
				dataBuffer.append((char) b);
				if(headerLineParsed){
					headerLineParsed=false;
				}
				
			}

		}
		throw new HttpParsingException(400, "Bad Request");


	}

	private void parseRequestBody(InputStreamReader reader,HttpRequest request,int length) throws IOException{

		// will implment in future as not needed currently

	}


}