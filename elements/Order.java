package elements;

/**
 * Parent class of the BuyingOrder and SellingOrder classes. Contains fundamental order fields and methods.
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Order {
	
	/**
	 * Amount of the order.
	 */
	private double amount;
	/**
	 * Price of the order.
	 */
	private double price;
	/**
	 * Trader's ID. 
	 */
	private int traderID;

	/**
	 * Construction for the Order class.
	 * @param traderID
	 * @param amount
	 * @param price
	 */
	public Order(int traderID, double amount, double price) {
		this.traderID = traderID;
		this.amount = amount;
		this.price = price;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the traderID
	 */
	public int getTraderID() {
		return traderID;
	}

	/**
	 * @param traderID the traderID to set
	 */
	public void setTraderID(int traderID) {
		this.traderID = traderID;
	}

	
}
