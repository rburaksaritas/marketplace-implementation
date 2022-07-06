package executable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


import elements.*;

/**
 * Main class that simulates the simple market model.
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Main {

	/**
	 * Random object for the random seed input line.
	 */
	public static Random myRandom;
	/**
	 * Keeps track of number of invalid queries.
	 */
	public static int numberOfInvalidQueries = 0;
	
	/**
	 * Main method that reads the input, executes actions and prints the requested informations to a text file as an output.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		/**
		 * random seed
		 */
		int Seed = in.nextInt();
		myRandom = new Random(Seed);
		
		// moves reader to the next line
		in.nextLine();
		
		/**
		 * fee of the marketplace
		 */
		int Fee = in.nextInt();
		Market market = new Market(Fee);
		/**
		 * number of traders
		 */
		int T = in.nextInt();
		/**
		 * number of queries
		 */
		int Q = in.nextInt();

		// moves reader to the next line
		in.nextLine();
		
		// Fields for the program to store input in an array.
		
		/**
		 * Counter field to use in storing the input line to create a new port. <br>
		 * Also works as the ID of that port. Gets incremented by one in every use.
		 * 
		 */
		int currentTraderID = 0;
		/**
		 * Counter field to use in storing the input line to create a new ship. <br>
		 * Also works as the ID of that ship. Gets incremented by one in every use.
		 * 
		 */
		int currentQueryID = 0;

		/**
		 * Stores the input line to create a new port.
		 */
		ArrayList<String> traderData = new ArrayList<String>();
		/**
		 * Stores the input line to create a new ship.
		 */
		ArrayList<String> queryData = new ArrayList<String>();

		// Stores trader input lines in an array
		for (int i = 0; i < T; i++) {

			String inputLine = in.nextLine();
			
				traderData.add(currentTraderID, inputLine);
				currentTraderID += 1;    
		}

		// Stores query input lines in an array
		for (int i = 0; i < Q; i++) {

			String inputLine = in.nextLine();

				queryData.add(currentQueryID, inputLine);
				currentQueryID += 1;    
		}
		
		// Creates TRADER objects
		for (int i = 0; i < traderData.size(); i++) {
			double dollars = Double.parseDouble(traderData.get(i).split(" ")[0]);
			double coins = Double.parseDouble(traderData.get(i).split(" ")[1]);
			Trader trader = new Trader(dollars, coins);
			market.traders.add(i, trader);
		}
		
		// QUERIES
		for (int i = 0; i < queryData.size(); i++) {
			
			int queryID = Integer.parseInt(queryData.get(i).split(" ")[0]);
			
			// Trader Specific Queries
			
			//	gives buying order of specific price
			if (queryID == 10) {
				int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
				double price = Double.parseDouble(queryData.get(i).split(" ")[2]);
				double amount = Double.parseDouble(queryData.get(i).split(" ")[3]);
				
				BuyingOrder bOrder = new BuyingOrder(traderID, amount, price);
				market.giveBuyOrder(bOrder);
				market.checkTransactions(market.traders);
			}
			
			// gives buying order of market price
			else if (queryID == 11) {
				// if selling order queue is not empty
				if (market.getSellingOrders().size()>0) {
					int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
					double price = market.getSellingOrders().peek().getPrice();
					double amount = Double.parseDouble(queryData.get(i).split(" ")[2]);
					
					BuyingOrder bOrder = new BuyingOrder(traderID, amount, price);
					market.giveBuyOrder(bOrder);
					market.checkTransactions(market.traders);
				}		
				else {
					numberOfInvalidQueries++;
				}
			}
			
			// gives selling order of specific price
			else if (queryID == 20) {
				int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
				double price = Double.parseDouble(queryData.get(i).split(" ")[2]);
				double amount = Double.parseDouble(queryData.get(i).split(" ")[3]);
				
				SellingOrder sOrder = new SellingOrder(traderID, amount, price);
				market.giveSellOrder(sOrder);
				market.checkTransactions(market.traders);
			}
			
			// give selling order of market price
			else if (queryID == 21) {
				// if buying order queue is not empty
				if (market.getBuyingOrders().size()>0) {
					int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
					double price = market.getBuyingOrders().peek().getPrice();
					double amount = Double.parseDouble(queryData.get(i).split(" ")[2]);
					
					SellingOrder sOrder = new SellingOrder(traderID, amount, price);
					market.giveSellOrder(sOrder);
				}		
				else {
					numberOfInvalidQueries++;
				}
				market.checkTransactions(market.traders);
			}
			
			// deposits a certain amount of dollars to wallet
			else if (queryID == 3) {
				int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
				double amount = Double.parseDouble(queryData.get(i).split(" ")[2]);
				double currentDollars = market.traders.get(traderID).getWallet().getDollars();
				
				market.traders.get(traderID).getWallet().setDollars(currentDollars + amount);
				market.checkTransactions(market.traders);
			}
			

			// withdraws a certain amount of dollars from wallet
			else if (queryID == 4) {
				int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
				double amount = Double.parseDouble(queryData.get(i).split(" ")[2]);
				double currentDollars = market.traders.get(traderID).getWallet().getDollars();
				
				if (amount <= currentDollars) {
					market.traders.get(traderID).getWallet().setDollars(currentDollars + amount);
				}
				else {
					numberOfInvalidQueries++;
				}
				market.checkTransactions(market.traders);
			}
			
			// print wallet status
			else if (queryID == 5) {
				int traderID = Integer.parseInt(queryData.get(i).split(" ")[1]);
				double dollars = market.traders.get(traderID).getWallet().getDollars()
									+ market.traders.get(traderID).getWallet().getBlockedDollars();
				double coins = market.traders.get(traderID).getWallet().getCoins()
							 		+ market.traders.get(traderID).getWallet().getBlockedCoins();
				
				out.printf ("Trader %d: %.5f$ %.5fPQ\n", traderID, dollars, coins);

			}
			
			
			// System Queries
			//  gives PQoin rewards to all traders
			else if (queryID == 777) {
				for (Trader t : market.traders) {					
					double coins = t.getWallet().getCoins();
					double reward = myRandom.nextDouble()*10;
					
					t.getWallet().setCoins(coins + reward);
				}
				market.checkTransactions(market.traders);
			}
			
			// makes open market operation
			else if (queryID == 666) {
				double price = Double.parseDouble(queryData.get(i).split(" ")[1]);
				market.makeOpenMarketOperation(price);
			}
			
			// prints the current market size
			else if (queryID == 500) {
				double dollars = 0;
				double coins = 0;
				
				for (BuyingOrder bo : market.getBuyingOrders()) {
					dollars += bo.getAmount() * bo.getPrice();
				}
				
				for (SellingOrder so : market.getSellingOrders()) {
					coins += so.getAmount();
				}
				
				out.printf("Current market size: %.5f %.5f\n", dollars, coins);
			}
			
			// prints number of successful transactions
			else if (queryID == 501) {
				out.println("Number of successful transactions: " + market.getTransactions().size());
			}
			
			// prints the number of invalid queries
			else if (queryID == 502) {
				out.println("Number of invalid queries: " + numberOfInvalidQueries);
			}
			
			// prints the current prices
			else if (queryID == 505) {			
				double cpBuying;
				double cpSelling;
				double cpAverage;
				
				if (market.getBuyingOrders().size()>0 & market.getSellingOrders().size()>0) {
					cpBuying = market.getBuyingOrders().peek().getPrice();
					cpSelling = market.getSellingOrders().peek().getPrice();
					cpAverage = (cpBuying + cpSelling)/2;
				}
				
				else if (market.getBuyingOrders().size()>0 & market.getSellingOrders().size()==0) {
					cpBuying = market.getBuyingOrders().peek().getPrice();
					cpSelling = 0;
					cpAverage = cpSelling;
				}
				
				else if (market.getBuyingOrders().size()==0 & market.getSellingOrders().size()>0) {
					cpBuying = 0;
					cpSelling = market.getSellingOrders().peek().getPrice();
					cpAverage = cpBuying;
				}
				
				else {
					cpBuying = 0;
					cpSelling = 0;
					cpAverage = 0;
				}
				
				out.printf("Current prices: %.5f %.5f %.5f\n", cpBuying, cpSelling, cpAverage);
			}
			
			// prints all traders’ wallet status
			else if (queryID == 555) {
				for (Trader t : market.traders) {
					double dollars = t.getWallet().getDollars() + t.getWallet().getBlockedDollars();
					double coins = t.getWallet().getCoins() + t.getWallet().getBlockedCoins();
		
					out.printf ("Trader %d: %.5f$ %.5fPQ\n", t.getId(), dollars, coins);
				}
			}	
		}
		
		in.close();
		out.close();
		
		numberOfInvalidQueries = 0;
	}
	
}
