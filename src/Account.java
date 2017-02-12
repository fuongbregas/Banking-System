import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Comparator;

public class Account implements Serializable {
	private String number; // 4 digits string
	private double balance;
	private boolean active;
	private String type;
	private String cal;

	public Account() {
		balance = 0.0;
		active = false;
		number = null;
		type = null;
	}

	public Account(String Number) {
		number = Number;
		active = false;
		balance = 0.0;
		type = null;
	}

	/**
	 * Set Account Number
	 * 
	 * @param Number
	 *            Account Number you want to set
	 */
	public void setAccNum(String Number) {
		number = Number;
	}

	/**
	 * Return the balance of the Account
	 * 
	 * @return balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Set the balance of an Account if Money is In or Out
	 * 
	 * @param money
	 */
	public void setBalance(double money) {
		balance = money;
	}

	/**
	 * Get Account Number
	 * 
	 * @return account number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Activate an Account
	 */
	public void activate() {
		active = true;
	}

	/**
	 * Deactivate an Account
	 */
	public void deactivate() {
		active = false;
	}

	/**
	 * Get the status of an Account
	 * 
	 * @return A String shows that whether the Account is Active or Not
	 */
	public String status() {
		if (active == true)
			return "Account is Active.";
		return "Account is Dead.";
	}

	public double clear() {
		return balance = 0;
	}

	public void setSaving() {
		type = "Saving ";
	}

	public void setChecking() {
		type = "Checking ";
	}

	public String getType() {
		return type;
	}

	public void setCalendar(Calendar Cal) {
		cal = new String();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
		cal = sdf.format(Cal.getTime());

	}

	public String getCalendar() {
		return cal;
	}

	public static Comparator<Account> timeComparator = new Comparator<Account>() {
		@Override
		public int compare(Account acc1, Account acc2) {
			return acc2.getCalendar().compareTo(acc1.getCalendar());
		}
	};

	public static Comparator<Account> balanceComparator = new Comparator<Account>() {
		@Override
		public int compare(Account acc1, Account acc2) {
			if (acc1.getBalance() > acc2.getBalance()) {
				return -1;
			} else if (acc1.getBalance() < acc2.getBalance()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
}
