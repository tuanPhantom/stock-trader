package stocktrader.server;

import stocktrader.common.NotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @Overview User represents a person or thing that uses `stocktrader` program
 * @attributes <pre>
 * userName		String
 * password		String
 * name			String
 * Balance		double
 * ownStock		ArrayList<StockPurchase>
 * currentDate  int
 * profit		double
 * </pre>
 * @Object a typical User is c:<u, p, n, b, o, cd, f> where u is userName, p is password, n is name
 * 			b is money, o is ownStock, cd is currentDate, f is profit
 * AF(c) = c:<u, p, n, b, o, cd, f>
 * @rep_invariant
 * 	u!=null && u.length>0 && u.matches("[A-Za-z0-9]+")
 * 	p!=null && p.length>0 &&
 * 	n!=null && n.length>0 &&
 * 	m>=0d &&
 * 	o!=null &&
 * 	cd>=1
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public class User implements Serializable {
	private String userName;
	private String password;
	private String name;
	private double balance;
	private ArrayList<StockPurchase> ownStock;
	private int currentDate;
	private double profit; // derived attribute

	/**
	 * @effects <pre>
	 * if userName, password, name, balance are valid
	 * 	initialize this as c:<userName, password, name, balance, [], 1, 0>
	 * else
	 * 	throw NotPossibleException</pre>
	 */
	public User(String userName, String password, String name, double balance, int currentDate)
			throws NotPossibleException {
		if (!validateUserName(userName)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid userName:'" + userName + "'");
		}

		if (!validatePassword(password)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid password:'" + password + "'");
		}

		if (!validateName(name)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid name:'" + name + "'");
		}

		if (!validateBalance(balance)) {
			throw new NotPossibleException(getClass().getSimpleName() + ".init: invalid balance:'" + balance + "'");
		}

		if (!validateCurrentDate(currentDate)) {
			throw new NotPossibleException(
					getClass().getSimpleName() + ".init: invalid currentDate:'" + currentDate + "'");
		}

		this.userName = userName;
		this.password = password;
		this.name = name;
		this.balance = balance;
		this.ownStock = new ArrayList<>();
		this.currentDate = currentDate;
	}

	/**
	 * @effects <pre>
	 * if userName is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateUserName(String userName) {
		return userName != null && userName.length() > 0 && userName.matches("[A-Za-z0-9]+");
	}

	/**
	 * @effects <pre>
	 * if password is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validatePassword(String password) {
		return password != null && password.length() > 0;
	}

	/**
	 * @effects <pre>
	 * if name is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateName(String name) {
		return name != null && name.length() > 0;
	}

	/**
	 * @effects <pre>
	 * if balance is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateBalance(double balance) {
		return balance >= 0d;
	}

	/**
	 * @effects <pre>
	 * if currentDate is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateCurrentDate(int currentDate) {
		return currentDate >= 1;
	}

	/**
	 * @effects return name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @effects <pre>
	 * if name is valid
	 *   this.userName = userName
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setUserName(String userName) {
		if (validateUserName(userName)) {
			this.userName = userName;
		}
	}

	/**
	 * @effects return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @effects <pre>
	 * if password is valid
	 *   this.password = password
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setPassword(String password) {
		if (validatePassword(password)) {
			this.password = password;
		}
	}

	/**
	 * @effects return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @effects <pre>
	 * if name is valid
	 *   this.name = name
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setName(String name) {
		if (validateName(name)) {
			this.name = name;
		}
	}

	/**
	 * @effects return money
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @effects <pre>
	 * if money is valid
	 *   this.money = money
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setBalance(double balance) {
		if (validateBalance(balance)) {
			this.balance = balance;
		}
	}

	/**
	 * @effects return a shallow copy of ownStock
	 */
	public ArrayList<StockPurchase> getOwnStock() {
		return new ArrayList<>(ownStock);
	}

	/**
	 * @effects return size of ownStock
	 */
	public int sizeOfOwnStock() {
		return ownStock.size();
	}

	/**
	 * @effects <pre>
	 *  if s is in one of StockPurchase(s) in ownStock:
	 *    return that StockPurchase
	 *  else
	 *    return null
	 * </pre>
	 */
	public StockPurchase stockPurchaseOf(Stock s) {
		for (StockPurchase sp : ownStock) {
			Stock st = sp.getTheStock();
			if (st.getId().equals(s.getId())) {
				return sp;
			}
		}
		return null;
	}

	/**
	 * @effects <pre>
	 * if StockPurchase is valid
	 *   ownStock.add(StockPurchase)
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void addToOwnStock(StockPurchase sp) {
		if (validateStockPurchase(sp)) {
			ownStock.add(sp);
		}
	}

	/**
	 * @effects <pre>
	 * if oldSP and newSP are valid && oldSP is in ownStock
	 * 	 let's say i is index of oldSP in ownStock
	 *   ownStock.set(i, newSP)
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void setFromOwnStock(StockPurchase oldSP, StockPurchase newSP) {
		if (validateStockPurchase(oldSP) && validateStockPurchase(newSP) && ownStock.contains(oldSP)) {
			ownStock.set(ownStock.indexOf(oldSP), newSP);
		}
	}

	/**
	 * @effects <pre>
	 * if StockPurchase is valid && StockPurchase is in ownStock
	 *   ownStock.remove(StockPurchase)
	 * else
	 *   do nothing
	 * </pre>
	 */
	public void removeFromOwnStock(StockPurchase sp) {
		if (validateStockPurchase(sp) && ownStock.contains(sp)) {
			ownStock.remove(sp);
		}
	}

	/**
	 * @effects return currentDate
	 */
	public int getCurrentDate() {
		return currentDate;
	}

	/**
	 * @effects increase this.currentDate by 1
	 */
	public void increaseCurrentDate() {
		currentDate++;
	}

	/**
	 * @effects return profit
	 */
	public double getProfit() {
		calculateProfit();
		return profit;
	}

	/**
	 * @modifies this.profit
	 * @effects <pre>
	 *  create a new HashMap
	 *  for all StockPurchase in ownStock
	 *  calculate profit of  stock that map the StockPurchase
	 *  if HashMap does not contains stock
	 *    put stock, profit to HashMap
	 *  else
	 *    replace exProfit with new profit of that stock
	 *
	 *   this.profit = 0
	 *   for all profit in HashMap
	 *     add it to this.profit
	 *
	 * </pre>
	 */
	private void calculateProfit() {
		HashMap<Stock, Double> stockMap = new HashMap<>();
		for (StockPurchase sp : ownStock) {
			Stock s = sp.getTheStock();
			// profit of 1 StockPurchase
			double profit = sp.getQuantity() * (s.getCurrentPrice() - sp.getPurchasePrice());

			if (!stockMap.containsKey(s)) {
				stockMap.put(s, profit);
			} else {
				double exProfit = stockMap.get(s);
				stockMap.replace(s, exProfit, exProfit + profit);
			}
		}

		this.profit = 0;
		for (Double p : stockMap.values()) {
			this.profit += p;
		}
	}

	/**
	 * @effects <pre>
	 * if ownStock is valid
	 *   return true
	 * else
	 *   return false
	 * </pre>
	 */
	private boolean validateStockPurchase(StockPurchase sp) {
		Calendar c = Calendar.getInstance();
		c.set(1900, 0, 1);
		return sp != null && sp.getPurchasePrice() >= 0 && sp.getTheStock() != null && sp.getQuantity() > 0
				&& sp.getPurchaseDate().after(c.getTime());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":<" + userName + ", " + password + ", " + name + ", " + balance + ownStock
				+ ", " + currentDate + ", " + profit + ">";
	}

	/**
	 * empty constructor
	 */
	private User() {
	}

	/**
	 * @effects return a deep copy of this
	 */
	public User clone() {
		User cl = new User();
		cl.userName = userName;
		cl.password = password;
		cl.name = name;
		cl.balance = balance;
		cl.ownStock = getOwnStock();
		cl.currentDate = currentDate;
		cl.profit = profit;
		return cl;
	}
}
