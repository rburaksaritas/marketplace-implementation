package elements;

/**
 * BuyingOrder class contains information about the buying order. 
 * @author Ramazan Burak Saritas
 *
 */
public class BuyingOrder extends Order implements Comparable<BuyingOrder>{

	public BuyingOrder(int traderID, double amount, double price) {
		super(traderID, amount, price);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Custom compareTo method for the BuyingOrder class. <br>
	 * <br>
	 * Compares the buyingOrder objects' prices, <br>
	 * if they are equal then compares their amounts, <br>
	 * and if they are both equal compares their traderIDs.  
	 */
	@Override
	public int compareTo(BuyingOrder other) {
		if (this.getPrice() != other.getPrice()) {
			if (this.getPrice() < other.getPrice()) {
				return 1;
			}
			else return -1;
		}
		else if (this.getAmount() != other.getAmount()) {
			if (this.getAmount() > other.getAmount()) {
				return -1;
			}
			else return 1;
		}
		else {
			if (this.getTraderID() < other.getTraderID()) {
				return -1;
			}
			else {
				return 1;
			}
		}
	}

}
