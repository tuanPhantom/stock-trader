package stocktrader.server.UI;

import stocktrader.server.Stock;
import stocktrader.server.StockPurchase;
import stocktrader.server.User;

import java.util.ArrayList;

/**
 * @overview A class that contains factory methods to display tabular report
 *
 * @Version 1.0.211127
 * @author Phan Quang Tuan
 */
public abstract class DisplayReport {
	/**
	 * @requires source!=null
	 * @effects return a text-based tabular report of all the stocks in the market
	 */
	public static String displayAllStock(ArrayList<Stock> source) {
		Stock[] objs = source.toArray(new Stock[source.size()]);
		StringBuilder sb = new StringBuilder();

		String text = String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %8s |\n", "No.", "Stock's ID", "Company",
				"Stock's price", "Quantity");
		sb.append(line(text));
		sb.append(text);
		sb.append(line(text));

		int count = 1;
		for (Stock s : objs) {
			sb.append(String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %8s |\n", count, s.getId(),
					s.getCompanyName(), String.format("%14.2f", s.getCurrentPrice()), s.getAvailableQuantity()));
			count++;
		}
		sb.append(line(text));
		return sb.toString();
	}

	/**
	 * @requires source!=null
	 * @effects return a text-based tabular report of all the stocks that one user own
	 */
	public static String displayOwnStock(ArrayList<StockPurchase> source) {
		StockPurchase[] objs = source.toArray(new StockPurchase[source.size()]);
		StringBuilder sb = new StringBuilder();

		String text = String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %8.8s | %42.42s |\n", "No.", "Stock's ID",
				"Company", "Purchase price", "Quantity", "Purchase at");
		sb.append(line(text));
		sb.append(text);
		sb.append(line(text));

		int count = 1;
		for (StockPurchase sp : objs) {
			Stock s = sp.getTheStock();
			sb.append(String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %8.8s | (Day %6.6s) %29.29s |\n", count,
					s.getId(), s.getCompanyName(), String.format("%14.2f", sp.getPurchasePrice()), sp.getQuantity(),
					sp.getPurchaseDay(), sp.getPurchaseDate()));
			count++;
		}
		sb.append(line(text));
		return sb.toString();
	}

	/**
	 * @requires source!=null
	 * @effects return a text-based tabular report for tracking the user's own stocks
	 */
	public static String displayTrackStock(ArrayList<StockPurchase> source) {
		StockPurchase[] objs = source.toArray(new StockPurchase[source.size()]);
		StringBuilder sb = new StringBuilder();

		String text = String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %14.14s | %14.14s |\n", "No.",
				"Stock's ID", "Company", "Current price", "Purchase price", "Profit");
		sb.append(line(text));
		sb.append(text);
		sb.append(line(text));

		int count = 1;
		for (StockPurchase sp : objs) {
			Stock s = sp.getTheStock();
			double profit = s.getCurrentPrice() - sp.getPurchasePrice();
			sb.append(String.format("| %3.3s | %10.10s | %20.20s | %14.14s | %14.14s | %14.14s |\n", count, s.getId(),
					s.getCompanyName(), String.format("%14.2f", s.getCurrentPrice()),
					String.format("%14.2f", sp.getPurchasePrice()), String.format("%14.2f", profit)));
			count++;
		}
		sb.append(line(text));
		return sb.toString();
	}

	/**
	 * @requires source!=null
	 * @modifies all User in source
	 * @effects <pre>
	 *   calculate profit of each user
	 *   return a text-based tabular report of top users
	 * </pre>
	 */
	public static String displayTopEarners(ArrayList<User> source) {
		User[] objs = source.toArray(new User[source.size()]);
		StringBuilder sb = new StringBuilder();

		String text = String.format("| %-3.3s | %-20.20s | %-14.14s | %-6.6s |\n", "No.", "UserName", "Profit", "Day");
		sb.append(line(text));
		sb.append(text);
		sb.append(line(text));

		int count = 1;
		for (User u : objs) {
			sb.append(String.format("| %-3.3s | %-20.20s | %-14.14s | %-6.6s |\n", count, u.getUserName(),
					u.getBalance(), u.getCurrentDate()));
			count++;
		}
		sb.append(line(text));
		return sb.toString();
	}

	/**
	 * @return
	 *   a String represents horizontal line that matches the tabular format, which starts and ends with a plus ("+") character.
	 *   The positions of plus characters is provided by integer numbers in the method parameter.
	 *   e.g.
	 *   input:   0, 6, 13
	 *   ,output: +-----+------+
	 *   Remember that you can put any number of parameter you want.
	 *
	 * @requires plusPoint sorted in ascending order
	 * @effects <pre>
	 *  plusPoint is an integer array with elements sorted in ascending order,
	 *  i.e. [2, 3, 5, 8, 9,..,n]
	 *   -> create a new integer variable: length = n
	 *   -> create a new integer variable: index
	 *        s.t. index in range [0,n)
	 *   for all integer number from 0 -> n-1
	 *     if number!=plusPoint[index]
	 *       add '-' to the return String
	 *     else
	 *       add '+' to the return String
	 *       increase index by 1
	 * </pre>
	 */
	// using:
	// displayAllStock:   sb.append(line(0, 6, 19, 42, 59, 70));
	// displayOwnStock:   sb.append(line(0, 6, 19, 42, 59, 70, 115));
	// displayTrackStock: sb.append(line(0, 6, 19, 42, 59, 76, 93));
	// displayTopEarners: sb.append(line(0, 6, 29, 46, 55));
	private static String line(int... plusPoint) {
		int length = plusPoint[plusPoint.length - 1] + 1;
		int plusIndex = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i != plusPoint[plusIndex]) {
				sb.append("-");
			} else {
				sb.append("+");
				plusIndex++;
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * @return
	 *   a String represents horizontal line that matches the tabular format, which starts and ends with a plus ("+") character.
	 *   The plus character is determined by the position of '|' character appearing in the text.
	 *   e.g.
	 *   input:   |ab de| e1@h |
	 *   ,output: +-----+------+
	 * @requires text!=null
	 * @effects <pre>
	 *   for all character in text
	 *     if character==[System.lineSeparator()] i.e. \r, \n
	 *       do nothing
	 *     else
	 *      if character!='|'
	 *        add '-' to the return String
	 *      else
	 *        add '+' to the return String
	 * </pre>
	 */
	private static String line(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) != '\r' && text.charAt(i) != '\n') {
				if (text.charAt(i) != '|') {
					sb.append("-");
				} else {
					sb.append("+");
				}
			}
		}
		sb.append("\n");
		return sb.toString();
	}
}
