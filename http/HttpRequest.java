import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class HttpRequest{

	public static final Set<String> httpMethods = new HashSet<>(Arrays.asList("GET","HEAD","POST","PUT","DELETE","CONNECT","OPTIONS","TRACE","PATCH"));
	public static final Set<String> httpVersions = new HashSet<>(Arrays.asList("HTTP/1.1","HTTP/2","HTTP/3"));


	private String httpMethod;
	private String requestTarget;
	private String httpVersion;
	private Hashtable<String, String> requestHeaders;
    private StringBuffer messageBody;

	public HttpRequest() {
        	//requestHeaders = new Hashtable<String, String>();
        	//messageBody = new StringBuffer();
    	}

	public String getHttpMethod(){
		return(httpMethod);
	}

	public String getRequestTarget(){
		return(requestTarget);
	}

	public String getHttpVersion(){
		return(httpVersion);
	}

	public Hashtable<String, String> getRequestHeaders(){
		return(requestHeaders);
	}

	public StringBuffer getmessageBody(){
		return(messageBody);
	}

	public void setHttpMethod(String httpMethod) throws HttpParsingException{
		if(httpMethods.contains(httpMethod)){
			this.httpMethod = httpMethod;
		}
		else{
			throw new HttpParsingException(400, "Bad Request");
		}
		
	}
	
	public void setRequestTarget(String requestTarget) throws HttpParsingException{
		if (requestTarget == null || requestTarget.length()==0 || requestTarget.charAt(0)!='/') {
            throw new HttpParsingException(400, "Bad Request");
        }
		else{
			this.requestTarget = requestTarget;
		}
	}
	
	public void setHttpVersion(String httpVersion) throws HttpParsingException{
		if(httpVersions.contains(httpVersion)){	
			this.httpVersion = httpVersion;
		}
		else{
			throw new HttpParsingException(400, "Bad Request");
		}
	}

	public void setRequestHeaders(Hashtable<String, String> requestHeaders){
		this.requestHeaders = requestHeaders;
	}

	public void setMessageBody(StringBuffer messageBody){
		this.messageBody = messageBody;
	}	

}