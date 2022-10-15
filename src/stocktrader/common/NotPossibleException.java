package stocktrader.common;

/**
 * @Overview A class represents an exception caused by invalid arguments when creating a new object
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class NotPossibleException extends Exception{
    public NotPossibleException() {
    }

    public NotPossibleException(String message) {
        super(message);
    }
}
