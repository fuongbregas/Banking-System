import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogEntry implements Comparable<LogEntry>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1152695549839851430L;
	private int transactionID;
	private String time;
	private String customerID;
	private String accountNumber;
	private double amount;
	
	public LogEntry(){
		transactionID = 0;
		time = null;
		customerID = null;
		accountNumber = null;
		amount = 0.0;
	}
	
	public void setTransID(int transID){
		this.transactionID = transID;
	}	
	public int	getTransID(){
		return transactionID;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	public String getTime(){
		return time;
	}
	
	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}
	public String getCustomerID(){
		return this.customerID;
	}
	
	public void setAccNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}
	public String getAccNumber(){
		return accountNumber;
	}
	
	public void setAmount(double amount){
		this.amount = amount;
	}
	public double getAmount(){
		return amount;
	}
	
	public void setCalendar(Calendar Cal){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        time = sdf.format(Cal.getTime());        
    }
	
	public String getCalendar(){
		return time;
	}
	
	
	public int compareTo(LogEntry other) {
		return -time.compareTo(other.time);
        		
	}
}
