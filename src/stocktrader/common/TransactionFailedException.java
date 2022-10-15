package stocktrader.common;

/**
 * @Overview A class represents an exception means that user failed to purchase or sell
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class TransactionFailedException extends Exception{
    public TransactionFailedException() {
    }

    public TransactionFailedException(String message) {
        super(message);
    }
}
