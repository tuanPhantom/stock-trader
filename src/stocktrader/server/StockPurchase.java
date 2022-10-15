package stocktrader.server;

import stocktrader.common.NotPossibleException;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @Overview StockPurchase represents a record of a purchased stock
 * @attributes <pre>
 * theStock			Stock
 * quantity			int
 * purchasePrice	double
 * purchaseDate		Date
 * </pre>
 * @Object a typical Stock is :<t, q, p, d> where t is theStock, q is quantity, p is purchasePrice, d is purchaseDate,
 *         pd is purchaseDay
 * AF(c) = c:<t, q, p, d, pd>
 * @rep_invariant
 * 	t!=null &&
 * 	q>0 &&
 * 	p>=0d &&
 * 	d is after 1900/0/1 &&
 * 	pd>=1
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class StockPurchase implements Serializable {
	private Stock theStock;
	private int quantity;
	private double purchasePrice;
	private Date purchaseDate;
	private int purchaseDay;

	/**
	 * @effects <pre>
	 * if theStock, quantity, purchasePrice, purchaseDate are valid
	 * 	initialize this as c:<theStock, quantity, purchasePrice, purchaseDate>
	 * else
	 * 	throw NotPossibleException</pre>
	 */
	public StockPurchase(Stock theStock, int quantity, double purchasePrice, Date purchaseDate, int purchaseDay)
			throws NotPossibleException {
		if (!validateStock(theStock)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid theStock:'" + theStock + "'");
		}

		if (!validateQuantity(quantity)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid quantity:'" + quantity + "'");
		}

		if (!validatePurchasePrice(purchasePrice)) {
			throw new NotPossibleException(
					getClass().getSimpleName() + ".init: invalid purchasePrice:'" + purchasePrice + "'");
		}

		if (!validatePurchaseDate(purchaseDate)) {
			throw new NotPossibleException(
					getClass().getSimpleName() + ".init: invalid purchaseDate:'" + purchaseDate + "'");
		}

		if (!validatePurchaseDay(purchaseDay)) {
			throw new NotPossibleException(
					getClass().getSimpleName() + ".init: invalid purchaseDay:'" + purchaseDay + "'");
		}

		this.theStock = theStock;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.purchaseDate = Calendar.getInstance().getTime();
		this.purchaseDate.setTime(purchaseDate.getTime());
		this.purchaseDay = purchaseDay;
	}

	/**
	 * @effects <pre>
	 * if theStock is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateStock(Stock theStock) {
		return theStock != null;
	}

	/**
	 * @effects <pre>
	 * if quantity is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateQuantity(int quantity) {
		return quantity > 0;
	}

	/**
	 * @effects <pre>
	 * if purchasePrice is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validatePurchasePrice(double purchasePrice) {
		return purchasePrice >= 0d;
	}

	/**
	 * @effects <pre>
	 * if purchaseDate is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validatePurchaseDate(Date purchaseDate) {
		Calendar cal = Calendar.getInstance();
		if (purchaseDate != null) {
			cal.setTime(purchaseDate);
		}

		Calendar minCal = Calendar.getInstance();
		minCal.set(1900, 0, 1);
		return (purchaseDate != null && cal.getTime().after(minCal.getTime()));
	}

	/**
	 * @effects <pre>
	 * if purchaseDay is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validatePurchaseDay(int purchaseDay) {
		return purchaseDay >= 1;
	}

	/**
	 * @effects return theStock
	 */
	public Stock getTheStock() {
		return theStock;
	}

	/**
	 * @effects <pre>
	 * if theStock is valid
	 *   this.theStock = theStock
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setTheStock(Stock theStock) {
		if (validateStock(theStock)) {
			this.theStock = theStock;
		}
	}

	/**
	 * @effects return quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @effects <pre>
	 * if quantity is valid
	 *   this.quantity = quantity
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setQuantity(int quantity) {
		if (validateQuantity(quantity)) {
			this.quantity = quantity;
		}
	}

	/**
	 * @effects return purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @effects <pre>
	 * if purchasePrice is valid
	 *   this.purchasePrice = purchasePrice
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setPurchasePrice(double purchasePrice) {
		if (validatePurchasePrice(purchasePrice)) {
			this.purchasePrice = purchasePrice;
		}
	}

	/**
	 * @effects return purchaseDate
	 */
	public Date getPurchaseDate() {
		return (Date) purchaseDate.clone();
	}

	/**
	 * @effects <pre>
	 * if purchaseDate is valid
	 *   this.purchaseDate.set(purchaseDate.getTime)
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setPurchaseDate(Date purchaseDate) {
		if (validatePurchaseDate(purchaseDate)) {
			this.purchaseDate.setTime(purchaseDate.getTime());
		}
	}

	/**
	 * @effects return purchaseDay
	 */
	public int getPurchaseDay() {
		return purchaseDay;
	}

	/**
	 * @effects <pre>
	 * if purchaseDay is valid
	 *   this.purchaseDay = purchaseDay
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setPurchaseDay(int purchaseDay) {
		if (validatePurchaseDay(purchaseDay)) {
			this.purchaseDay = purchaseDay;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":<" + theStock + ", " + quantity + ", " + purchasePrice + ", "
				+ purchaseDate + ", " + purchaseDay + ">";
	}

	/**
	 * empty constructor
	 */
	private StockPurchase() {

	}

	/**
	 * @effects return a deep copy of this
	 */
	public StockPurchase clone() {
		StockPurchase s = new StockPurchase();
		s.theStock = theStock;
		s.quantity = quantity;
		s.purchasePrice = purchasePrice;
		s.purchaseDate.setTime(purchaseDate.getTime());
		return s;
	}
}
