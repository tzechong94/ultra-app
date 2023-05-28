package ultra.com.server.exception;

public class UltraException extends RuntimeException {
    public UltraException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public UltraException(String exMessage) {
        super(exMessage);
    }

}
