import java.util.Hashtable;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private Hashtable<String, String> requestHeaders;
    private StringBuffer messageBody;
    private final String httpVersion = "HTTP/1.1";
    
    
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

    public Hashtable<String, String> getRequestHeaders() {
        return requestHeaders;
    }
    public void setRequestHeaders(Hashtable<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public StringBuffer getMessageBody() {
        return messageBody;
    }
    public void setMessageBody(StringBuffer messageBody) {
        this.messageBody = messageBody;
    }
    
    public String getHttpVersion() {
        return httpVersion;
    }

    
}
