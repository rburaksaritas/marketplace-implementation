package elements;

/**
 * Trader class for the traders of market. <br>
 * Each trader has ID and their own wallet which can store their dollars and PQoins.
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Trader {

	/**
	 * Trader's ID.
	 */
	private final int id;
	/**
	 * Trader's wallet which can store their dollars and PQoins.
	 */
	private Wallet wallet;
	/**
	 * Keeps track of number of users. <br>
	 * Also used for assigning traders' ID since there is no ID parameter in the constructor.
	 */
	public static int numberOfUsers = 0;

	/**
	 * Constructor of Trader class.
	 * @param dollars
	 * @param coins
	 */
	public Trader(double dollars, double coins) {
		this.wallet = new Wallet(dollars,coins);
		this.id = numberOfUsers;
		numberOfUsers++;
	}
	
	/**
	 * Sell method which adds the income from the transaction to the trader's wallet. <br>
	 * Also removes the sold amount of PQoins from the wallet.
	 * @param amount
	 * @param price
	 * @param market
	 * @return 1 when the selling is completed
	 */
	public int sell(double amount, double price, Market market) {
		
		double income = amount * price * (1.00 - (double)(market.getFee()/1000.00));
		
		this.wallet.setDollars(this.wallet.getDollars() + income);
		this.wallet.setBlockedCoins(this.wallet.getBlockedCoins() - amount);
		
		return 1;
	}
	/**
	 * Sell method which adds the PQoins from the transaction to the trader's wallet. <br>
	 * Also removes the paid amount of dollars from the wallet. 
	 * @param amount
	 * @param price
	 * @param market
	 * @return 1 when the buying is completed
	 */
	public int buy(double amount, double price, Market market) {
		
		double spent = amount * price;
		
		this.wallet.setBlockedDollars(this.wallet.getBlockedDollars() - spent);
		this.wallet.setCoins(this.wallet.getCoins() + amount);
		
		return 1;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}

	


}
