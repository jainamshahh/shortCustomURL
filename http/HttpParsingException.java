public class HttpParsingException extends Exception {
    final int errorCode;
    final String errorMessage;

    HttpParsingException(int errorCode,String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    
}
