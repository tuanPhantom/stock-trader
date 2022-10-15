package stocktrader.server;

import stocktrader.common.NotPossibleException;

import java.io.Serializable;

/**
 * @Overview Stock represents the value of the shares in a company that have been sold
 * @attributes <pre>
 * id				String
 * companyName		String
 * currentPrice		double
 * quantity			int
 *
 * </pre>
 * @Object a typical Stock is :<i, c, p, q> where i is id, c is companyName, p is currentPrice, q is availableQuantity
 * AF(c) = :<i, c, p, q>
 * @rep_invariant
 * 	i!=null && 3<=i.length<=6 &&
 * 	c!=null && 0<c.length<=20 &&
 * 	p>=0d && q>=0
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class Stock implements Serializable {
	private String id;
	private String companyName;
	private double currentPrice;
	private int availableQuantity;

	/**
	 * @effects <pre>
	 * if id, companyName, currentPrice, availableQuantity are valid
	 * 	initialize this as c:<id, companyName, currentPrice, availableQuantity>
	 * else
	 * 	throw NotPossibleException</pre>
	 */
	public Stock(String id, String companyName, double currentPrice, int availableQuantity) throws NotPossibleException {
		if (!validateID(id)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid id:'" + id + "'");
		}

		if (!validateCompanyName(companyName)) {
			throw new NotPossibleException(
					getClass().getSimpleName() + ".init: invalid companyName:'" + companyName + "'");
		}

		if (!validateCurrentPrice(currentPrice)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid currentPrice:'" + currentPrice + "'");
		}

		if (!validateAvailableQuantity(availableQuantity)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid availableQuantity:'" + availableQuantity + "'");
		}

		this.id = id;
		this.companyName = companyName;
		this.currentPrice = currentPrice;
		this.availableQuantity = availableQuantity;
	}

	/**
	 * @effects <pre>
	 * if userName is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateID(String userName) {
		return userName != null && userName.length() >= 3 && userName.length() <= 6;
	}

	/**
	 * @effects <pre>
	 * if password is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateCompanyName(String companyName) {
		return companyName != null && companyName.length() > 0 && companyName.length() <= 20;
	}

	/**
	 * @effects <pre>
	 * if currentPrice is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateCurrentPrice(double currentPrice) {
		return currentPrice >= 0;
	}

	/**
	 * @effects <pre>
	 * if quantity is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateAvailableQuantity(int availableQuantity) {
		return availableQuantity >= 0;
	}

	/**
	 * @effects return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @effects return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @effects return currentPrice
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @effects <pre>
	 * if value is valid
	 *   this.value = value
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setCurrentPrice(double currentPrice) {
		if (validateCurrentPrice(currentPrice)) {
			this.currentPrice = currentPrice;
		}
	}

	/**
	 * @effects return quantity
	 */
	public int getAvailableQuantity() {
		return availableQuantity;
	}

	/**
	 * @effects <pre>
	 * if quantity is valid
	 *   this.quantity = quantity
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setAvailableQuantity(int availableQuantity) {
		if (validateAvailableQuantity(availableQuantity)) {
			this.availableQuantity = availableQuantity;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":<" + id + ", " + companyName + ", " + currentPrice + ", " + availableQuantity + ">";
	}

	/**
	 * empty constructor
	 */
	private Stock() {

	}

	/**
	 * @effects return a deep copy of this
	 */
	public Stock clone() {
		Stock s = new Stock();
		s.id = id;
		s.companyName = companyName;
		s.currentPrice = currentPrice;
		s.availableQuantity = availableQuantity;
		return s;
	}
}
