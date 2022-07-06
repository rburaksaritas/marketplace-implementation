package elements;

import java.util.ArrayList;
import java.util.PriorityQueue;

import executable.Main;

/**
 * Market class which contains market operation methods such as giving buy/sell order, checking orders and <br>
 * executing transaction if there is an overlap, and making open market operation. 
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Market {
	
	/**
	 * Market's fee rate. Seller pays the fee for every transaction.
	 */
	private int fee;

	/**
	 * Constructor of market class.
	 * 
	 * @param fee the fee rate for the seller to pay.
	 */
	public Market(int fee) {
		this.fee = fee;
	}
	
	/**
	 * Keeps track of selling orders given by traders.
	 */
	private PriorityQueue<SellingOrder> sellingOrders = new PriorityQueue<SellingOrder>();
	/**
	 * Keeps track of buying orders given by traders.
	 */
	private PriorityQueue<BuyingOrder> buyingOrders = new PriorityQueue<BuyingOrder>();
	/**
	 * Keeps track of executed transactions. Contains the relevant buy order and sell order objects.
	 */
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	/**
	 * Keeps track of traders.
	 */
	public ArrayList<Trader> traders = new ArrayList<Trader>();
	
	/**
	 * Gives selling order with given order object's parameters<br>
	 * if the seller has sufficient PQoins.
	 *  
	 * @param order
	 */
	public void giveSellOrder(SellingOrder order) {
		
		if (order.getAmount() <= traders.get(order.getTraderID()).getWallet().getCoins()) {
		
		sellingOrders.add(order);
		
		double sellersCoins = traders.get(order.getTraderID()).getWallet().getCoins();
		double sellersBlockedCoins = traders.get(order.getTraderID()).getWallet().getBlockedCoins();
		double coinsToBlock = order.getAmount();
		
		traders.get(order.getTraderID()).getWallet().setCoins(sellersCoins - coinsToBlock);
		traders.get(order.getTraderID()).getWallet().setBlockedCoins(sellersBlockedCoins + coinsToBlock);
		
		}
		
		else {
			Main.numberOfInvalidQueries++;
		}
	}
	
	/**
	 * Gives buying order with given order object's parameters<br>
	 * if the buyer has sufficient Dollars.
	 * 
	 * @param order
	 */
	public void giveBuyOrder(BuyingOrder order) {
		
		if (order.getPrice()*order.getAmount() <= traders.get(order.getTraderID()).getWallet().getDollars()) {
		
		buyingOrders.add(order);
		
		double buyersDollars = traders.get(order.getTraderID()).getWallet().getDollars();
		double buyersBlockedDollars = traders.get(order.getTraderID()).getWallet().getBlockedDollars();
		double dollarsToBlock = order.getAmount() * order.getPrice();
		
		traders.get(order.getTraderID()).getWallet().setDollars(buyersDollars - dollarsToBlock);
		traders.get(order.getTraderID()).getWallet().setBlockedDollars(buyersBlockedDollars + dollarsToBlock);
		
		}
		
		else {
			Main.numberOfInvalidQueries++;
		}
	}
	
	/**
	 * Trader 0 (The System) gives buying or selling orders for setting
	 * the current price of PQoin to the given price. <br>
	 * The market tries to converge as much as possible.
	 * 
	 * @param price
	 */
	public void makeOpenMarketOperation(double price) {
		
		while (buyingOrders.size() > 0) {
			if (buyingOrders.peek().getPrice() >= price) {
				SellingOrder s = new SellingOrder(0, buyingOrders.peek().getAmount(), buyingOrders.peek().getPrice());
				traders.get(0).getWallet().setCoins(buyingOrders.peek().getAmount());
				giveSellOrder(s);
				checkTransactions(traders);			
			}
			else break;
		}
		
		while (sellingOrders.size() > 0) {
			if (sellingOrders.peek().getPrice() <= price) {
				BuyingOrder b = new BuyingOrder(0, sellingOrders.peek().getAmount(), sellingOrders.peek().getPrice());
				traders.get(0).getWallet().setDollars(sellingOrders.peek().getAmount()*sellingOrders.peek().getPrice());
				giveBuyOrder(b);
				checkTransactions(traders);
			} 
			else break;
		}
	}
	
	/**
	 * Checks buyingOrders and sellingOrders queues and executes transaction if the <br>
	 * prices are equal or buying order's price is higher than selling order's price.
	 * 
	 * @param traders
	 */
	public void checkTransactions(ArrayList<Trader> traders) {
		// CHECK TRANSACTIONS
		while (buyingOrders.size() > 0 && sellingOrders.size() > 0) {
			if (buyingOrders.peek().getPrice() >= sellingOrders.peek().getPrice()) {
				
				BuyingOrder b = buyingOrders.poll();
				SellingOrder s = sellingOrders.poll();
				
				// return the extra dollars blocked for the transaction to the buyers wallet
				double dollarsToReturn = (b.getPrice() - s.getPrice()) * s.getAmount();
				double buyersDollars = traders.get(b.getTraderID()).getWallet().getDollars();
				double buyersBlockedDollars = traders.get(b.getTraderID()).getWallet().getBlockedDollars();
				traders.get(b.getTraderID()).getWallet().setDollars(buyersDollars + dollarsToReturn);
				traders.get(b.getTraderID()).getWallet().setBlockedDollars(buyersBlockedDollars - dollarsToReturn);
		
				// if amounts are equal
				if (b.getAmount() == s.getAmount()) {
					traders.get(b.getTraderID()).buy(b.getAmount(), s.getPrice(), this);
					traders.get(s.getTraderID()).sell(s.getAmount(), s.getPrice(), this);
					
				}
				// if demanded amount is greater than supplied amount
				else if (b.getAmount() > s.getAmount()) {
					
					// remaining amount of demanded buying amount after the transaction
					double newOrdersAmount = b.getAmount() - s.getAmount();
					
					// new buying order with the remaining demanded buying amount
					BuyingOrder newOrder = new BuyingOrder(b.getTraderID(), newOrdersAmount, b.getPrice());
					
					// setting the current transaction's buying amount to the available selling amount
					b.setAmount(s.getAmount());
					
					// transaction is being executed
					traders.get(b.getTraderID()).buy(b.getAmount(), s.getPrice(), this);
					traders.get(s.getTraderID()).sell(s.getAmount(), s.getPrice(), this);
				
				// adding new buying order to queue
				buyingOrders.add(newOrder);
		
				}
				// if demanded amount is less than supplied amount
				else {
					
					//remaining amount of supplied amount after the transaction
					double newOrdersAmount = s.getAmount() - b.getAmount();
					
					// new selling order with remaining supplied amount
					SellingOrder newOrder = new SellingOrder(s.getTraderID(), newOrdersAmount, s.getPrice());
					
					// setting the current transaction's selling amount to the available demanded amount
					s.setAmount(b.getAmount());
					
					// transaction is being executed
					traders.get(b.getTraderID()).buy(b.getAmount(), s.getPrice(), this);
					traders.get(s.getTraderID()).sell(s.getAmount(), s.getPrice(), this);
				
				// adding the new selling order to queue	
				sellingOrders.add(newOrder);
				}
				
			// adding the transaction to the transaction history
			Transaction t = new Transaction(b, s);
			transactions.add(t);
			}
			else break;
		}
	}
	
	/**
	 * @return the fee
	 */
	public int getFee() {
		return fee;
	}

	/**
	 * @return the sellingOrders
	 */
	public PriorityQueue<SellingOrder> getSellingOrders() {
		return sellingOrders;
	}

	/**
	 * @return the buyingOrders
	 */
	public PriorityQueue<BuyingOrder> getBuyingOrders() {
		return buyingOrders;
	}

	/**
	 * @return the transactions
	 */
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	
}
