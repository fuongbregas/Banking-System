import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable, Comparable<Customer> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1720477447438865116L;
	private String name;
    private String id;	// 3 digits string
    private String pin;	// 4 digits string  
    private ArrayList<Account> act;
    private double total_bal;  // for all accounts

    public Customer() {
        act = new ArrayList<>();
        name = null;
        id = null;
        pin = null;
        total_bal = 0;
    }

    public void cal_total_bal() {
        double total = 0;
        for (int i = 0; i < act.size(); i++) {
            total = total + act.get(i).getBalance();
        }
    }

    public ArrayList<Account> getAcc() {
        return act;
    }

    public void setAcc(Account acc) {
        act.add(acc);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }

    public void setName(String Name) {
        name = Name;
    }

    public void setId(String Id) {
        id = Id;
    }

    public void setPin(String Pin) {
        pin = Pin;
    }

    public double getBalance() {
        return total_bal;
    }

    public void setBalance(double newBal) {
        total_bal = newBal;
    }

    @Override
    public int compareTo(Customer other) {
        return name.compareTo(other.name);
    }
}