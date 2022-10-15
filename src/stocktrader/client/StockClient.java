package stocktrader.client;

import stocktrader.common.AccessDeniedException;
import stocktrader.common.TransactionFailedException;
import stocktrader.server.StockServer;

import java.util.Scanner;

/**
 * @Overview A class acts as a console-based, menu-driven program which allows the user
 *           to perform all the features provided by the server.
 * @Version 1.0.211127
 * @BasedOnVersionOf Mr. QuanDD
 * @author Phan Quang Tuan
 */
public class StockClient {
	private StockServer srv;

	/**
	 * @effects initializes (connects to) the server
	 */
	private StockClient() {
		srv = new StockServer();
	}

	// case 1
	private void login() {
		if (srv.isLoggedIn()) {
			System.out.println("\u001B[34m" + "you have already logged in" + "\u001B[0m");
			System.out.println("\u001B[35m" + "refresh page successfully" + "\u001B[0m");
			return;
		}

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter username: ");
		String usrName = sc.nextLine();
		System.out.print("Enter password: ");
		String password = sc.nextLine();

		String message = srv.login(usrName, password);
		System.out.println("\u001B[34m" + message + "\u001B[0m");
	}

	// case 3
	private void purchase() throws AccessDeniedException, TransactionFailedException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter stockNo: ");
		int stockNo = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter quantity: ");
		int q = sc.nextInt();

		boolean success = srv.purchase(stockNo, q);
		if (success) {
			System.out.println("\u001B[34m" + "purchased successfully!" + "\u001B[0m");
			System.err.println();
		} else {
			System.out.println("\u001B[34m" + "cannot purchase" + "\u001B[0m");
		}
	}

	// case 5
	private void sell() throws AccessDeniedException, TransactionFailedException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter stockNo: ");
		int stockNo = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter quantity: ");
		int q = sc.nextInt();

		boolean success = srv.sellStock(stockNo, q);
		if (success) {
			System.out.println("\u001B[34m" + "sold successfully!" + "\u001B[0m");
		} else {
			System.out.println("\u001B[34m" + "cannot sell" + "\u001B[0m");
		}
	}

	// case 7
	private void drunk() throws AccessDeniedException {
		boolean success = srv.nextDay();
		if (success) {
			System.out.println("\u001B[34m" + "Server says: a day is passed..." + "\u001B[0m");
		}
	}

	private void printMenu() {
		System.out.println("1. Login");
		System.out.println("2. View stocks on the market");
		System.out.println("3. Purchase a stock");
		System.out.println("4. View owned stocks");
		System.out.println("5. Sell a stock");
		System.out.println("6. Investment report");
		System.out.println("7. Drunk because of deadlines and sleep for 1 day :<");
		System.out.println("8. View top earners");
		System.out.println("9. Sign out");
		System.out.println("10. Quit");
	}

	private int getOption() {
		printMenu();
		Scanner sc = new Scanner(System.in);
		System.out.print("Choose an option: ");
		return sc.nextInt();
	}

	public void run() {
		int option = 0;
		while (option != 10) {
			// necessary for updating the server time when not logging in
			srv.request();
			System.out.println("session's id: " + srv.getSessionID());
			System.out.println("user's name: " + srv.getUserName());
			System.out.println("money: " + srv.getUserMoney());
			System.out.println("Server time: " + srv.getServerTime());
			System.out.println("Day: " + srv.getCurrentDate());
			try {
				option = getOption();
				switch (option) {
				case 1:
					login();
					break;
				case 2:
					System.out.println("\u001B[34m" + srv.listAllStocks() + "\u001B[0m");
					break;
				case 3:
					purchase();
					break;
				case 4:
					System.out.println("\u001B[34m" + srv.listOwnStocks() + "\u001B[0m");
					break;
				case 5:
					sell();
					break;
				case 6:
					System.out.println("\u001B[34m" + srv.trackStocks() + "\u001B[0m");
					break;
				case 7:
					drunk();
					break;
				case 8:
					System.out.println("\u001B[34m" + srv.viewTopEarners() + "\u001B[0m");
					break;
				case 9:
					System.out.println("\u001B[34m" + srv.signOut() + "\u001B[0m");
					break;
				case 10:
					//srv.signOut(); this line is disabled because of testing purpose
					System.out.println("\u001B[34m" + "Say goodbye!" + "\u001B[0m");
					break;
				default:
					System.err.println("Invalid input!");
					break;
				}
			} catch (Exception e) {
				//e.printStackTrace(); enable this when you need to debug only
				System.out.println("\u001B[34m" + "Server says: " + e.getMessage() + "\u001B[0m");
			}
			Scanner sc = new Scanner(System.in);
			System.out.println("\u001B[34m" + ">> Press enter to continue" + "\u001B[0m");
			sc.nextLine();
		}
		System.out.println("-----------------------");
	}

	public static void main(String[] args) {
		StockClient client = new StockClient();
		client.run();

		StockClient client2 = new StockClient();
		client2.run();
	}
}
