package stocktrader.common;

/**
 * @Overview A class represents an exception caused by not logging in
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class AccessDeniedException extends Exception{
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
