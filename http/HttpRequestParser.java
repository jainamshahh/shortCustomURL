import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class HttpRequestParser{
	
	private static final int SP = 32;
    private static final int CR = 13;
    private static final int LF = 10;

	public void parseHttpRequest(InputStream inputStream) throws IOException,HttpParsingException {
        	
		InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
		
		HttpRequest request = new HttpRequest();

		parseRequestLine(reader,request);
		System.out.println(request.getHttpMethod());
		System.out.println(request.getRequestTarget());
		System.out.println(request.getHttpVersion());
		
		
	
	}
	private void parseRequestLine(InputStreamReader reader,HttpRequest request) throws IOException,HttpParsingException{
				
		StringBuilder dataBuffer = new StringBuilder();
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
					request.setRequestTarget(dataBuffer.toString());
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
		

	}
}