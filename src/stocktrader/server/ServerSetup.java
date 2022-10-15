package stocktrader.server;

import stocktrader.common.NotPossibleException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Must be run before the main program
 * Initializes server data:
 *   - 3 users with random balance between $20 and $30
 *   - 10 stocks with:
 *       random 3-char (uppercase) names such as ABC, HFJ, RJI...
 *       random quantity between 100 and 1000
 *       random price between $1 and $20
 * Saves all data into the file `defaultDB.dat`
 * @Version 1.0.211127
 * @author Mr. QuanDD, Phan Quang Tuan
 */
public class ServerSetup {

	public static double randomBalance() {
		return Math.ceil(Math.random() * 100 + 200) / 10;
	}

	public static double randomStockPrice() {
		return Math.ceil(Math.random() * 190 + 10) / 10;
	}

	public static String randomStockName() {
		String s = "";

		for (int i = 0; i < 3; i++) {
			char c = (char) ((Math.random() * 26) + 65);
			s += c;
		}

		return s;
	}

	public static String randomCompStockName() {
		String s = "";

		for (int i = 0; i < 3; i++) {
			char c = (char) ((Math.random() * 26) + 65);
			String tmp = "" + c + c;
			s += tmp;
		}

		return s;
	}

	public static int randomQuantity() {
		return (int) ((Math.random() * 900) + 100);
	}

	public static void main(String[] args) throws NotPossibleException, IOException {
		// init users
		ArrayList<User> users = new ArrayList<>();
		User[] u = new User[5];
		u[0] = new User("tuan", "123", "tuanPQ", randomBalance(), 1);
		u[1] = new User("congnv", "wpr", "congNV", randomBalance(), 1);
		u[2] = new User("thangnx", "ss1", "thangNX", randomBalance(), 1);
		u[3] = new User("camnh", "dbs", "camNH", randomBalance(), 1);
		u[4] = new User("quandd", "se1", "quanDD", randomBalance(), 1);

		users.addAll(Arrays.asList(u));

		// init stocks
		ArrayList<Stock> stocks = new ArrayList<>();
		Stock[] s = new Stock[5];
		s[0] = new Stock("COMP", "composite.,ltd", 12.88, 12);
		s[1] = new Stock("NDX", "noDogex", 11.3, 103);
		s[2] = new Stock("SPX", "sp500", 3.25, 1500);
		s[3] = new Stock("INDU", "india adu", 10.25, 50);
		s[4] = new Stock("TLA", "Tesla", 100.163, 15);

		stocks.addAll(Arrays.asList(s));

		for (int i = 0; i < 5; i++) {
			stocks.add(new Stock(randomStockName(), randomCompStockName(), randomStockPrice(), randomQuantity()));
		}

		// write server data into a file
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("defaultDB.dat"));
		oos.writeObject(Calendar.getInstance().getTime());
		oos.writeObject("000 SeRvEr_-_SeTuP 000"); // this String must not be a valid User.userName
		oos.writeObject(users);
		oos.writeObject(stocks);
		oos.writeObject(1);
		oos.close();
	}
}
