package stocktrader.server;

import stocktrader.common.AccessDeniedException;
import stocktrader.common.OutOfDateException;
import stocktrader.common.TransactionFailedException;
import stocktrader.server.UI.DisplayReport;

import java.io.*;
import java.util.*;

/**
 * @Overview A StockServer communicates with StockClient and works with other entities such as User, Stock, StockPurchase
 * @attributes <pre>
 * users		  ArrayList<User>
 * currentUser	  User
 * stocks		  ArrayList<Stock>
 * Day			  int
 * lastEdit		  Date
 * editor		  String
 * </pre>
 * @Object a typical StockServer is c:<ul, cu, s, d, l, e> where ul is users, cu is currentUser, s is stocks, d is day
 *         l is lastEdit which represents time at last DB valid save,
 *         e represents userName that lastly modified the DB
 *   AF(c) = c:<ul, cu, s, d, l, e>
 * @rep_invariant
 *   ul != null && cu != null && cu is in ul &&
 *   a>=0 &&
 *   s != null && e != null
 *
 * @Version 1.0.211127
 * @BasedOnVersionOf Mr. QuanDD
 * @author Phan Quang Tuan
 */
public class StockServer implements Serializable {
	private ArrayList<User> users;
	private User currentUser;
	private ArrayList<Stock> stocks;
	private int day;
	private Date lastEdit; // time's at last DB valid save
	private String editor; // userName that lastly modified the DB

	/**
	 * @effects <pre>
	 * Load default DB to initialize all the attributes
	 * </pre>
	 */
	public StockServer() {
		defaultLoad();
	}

	/**
	 * @modifies currentUser
	 * @effects <pre>
	 *  if there exists u in c.users
	 *  where u.userName==username /\ u.password==password
	 *     currentUser = u
	 *     return "logged in"
	 *   else
	 *     return "login failed"
	 * </pre>
	 */
	public String login(String username, String password) {
		for (User u : users) {
			if (u.getUserName().equals(username) && u.getPassword().equals(password)) {
				currentUser = u;
				//System.out.println("yahooo"); debug
				return "logged in";
			}
		}
		return "login failed";
	}

	/**
	 * @modifies currentUser
	 * @effects <pre>
	 *  if currentUser!=null
	 *    currentUser = null
	 *    return "signed out"
	 *  else
	 *    return "you are not logged in!"
	 * </pre>
	 */
	public String signOut() {
		if (currentUser != null) {
			currentUser = null;
			return "signed out";
		}
		return "you are not logged in!";
	}

	/**
	 * @modifies all attributes of this
	 * @effects <pre>
	 *  if currentUser!=null
	 *    return true
	 *  else
	 *    return false
	 * </pre>
	 */
	public boolean isLoggedIn() {
		return currentUser != null;
	}

	/**
	 * request the newest state of the server
	 * @modifies all attributes of this
	 * @effects Load the default DB
	 */
	public void request() {
		defaultLoad();
	}

	/**
	 * This method checks if user has logged in or not
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw new AccessDeniedException
	 *  else
	 *    do nothing
	 * </pre>
	 */
	private void checkStatus() throws AccessDeniedException {
		if (currentUser == null) {
			throw new AccessDeniedException("not logged in");
		}
	}

	/**
	 * Listing all stocks purchased by user
	 * @modifies all attributes of this
	 * @effects <pre>
	 *   if currentUser==null
	 *     throw AccessDeniedException
	 *   else
	 *     load default DB
	 *     return a String containing information
	 *     about all stocks in c.userStocks
	 * </pre>
	 */
	public String listAllStocks() throws AccessDeniedException {
		checkStatus();
		defaultLoad();
		StringBuilder sb = new StringBuilder();
		sb.append("last update at: ").append(lastEdit).append("\n");
		sb.append(DisplayReport.displayAllStock(stocks));
		return sb.toString();
	}

	/**
	 * This method uses the stock no. listed in the listAllStocks() method.
	 * @modifies all attributes of this, stock.availableQuantity, currentUser.balance, `currentDB.dat`
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *
	 *  Load default DB
	 *  stockNo = stockNo -1
	 *
	 *  if stockNo < 0 \/ stockNo >= stocks.size()
	 *    throw TransactionFailedException with the message: stock doesn't exist
	 *  else
	 *    if quantity < 0 \/ quantity > stock.availableQuantity
	 *      throw TransactionFailedException with the message: not enough quantity
	 *    else
	 *      if currentUser.balance < (stock.currentPrice * quantity)
	 *        throw TransactionFailedException with the message: not enough money
	 *      else
	 * 		  create a new StockPurchase object
	 *        add the object into c.userStocks
	 *
	 *        (update) subtract quantity from stock.availableQuantity
	 *        (update) subtract currentUser.balance
	 *
	 *        save to default DB
	 *        if save successfully:
	 *          return true
	 *        else
	 *          return false
	 * </pre>
	 */
	public boolean purchase(int stockNo, int quantity) throws AccessDeniedException, TransactionFailedException {
		checkStatus();
		defaultLoad();
		double balance = currentUser.getBalance();

		stockNo -= 1;
		if (stockNo < 0 || stockNo >= stocks.size()) {
			throw new TransactionFailedException("stock doesn't exist");
		} else {
			Stock stock = stocks.get(stockNo);

			if (quantity < 0 || quantity > stock.getAvailableQuantity()) {
				throw new TransactionFailedException("not enough quantity");
			} else {
				if (balance < (stock.getCurrentPrice() * quantity)) {
					throw new TransactionFailedException("not enough money");
				} else {
					try {
						Calendar c = Calendar.getInstance();
						StockPurchase sp = new StockPurchase(stock, quantity, stock.getCurrentPrice(), c.getTime(),
								day);
						currentUser.addToOwnStock(sp);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update the stock
					stock.setAvailableQuantity(stock.getAvailableQuantity() - quantity);
					// update currentUser.balance
					currentUser.setBalance(currentUser.getBalance() - (stock.getCurrentPrice() * quantity));
					return defaultSave();
				}
			}
		}
	}

	/**
	 * Listing all stocks purchased by user
	 * @modifies all attributes of this
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *  else
	 *    Load default DB
	 *    return a String containing information
	 *    about all stocks in c.userStocks
	 * </pre>
	 */
	public String listOwnStocks() throws AccessDeniedException {
		checkStatus();
		defaultLoad();
		StringBuilder sb = new StringBuilder();
		sb.append("last update at: ").append(lastEdit).append("\n");
		sb.append(DisplayReport.displayOwnStock(currentUser.getOwnStock()));
		return sb.toString();
	}

	/**
	 * This method uses the stock no. listed in the listOwnedStocks() method.
	 * @modifies all attributes of this, s.quantity, currentUser.balance, `currentDB.dat`
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *
	 *   //let's say: s is the stock
	 *   			  SP is the StockPurchase of the stock
	 *
	 *   Load default DB
	 *   stockNo -= 1
	 *   if StockNo < 0 \/ StockNo >= list.size
	 *     throw new TransactionFailedException with message: stock doesn't exist
	 *   else
	 *     if quantity < 0 \/ quantity > SP.quantity
	 *       throw new TransactionFailedException with message: invalid quantity
	 *     else
	 *       subtract quantity from SP.quantity
	 *
	 *       if SP.quantity = quantity
	 *         remove SP
	 *       else
	 *         (update) add quantity to s.AvailableQuantity
	 *         (update) add s.currentPrice * quantity to currentUser.balance
	 *
	 *       save to default DB
	 *  	 if save successfully
	 *    	   return true
	 *       else
	 *         return false
	 * </pre>
	 */
	public boolean sellStock(int stockNo, int quantity) throws AccessDeniedException, TransactionFailedException {
		checkStatus();
		defaultLoad();
		ArrayList<StockPurchase> list = currentUser.getOwnStock();

		stockNo -= 1;
		if (stockNo < 0 || stockNo >= list.size()) {
			throw new TransactionFailedException("stock doesn't exist");
		} else {
			StockPurchase sp = list.get(stockNo);
			int oldQuantity = sp.getQuantity();
			if (quantity < 0 || quantity > oldQuantity) {
				throw new TransactionFailedException("invalid quantity");
			} else {
				if (oldQuantity - quantity > 0) {
					sp.setQuantity(oldQuantity - quantity);
				} else {
					currentUser.removeFromOwnStock(sp);
				}

				Stock s = sp.getTheStock();
				// update the stock
				s.setAvailableQuantity(s.getAvailableQuantity() + quantity);
				// update currentUser.balance
				currentUser.setBalance(currentUser.getBalance() + (s.getCurrentPrice() * quantity));
				return defaultSave();
			}
		}
	}

	/**
	 * Randomly change all stock prices as if a day has passed.
	 * @modifies all attributes of this, c.stocks, currentUser.currentDate, `currentDB.dat`
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *  else
	 *    Load default DB
	 *    randomly change all stock prices
	 *	  (limit the rate of changing to no more than 15% per day)
	 *    increase c.day by 1 and currentUser.currentDate by 1
	 *    save to default DB
	 *    if save successfully
	 *      return true
	 *    else
	 *      return false
	 * </pre>
	 */
	public boolean nextDay() throws AccessDeniedException {
		checkStatus();
		defaultLoad();
		// update stocks' currentPrice
		for (Stock s : stocks) {
			double rate = 0.85 + Math.random() * 0.3;
			s.setCurrentPrice(s.getCurrentPrice() * rate);
		}

		//increase c.day by 1
		day++;

		// increases currentUser.currentDate by 1
		currentUser.increaseCurrentDate();
		return defaultSave();
	}

	/**
	 * Show a table-like list of all stocks owned by the user with purchase price and current price,
	 * and the calculated benefit for each stock.
	 * @modifies all attributes of this
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *  else
	 *    Load default DB
	 *    if save successfully
	 *      do nothing
	 *    else
	 *      print out the failure message
	 *    return a String containing information
	 * </pre>
	 */
	public String trackStocks() throws AccessDeniedException {
		checkStatus();
		defaultLoad();
		StringBuilder sb = new StringBuilder();
		sb.append("last update at: ").append(lastEdit).append("\n");
		sb.append(DisplayReport.displayTrackStock(currentUser.getOwnStock()));
		return sb.toString();
	}

	/**
	 * view the top earners (along with the number of days they have run the program)
	 * @modifies all attributes of this, especially all User.profit of each User in users
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *  else
	 *    Load default DB
	 *    make an shallow copy ArrayList of users
	 *    sort that ArrayList based on User.balance
	 *    processing data
	 *    save to the default DB
	 *    return a String containing information
	 * </pre>
	 */
	public String viewTopEarners() throws AccessDeniedException {
		checkStatus();
		defaultLoad();
		ArrayList<User> list = new ArrayList<>(users);
		// sort in descending order
		Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				if (u1.getBalance() == u2.getBalance()) {
					return u1.getCurrentDate() < u2.getCurrentDate() ? -1 : 1;
				} else {
					return u1.getBalance() > u2.getBalance() ? -1 : 1;
				}
			}
		});
		String data = DisplayReport.displayTopEarners(list);
		defaultSave();
		StringBuilder sb = new StringBuilder();
		sb.append("last update at: ").append(lastEdit).append("\n");
		sb.append(data);
		return sb.toString();
	}

	/**
	 * @effects <pre>
	 *  if currentUser != null
	 *    return "[not logged in]"
	 *  else
	 *    return a string contains userName and their balance
	 * </pre>
	 */
	// method that is not used
	public String userWelcome() {
		if (currentUser == null) {
			return "[not logged in]";
		} else {
			return currentUser.getUserName() + " ($" + String.format("%.2f", currentUser.getBalance()) + ")";
		}
	}

	/**
	 * @effects <pre>
	 *  if currentUser==null
	 *    return "?"
	 *  else
	 *    return a String represents how many day the currentUser has used this app
	 * </pre>
	 */
	public String getCurrentDate() {
		if (currentUser == null) {
			return "?";
		} else {
			return "" + currentUser.getCurrentDate();
		}
	}

	/**
	 * @effects return a String represents time provided by getVirtualTime() method
	 */
	public String getServerTime() {
		return "" + getVirtualTime();
	}

	/**
	 * Because you can sleep for one day, it means you can modify time!
	 * @effects <pre>
	 *   return a Date represents modified time that its day equal sum of:
	 *   current real time + c.day - 1
	 * </pre>
	 */
	private Date getVirtualTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day - 1);
		return cal.getTime();
	}

	/**
	 * @effects <pre>
	 *  if currentUser != null
	 *    return currentUser's name
	 *  else
	 *    return "guest session"
	 * </pre>
	 */
	public String getUserName() {
		return (currentUser != null) ? currentUser.getName() : "[guest session]";
	}

	/**
	 * @effects <pre>
	 *  if currentUser != null
	 *    return currentUser's balance
	 *  else
	 *    return "?"
	 * </pre>
	 */
	public String getUserMoney() {
		return (currentUser != null) ? "" + String.format("$%.2f", currentUser.getBalance()) : "?";
	}

	/**
	 * @effects return id of current session based on this.toString()
	 */
	public String getSessionID() {
		String id = this.toString();
		id = id.substring(id.indexOf("@"));
		return id;
	}

	/**
	 * A method to check if this session is newer than the DB or not.
	 * @requires currentUser!=null
	 * @effects <pre>
	 *  load Date object from `defaultDB.dat`:		dataBaseTime
	 *  load editor from `defaultDB.dat`:			modifier
	 *  if c.lastEdit.getTime <= dataBaseTime.getTime /\ modifier!=c.currentUser.userName
	 *    it means this server session is out of date -> it's not valid to save data into the DB
	 *    -> throw new OutOfDateException with message: "your database is out of date, cannot do the action"
	 *    return false
	 *  else if Exception occurs:
	 *    return false
	 *  else
	 *    return true
	 * </pre>
	 */
	private boolean isUpToDate() {
		try {
			FileInputStream fis = new FileInputStream("defaultDB.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Date dataBaseTime = (Date) ois.readObject();
			String modifier = (String) ois.readObject();

			if (!lastEdit.after(dataBaseTime) && !modifier.equals(editor)
					&& !modifier.equals("000 SeRvEr_-_SeTuP 000")) {
				throw new OutOfDateException(
						"Your session is out of date, we've just updated for you. Please try again!");
			}

			ois.close();
			fis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @modifies c.editor, `defaultDB.dat`
	 * @effects <pre>
	 *  editor = currentUser.userName
	 *  Save the DB to an external file with the specified name
	 *  if Exception occurs
	 *    return false
	 *  else
	 *    return true
	 * </pre>
	 */
	private boolean saveDB(String DBname) {
		editor = currentUser.getUserName();
		try {
			FileOutputStream fos = new FileOutputStream(DBname + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(getVirtualTime());
			oos.writeObject(editor);
			oos.writeObject(users);
			oos.writeObject(stocks);
			oos.writeObject(day);
			oos.close();
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("cannot save DB!");
			return false;
		}
	}

	/**
	 * @modifies all attributes of this
	 * @effects <pre>
	 *  Load the DB form the specified name
	 *  -> modify all the attributes of this object,
	 *     including this.currentUser
	 *     -> if currentUser!=null
	 *          login again using currentUser's usr and password
	 *  if Exception occurs
	 *    return false
	 *  else
	 *    update this.lastEdit
	 *    return true
	 * </pre>
	 */
	private boolean loadDB(String DBname) {
		try {
			FileInputStream fis = new FileInputStream(DBname + ".dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Date d = (Date) ois.readObject();
			editor = (String) ois.readObject();
			users = (ArrayList<User>) ois.readObject();
			stocks = (ArrayList<Stock>) ois.readObject();
			day = (int) ois.readObject();

			// update this.currentUser
			if (currentUser != null) {
				login(currentUser.getUserName(), currentUser.getPassword());
			}

			// init and update lastEdit
			lastEdit = Calendar.getInstance().getTime();
			lastEdit.setTime(d.getTime());
			ois.close();
			fis.close();
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("DB corrupted or no such DB: \"" + DBname + "\"");
			return false;
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
			System.err.println("DB was damaged: \"" + DBname + "\"");
			return false;
		}
	}

	/**
	 * @effects <pre>
	 *  if currentUser==null
	 *    throw AccessDeniedException
	 *  else
	 *    if DB is up to date
	 *      save to `defaultDB.dat`
	 *      return true
	 *    else
	 *      Load to the default DB
	 *      return false
	 * </pre>
	 */
	private boolean defaultSave() throws AccessDeniedException {
		checkStatus();
		if (isUpToDate()) {
			return saveDB("defaultDB");
		} else {
			defaultLoad();
			return false;
		}
	}

	/**
	 * load the default DB.
	 * @modifies all attributes of this
	 * @effects <pre>
	 *  load DB from `defaultDB.dat`
	 *  and return true if no exception occurs
	 * </pre>
	 */
	private boolean defaultLoad() {
		return loadDB("defaultDB");
	}
}
