package elements;

/**
 * Transaction class' objects are used to keep track of transactions. <br>
 * Contains overlapping BuyingOrder and SellingOrder objects.
 * 
 * @author Ramazan Burak Saritas
 *
 */
public class Transaction {

	/**
	 * Selling order of the transaction.
	 */
	private SellingOrder sellingOrder;
	/**
	 * Buying order of the transaction.
	 */
	private BuyingOrder buyingOrder;

	/**
	 * Constructor of Transaction class.
	 * @param o1 buying order
	 * @param o2 selling order
	 */
	public Transaction(BuyingOrder o1, SellingOrder o2) {
		this.buyingOrder = o1;
		this.sellingOrder = o2;
	}

	/**
	 * @return the sellingOrder
	 */
	public SellingOrder getSellingOrder() {
		return sellingOrder;
	}

	/**
	 * @return the buyingOrder
	 */
	public BuyingOrder getBuyingOrder() {
		return buyingOrder;
	}
	
	
}
