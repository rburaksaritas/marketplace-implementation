package elements;

/**
 * Wallet class' objects contains dollars & PQoins and <br>
 * blocked dollars & blocked PQoins when they are used <br>
 * in giving buying order or selling order. 
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Wallet {

	/**
	 * Dollars in the wallet.
	 */
	private double dollars = 0;
	/**
	 * PQoins in the wallet.
	 */
	private double coins = 0;
	/**
	 * Blocked dollars for the buying orders.
	 */
	private double blockedDollars = 0;
	/**
	 * Blocked PQoins for the selling orders.
	 */
	private double blockedCoins = 0;

	/**
	 * Constructor of Wallet class.
	 * @param dollars
	 * @param coins
	 */
	public Wallet(double dollars, double coins) {
		
		this.setDollars(dollars);
		this.setCoins(coins);
		
	}

	/**
	 * @return the dollars
	 */
	public double getDollars() {
		return dollars;
	}

	/**
	 * @param dollars the dollars to set
	 */
	public void setDollars(double dollars) {
		this.dollars = dollars;
	}

	/**
	 * @return the coins
	 */
	public double getCoins() {
		return coins;
	}

	/**
	 * @param coins the coins to set
	 */
	public void setCoins(double coins) {
		this.coins = coins;
	}

	/**
	 * @return the blockedDollars
	 */
	public double getBlockedDollars() {
		return blockedDollars;
	}

	/**
	 * @param blockedDollars the blockedDollars to set
	 */
	public void setBlockedDollars(double blockedDollars) {
		this.blockedDollars = blockedDollars;
	}

	/**
	 * @return the blockedCoins
	 */
	public double getBlockedCoins() {
		return blockedCoins;
	}

	/**
	 * @param blockedCoins the blockedCoins to set
	 */
	public void setBlockedCoins(double blockedCoins) {
		this.blockedCoins = blockedCoins;
	}

}
