package stocktrader.common;

/**
 * @Overview A class represents an exception means that your session is out of date
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class OutOfDateException  extends Exception{
    public OutOfDateException() {
    }

    public OutOfDateException(String message) {
        super(message);
    }
}
