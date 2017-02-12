import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Atm extends JFrame {

	private ArrayList<Customer> cust;
	private int starting_account_number;
	private String admin_pin;
	private double interest_rate;
	private int transaction_counter;
	private int starting_customer_number;
	final int orderName = 1;
	final int orderBalance = 2;
	final int sameOwner = 3;
	final int timeOrder = 4;
	final int out = 0;
	final String dataFile = "p1.dat";
	final int checking = 1;
	final int saving = 2;
	private File p1;

	private JFrame frame;
	private File logFile;
	private String logData = "p2.log";
	FileInputStream from;

	// For Log
	private ArrayList<LogEntry> logList;
	private int transID;

	public Atm() // constructor
	{
		cust = new ArrayList<>();
		starting_account_number = 1001;// Ex: 2nd acc will be 1002
		starting_customer_number = 101;// generate new cust, increment, Ex: 2nd
										// cust will be 102
		admin_pin = "abcd";
		interest_rate = 5;
		transaction_counter = 0;
		p1 = new File(dataFile);

		// For log
		logList = new ArrayList<>();
		transID = 1;
		from = null;
		logFile = new File(logData);

	}

	public void admin() {

		// Display this frame after login
		JFrame frame2 = new JFrame("Choose...");
		frame2.setSize(1000, 1000);
		JPanel panel2 = new JPanel();
		JButton orderName = new JButton("Show order by customer NAME");
		JButton orderBalance = new JButton("Show order by BALANCE");
		JButton sameOwner = new JButton("Show accounts belong to 1 customer");
		JButton orderTime = new JButton("Show order by TIME CHANGES");
		JButton cancel = new JButton("Cancel");
		JTextArea display1 = new JTextArea();

		panel2.add(orderName);
		panel2.add(orderBalance);
		panel2.add(sameOwner);
		panel2.add(orderTime);
		panel2.add(cancel);
		panel2.add(display1);
		frame2.add(panel2);

		frame2.setVisible(true);
		String format = "%-20s %-20s %-20s %-20s %20s %n";

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame2.dispose();
			}
		});

		orderName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display1.setText("");
				Collections.sort(cust);
				String headline = String.format(format, "[Customer Name]", "[Customer ID]", "[Account#]", "[Pin#]",
						"[Current Balance]");
				display1.append(headline);
				for (int i = 0; i < cust.size(); i++) {
					for (int j = 0; j < cust.get(i).getAcc().size(); j++) {
						display1.append(String.format("%-44s %-24s %-20s %-23s %20s %n", cust.get(i).getName(),
								cust.get(i).getId(), cust.get(i).getAcc().get(j).getNumber(), cust.get(i).getPin(),
								cust.get(i).getAcc().get(j).getBalance()));
					}
				}
			}
		});

		orderBalance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display1.setText("");
				String headline = String.format(format, "[Customer Name]", "[Customer ID]", "[Account#]", "[Pin#]",
						"[Current Balance]");
				display1.append(headline);
				ArrayList<Account> accToCompare = new ArrayList<>();
				for (int i = 0; i < cust.size(); i++) {
					for (int j = 0; j < cust.get(i).getAcc().size(); j++) {
						accToCompare.add(cust.get(i).getAcc().get(j));
					}
				}
				Collections.sort(accToCompare, Account.balanceComparator);
				for (int i = 0; i < accToCompare.size(); i++) {
					for (int j = 0; j < cust.size(); j++) {
						for (int k = 0; k < cust.get(j).getAcc().size(); k++) {
							if (accToCompare.get(i).getNumber().equals(cust.get(j).getAcc().get(k).getNumber())) {
								display1.append(String.format("%-44s %-24s %-20s %-23s %20s %n", cust.get(j).getName(),
										cust.get(j).getId(), accToCompare.get(i).getNumber(), cust.get(j).getPin(),
										accToCompare.get(i).getBalance()));
							}
						}
					}
				}
			}
		});

		sameOwner.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display1.setText("");
				String result = JOptionPane.showInputDialog(frame2, "Enter Customer ID:");
				String headline = String.format(format, "[Customer Name]", "[Customer ID]", "[Account#]", "[Pin#]",
						"[Current Balance]");
				Collections.sort(cust);
				for (int i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(result)) {
						if (cust.get(i).getAcc().size() > 1) {
							display1.append(headline);
							for (int j = 0; j < cust.get(i).getAcc().size(); j++) {
								display1.append(String.format("%-44s %-24s %-20s %-23s %20s %n", cust.get(i).getName(),
										cust.get(i).getId(), cust.get(i).getAcc().get(j).getNumber(),
										cust.get(i).getPin(), cust.get(i).getAcc().get(j).getBalance()));

							}
						} else {
							JOptionPane.showMessageDialog(frame2, "This Customer only has 1 account");
						}
					}
				}
			}
		});
		orderTime.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display1.setText("");
				String headline = String.format("%-20s %-20s %-20s %-20s %38s %n", "[Transaction]", "[Customer ID]",
						"[Account#]", "[Amount]", "[Time Change]");
				display1.append(headline);
				Collections.sort(logList);
				for (int i = 0; i < logList.size(); i++) {
					display1.append(String.format("%-39s %-24s %-20s %-23s %20s %n", logList.get(i).getTransID(),
							logList.get(i).getCustomerID(), logList.get(i).getAccNumber(), logList.get(i).getAmount(),
							logList.get(i).getTime()));
				}
			}
		});

	}

	/**
	 * A new Customer is born
	 */
	public void create() {
		final Customer newCustomer = new Customer();
		newCustomer.setId(Integer.toString(starting_customer_number));
		frame = new JFrame("Create Customer");
		JLabel CustomerName = new JLabel("Enter Your Name: ");
		final JTextField name = new JTextField(16);
		JLabel EnterPin = new JLabel("Please Enter Your Pin: ");
		final JTextField pinNumber = new JTextField(4);
		pinNumber.setDocument(new JTextFieldLimit(4));

		JButton create = new JButton("Create Customer");

		JPanel panel = new JPanel();
		panel.add(CustomerName);
		panel.add(name);
		panel.add(EnterPin);
		panel.add(pinNumber);

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String getPin = pinNumber.getText();
				if (pinNumber.getText().contains(" ")) {
					JOptionPane.showMessageDialog(frame, "Pin cannot contain empty space");

				} else if (pinNumber.getText().length() < 4) {
					JOptionPane.showMessageDialog(frame, "PIN must have 4 digits");
				} else {
					JOptionPane.showMessageDialog(frame, "Name: " + name.getText() + "\n" + "Pin Number: " + getPin
							+ "\n" + "Customer ID: " + (starting_customer_number - 1) + "\n");
					newCustomer.setName(name.getText());
					newCustomer.setPin(getPin);
				}
			}
		});
		panel.add(create);

		frame.add(panel);
		frame.setSize(300, 300);

		frame.setVisible(true);

		cust.add(newCustomer);
		starting_customer_number++;
	}

	/**
	 * Create an Account
	 */
	public void open() {

		JFrame frame2 = new JFrame();
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JLabel enterType = new JLabel("(S)aving or (C)hecking?");
		panel2.add(enterType);
		JTextField type = new JTextField(2);
		// type.setDocument(new JTextFieldLimit(1));
		panel2.add(type);

		JButton OKButton = new JButton("OK");
		panel2.add(OKButton);

		frame2.add(panel2);
		frame2.toFront();
		frame2.setVisible(true);
		
		for (int i = 0; i < cust.size(); i++) {
			ArrayList<Account> accs = new ArrayList<>();
			Account acc = new Account(Integer.toString(starting_account_number++));
			Customer tempCust = cust.get(i);
			OKButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					int j;
					// String AccType =
					// type.getText().toLowerCase();
					if (!type.getText().toLowerCase().equals("s") && !type.getText().toLowerCase().equals("c")) {
						JOptionPane.showMessageDialog(frame2, "Input not Satisfied");
					} else if (type.getText().toLowerCase().equals("s")) {
						acc.activate();
						accs.add(acc);
						tempCust.setAcc(acc);
						j = accs.size() - 1;
						accs.get(j).setSaving();
						int input = JOptionPane.showOptionDialog(frame2,
								"Your new " + accs.get(j).getType() + "account Number: " + accs.get(j).getNumber()
										+ ", " + "Balance: $" + accs.get(j).getBalance() + " and Status: "
										+ accs.get(j).status(),
								" ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
						accs.get(j).setCalendar(Calendar.getInstance());
						JOptionPane.showMessageDialog(frame, "Latest time change: " + accs.get(j).getCalendar());
						if (input == JOptionPane.OK_OPTION) {
							frame2.setVisible(false);
							frame.setVisible(false);
						}

						writeFile();

					} else {
						acc.activate();
						accs.add(acc);
						tempCust.setAcc(acc);
						j = accs.size() - 1;
						accs.get(j).setChecking();
						int input = JOptionPane.showOptionDialog(frame2,
								"Your new " + accs.get(j).getType() + "account Number: " + accs.get(j).getNumber()
										+ ", " + "Balance: $" + accs.get(j).getBalance() + " and Status: "
										+ accs.get(j).status(),
								" ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
						accs.get(j).setCalendar(Calendar.getInstance());
						JOptionPane.showMessageDialog(frame, "Latest time change: " + accs.get(j).getCalendar());
						if (input == JOptionPane.OK_OPTION) {
							frame2.setVisible(false);
						}
						writeFile();
					}
				}
			});
		}
	}

	/**
	 * Transfer Money from an Account to another Account
	 */
	public void transfer() {
		// Create 2 log object, 1 for withdraw, 1 for transfer
		LogEntry logWithdraw = new LogEntry();
		LogEntry logTransfer = new LogEntry();

		// Login Screen
		frame = new JFrame("Login");
		frame.setSize(300, 300);
		JPanel panel1 = new JPanel();
		JLabel enterID = new JLabel("Enter Customer ID");
		JTextField custID = new JTextField(4);
		JLabel enterPIN = new JLabel("Enter your PIN");
		JTextField PIN = new JTextField(4);
		JButton loginButton = new JButton("Log In");
		panel1.add(enterID);
		panel1.add(custID);
		panel1.add(enterPIN);
		panel1.add(PIN);
		panel1.add(loginButton);
		frame.add(panel1);
		frame.setVisible(true);

		// Screen after login

		JFrame frame2 = new JFrame("Transfer");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JLabel enterAcc1 = new JLabel("Enter the Account you WITHDRAW money FROM");
		JTextField acc1 = new JTextField(4);
		JLabel enterAcc2 = new JLabel("Enter the Account you TRANSFER money TO");
		JTextField acc2 = new JTextField(4);
		JLabel enterAmount = new JLabel("Enter Amount of Money");
		JTextField amount = new JTextField(4);
		JButton confirm = new JButton("Confirm");
		panel2.add(enterAcc1);
		panel2.add(acc1);
		panel2.add(enterAcc2);
		panel2.add(acc2);
		panel2.add(enterAmount);
		panel2.add(amount);
		panel2.add(confirm);
		frame2.add(panel2);

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// After Login
				for (int i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(custID.getText()) && i < cust.size()) {
						// Set both Log Object with the same Customer ID
						logWithdraw.setCustomerID(cust.get(i).getId());
						logTransfer.setCustomerID(cust.get(i).getId());

						ArrayList<Account> listOfAcc = cust.get(i).getAcc();
						if (cust.get(i).getPin().equals(PIN.getText())) {
							frame2.setVisible(true);
							confirm.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									// Enter Information needed
									for (int j = 0; j < listOfAcc.size(); j++) {
										if (listOfAcc.get(j).getNumber().equals(acc1.getText())) {
											String first = acc1.getText();
											String second = acc2.getText();
											if (second.equals(first)) {
												JOptionPane.showMessageDialog(frame2,
														"Account can't be transferred to itself!");
											} else {
												Double moneyTransfer = Double.parseDouble(amount.getText());
												// Set both log with
												// moneyTransfer
												logWithdraw.setAmount(moneyTransfer * -1);
												logTransfer.setAmount(moneyTransfer);

												for (int k = 0; k < listOfAcc.size(); k++) {
													if (listOfAcc.get(k).getNumber().equals(second)) {
														if (listOfAcc.get(j).getBalance() >= moneyTransfer) {
															// Set balance for
															// first account,
															// minus money
															double firstNewBalance = listOfAcc.get(j).getBalance()
																	- moneyTransfer;
															listOfAcc.get(j).setBalance(firstNewBalance);

															// Set balance for
															// second account,
															// add money
															double secondNewBalance = listOfAcc.get(k).getBalance()
																	+ moneyTransfer;
															listOfAcc.get(k).setBalance(secondNewBalance);

															// Display balance
															// of each account
															JOptionPane.showMessageDialog(frame2,
																	"New balance of Account "
																			+ listOfAcc.get(j).getNumber() + ": $"
																			+ listOfAcc.get(j).getBalance());
															JOptionPane.showMessageDialog(frame2,
																	"New balance of Account "
																			+ listOfAcc.get(k).getNumber() + ": $"
																			+ listOfAcc.get(k).getBalance());

															// Set both accounts
															// with the same
															// time
															Calendar now = Calendar.getInstance();
															listOfAcc.get(j).setCalendar(now);
															listOfAcc.get(k).setCalendar(now);

															// Set info Log for
															// both log Object
															logWithdraw.setAccNumber(acc1.getText());
															logWithdraw.setTransID(transID++);
															logWithdraw.setCalendar(Calendar.getInstance());
															logList.add(logWithdraw);

															logTransfer.setAccNumber(acc2.getText());
															logTransfer.setTransID(transID++);
															logTransfer.setCalendar(Calendar.getInstance());
															logList.add(logTransfer);// add
																						// to
																						// logList

															Account firstAcc = listOfAcc.get(j);
															Account secondAcc = listOfAcc.get(k);
															if (firstAcc.getType().equals("Saving ")
																	|| secondAcc.getType().equals("Saving ")
																	|| (firstAcc.getType().equals("Saving ")
																			&& secondAcc.getType().equals("Saving "))) {
																transaction_counter++;
															}
															if (transaction_counter >= 5) {
																if (listOfAcc.get(j).getType().equals("Saving ")) {
																	double interest = interest_rate
																			* listOfAcc.get(j).getBalance() / 100;
																	listOfAcc.get(j).setBalance(
																			listOfAcc.get(j).getBalance() + interest);

																	LogEntry logWInterest = new LogEntry();
																	logWInterest.setCustomerID(custID.getText());
																	logWInterest.setTransID(0);
																	logWInterest.setAccNumber(acc1.getText());
																	logWInterest.setAmount(interest);
																	logWInterest.setCalendar(Calendar.getInstance());
																	logList.add(logWInterest);

																	transaction_counter = 0;
																	writeLog();
																}
																if (listOfAcc.get(k).getType().equals("Saving ")) {
																	double interest = interest_rate
																			* listOfAcc.get(k).getBalance() / 100;
																	listOfAcc.get(k).setBalance(
																			listOfAcc.get(k).getBalance() + interest);

																	LogEntry logTInterest = new LogEntry();
																	logTInterest.setCustomerID(custID.getText());
																	logTInterest.setTransID(0);
																	logTInterest.setAccNumber(acc2.getText());
																	logTInterest.setAmount(interest);
																	logTInterest.setCalendar(Calendar.getInstance());
																	logList.add(logTInterest);

																	transaction_counter = 0;
																	writeLog();
																}
															}
															writeFile();
															writeLog();
														} else {
															JOptionPane.showMessageDialog(frame2,
																	listOfAcc.get(j).getNumber()
																			+ " does not have enough fund!");
														}
													}
												}
											}
										}
									}
								}
							});
						} else {
							JOptionPane.showMessageDialog(frame, "Wrong PIN");
						}
					}
				}
			}
		});
	}

	/**
	 * Withdraw Money from a Specific Account
	 */
	public void withdraw() {
		// Create Log object every time
		LogEntry log = new LogEntry();

		frame = new JFrame("Deposit");
		frame.setSize(300, 300);
		JPanel panel1 = new JPanel();
		JLabel enterID = new JLabel("Enter Customer ID");
		JTextField custID = new JTextField(4);
		JLabel enterPIN = new JLabel("Enter your PIN");
		JTextField PIN = new JTextField(4);
		JButton loginButton = new JButton("Log In");
		panel1.add(enterID);
		panel1.add(custID);
		panel1.add(enterPIN);
		panel1.add(PIN);
		panel1.add(loginButton);
		frame.add(panel1);
		frame.setVisible(true);

		// After login, show this screen
		JFrame frame2 = new JFrame("Withdraw Screen");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JLabel enterAcc1 = new JLabel("Enter the Account you want to WITHDRAW ");
		JTextField acc1 = new JTextField(10);

		JLabel amountMoney = new JLabel("How much money? ");
		JTextField amount = new JTextField(10);
		JButton confirm = new JButton("Confirm");
		panel2.add(enterAcc1);
		panel2.add(acc1);
		panel2.add(amountMoney);
		panel2.add(amount);
		panel2.add(confirm);
		frame2.add(panel2);

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// After login
				int i = 0;
				for (i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(custID.getText())) {
						log.setCustomerID(cust.get(i).getId());

						// Get the list of accounts from the customer
						ArrayList<Account> listOfAcc = cust.get(i).getAcc();
						if (cust.get(i).getPin().equals(PIN.getText())) {
							frame2.setVisible(true);
							confirm.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									// After confirming
									int j = 0;
									for (j = 0; j < listOfAcc.size(); j++) {
										if (listOfAcc.get(j).getNumber().equals(acc1.getText())
												&& j < listOfAcc.size()) {
											Double moneyOut = Double.parseDouble(amount.getText());
											if (moneyOut <= listOfAcc.get(j).getBalance()) {
												Double newBal = listOfAcc.get(j).getBalance() - moneyOut;
												listOfAcc.get(j).setBalance(newBal);
												listOfAcc.get(j).setCalendar(Calendar.getInstance());

												// Take Log info
												log.setAccNumber(listOfAcc.get(j).getNumber());
												log.setTransID(transID++);
												log.setAmount(moneyOut * -1);
												log.setCalendar(Calendar.getInstance());

												// Add to the log arrayList
												logList.add(log);

												Account withdrawAcc = listOfAcc.get(j);
												if (withdrawAcc.getType().equals("Saving ")) {
													transaction_counter++;
												}
												if (transaction_counter >= 5) {
													if (listOfAcc.get(j).getType().equals("Saving ")) {
														double interest = listOfAcc.get(j).getBalance() * interest_rate
																/ 100;
														listOfAcc.get(j)
																.setBalance(interest + listOfAcc.get(j).getBalance());

														// Log when account get
														// interest
														LogEntry logInterest = new LogEntry();

														logInterest.setCustomerID(custID.getText());
														logInterest.setAccNumber(listOfAcc.get(j).getNumber());
														logInterest.setTransID(0);
														logInterest.setAmount(interest);
														logInterest.setCalendar(Calendar.getInstance());

														// Add to logList
														logList.add(logInterest);

														transaction_counter = 0;
														writeLog();
													}
												}
												int input = JOptionPane.showOptionDialog(frame2,
														"$" + listOfAcc.get(j).getBalance()
																+ " is the new balance of Account "
																+ listOfAcc.get(j).getNumber(),
														" ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
														null, null, null);
												JOptionPane.showMessageDialog(frame2,
														"Latest time change: " + listOfAcc.get(j).getCalendar());
												if (input == JOptionPane.OK_OPTION) {
													frame2.setVisible(false);
													frame.setVisible(false);
												}
												writeFile();
												writeLog();
											} else {
												JOptionPane.showMessageDialog(frame2,
														"You don't have enough money to withdraw!");
											}
										}
										if (j >= listOfAcc.size()) {
											JOptionPane.showMessageDialog(frame2, "Can't find the Account");
										}
									}

								}
							});
						} else {
							JOptionPane.showMessageDialog(frame, "Wrong PIN");
						}
					} else if (i > cust.size()) {
						JOptionPane.showMessageDialog(frame, "Wrong ID");
					}
				}
			}
		});
	}

	/**
	 * Deposit money to a specific Account
	 */
	public void deposit() {
		LogEntry log = new LogEntry();

		// Create first frame
		frame = new JFrame("Deposit");
		frame.setSize(300, 300);
		JPanel panel1 = new JPanel();
		JLabel enterID = new JLabel("Enter Customer ID");
		JTextField custID = new JTextField(4);
		JLabel enterPIN = new JLabel("Enter your PIN");
		JTextField PIN = new JTextField(4);
		JButton loginButton = new JButton("Log In");
		panel1.add(enterID);
		panel1.add(custID);
		panel1.add(enterPIN);
		panel1.add(PIN);
		panel1.add(loginButton);
		frame.add(panel1);

		// After login, show this screen
		JFrame frame2 = new JFrame("Deposit Screen");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JLabel enterAcc1 = new JLabel("Enter the Account you want to DEPOSIT ");
		JTextField acc1 = new JTextField(10);

		JLabel amountMoney = new JLabel("How much money? ");
		JTextField amount = new JTextField(10);
		JButton confirm = new JButton("Confirm");
		panel2.add(enterAcc1);
		panel2.add(acc1);
		panel2.add(amountMoney);
		panel2.add(amount);
		panel2.add(confirm);
		frame2.add(panel2);

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate user input
				for (int i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(custID.getText()) && i < cust.size()) {
						// Get Customer ID and Add to the Logs
						log.setCustomerID(cust.get(i).getId());

						// Get the list of accounts from the customer
						ArrayList<Account> listOfAcc = cust.get(i).getAcc();
						if (cust.get(i).getPin().equals(PIN.getText())) {
							frame2.setVisible(true);
							confirm.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									// After confirming
									for (int j = 0; j < listOfAcc.size(); j++) {
										if (listOfAcc.get(j).getNumber().equals(acc1.getText())
												&& j < listOfAcc.size()) {
											Double moneyIn = Double.parseDouble(amount.getText());
											Double newBal = listOfAcc.get(j).getBalance() + moneyIn;
											listOfAcc.get(j).setBalance(newBal);
											int input = JOptionPane.showOptionDialog(frame2,
													"$" + listOfAcc.get(j).getBalance()
															+ " is the new balance of Account "
															+ listOfAcc.get(j).getNumber(),
													" ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
													null, null);
											JOptionPane.showMessageDialog(frame2,
													"Latest time change: " + listOfAcc.get(j).getCalendar());
											if (input == JOptionPane.OK_OPTION) {
												frame2.setVisible(false);
												frame.setVisible(false);
											}
											// Take Log info
											log.setAccNumber(listOfAcc.get(j).getNumber());
											log.setTransID(transID++);
											log.setAmount(moneyIn);
											log.setCalendar(Calendar.getInstance());

											// Add to the log arrayList
											logList.add(log);

											listOfAcc.get(j).setCalendar(Calendar.getInstance());
											Account depositAcc = listOfAcc.get(j);
											if (depositAcc.getType().equals("Saving ")) {
												transaction_counter++;
											}
											if (transaction_counter >= 5) {
												if (listOfAcc.get(j).getType().equals("Saving ")) {
													double interest = listOfAcc.get(j).getBalance() * interest_rate
															/ 100;
													listOfAcc.get(j)
															.setBalance(interest + listOfAcc.get(j).getBalance());

													// Log when account get
													// interest
													LogEntry logInterest = new LogEntry();

													logInterest.setCustomerID(custID.getText());
													logInterest.setAccNumber(listOfAcc.get(j).getNumber());
													logInterest.setTransID(0);
													logInterest.setAmount(interest);
													logInterest.setCalendar(Calendar.getInstance());

													// Add to logList
													logList.add(logInterest);

													transaction_counter = 0;
													writeLog();
												}
											}
											writeFile();
											writeLog();
										}
										if (j >= listOfAcc.size()) {
											JOptionPane.showMessageDialog(frame2, "Can't find the Account");
										}
									}
								}
							});
						} else {
							JOptionPane.showMessageDialog(frame, "Wrong PIN");
						}
					}
					if (i >= cust.size()) {
						JOptionPane.showMessageDialog(frame, "Wrong ID");
					}
				}
			}
		});
		frame.setVisible(true);
	}

	/**
	 * Terminate an Specific Account
	 */
	public void closeAccount() {
		frame = new JFrame("Close Account");
		frame.setSize(300, 300);
		JPanel panel1 = new JPanel();
		JLabel enterID = new JLabel("Enter Customer ID");
		JTextField custID = new JTextField(4);
		JLabel enterPIN = new JLabel("Enter PIN");
		JTextField PIN = new JTextField(4);
		JButton logInButton = new JButton("Log In");
		panel1.add(enterID);
		panel1.add(custID);
		panel1.add(enterPIN);
		panel1.add(PIN);
		panel1.add(logInButton);
		frame.add(panel1);

		// Second frame after login
		JFrame frame2 = new JFrame("Choose Account");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JLabel chooseAccount = new JLabel("Choose the account you want to ternminate");
		JTextField accountChoice = new JTextField(4);
		JButton confirm = new JButton("Terminate");
		panel2.add(chooseAccount);
		panel2.add(accountChoice);
		panel2.add(confirm);
		frame2.add(panel2);

		logInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// After login
				for (int i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(custID.getText()) && i < cust.size()) {
						ArrayList<Account> listOfAccs = cust.get(i).getAcc();
						if (cust.get(i).getPin().equals(PIN.getText())) {
							frame2.setVisible(true);

							confirm.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									// After pressing the Terminate button
									for (int j = 0; j < listOfAccs.size(); j++) {
										if (listOfAccs.get(j).getNumber().equals(accountChoice.getText())
												&& j < listOfAccs.size()) {
											String accRemovedNum = listOfAccs.get(j).getNumber();
											listOfAccs.get(j).clear();
											listOfAccs.get(j).deactivate();
											listOfAccs.get(j).setCalendar(Calendar.getInstance());
											writeFile();
											int input = JOptionPane.showOptionDialog(frame2,
													"Account " + accRemovedNum + " has been removed from the system.",
													" ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
													null, null);
											JOptionPane.showMessageDialog(frame2,
													"Latest time change: " + listOfAccs.get(j).getCalendar());
											if (input == JOptionPane.OK_OPTION) {
												frame2.setVisible(false);
												frame.setVisible(false);
											}
										}
										if (j > listOfAccs.size()) {
											JOptionPane.showMessageDialog(frame2, "Can't find the Account");
										}
									}
								}
							});
						} else {
							JOptionPane.showMessageDialog(frame, "Wrong PIN");
						}
					}
					if (i > cust.size()) {
						JOptionPane.showMessageDialog(frame, "Can't find the account");
					}
				}
			}
		});
		frame.setVisible(true);
	}

	/**
	 * Show user information
	 */
	public void getBalance() {
		frame = new JFrame("Account Information");
		frame.setSize(300, 300);
		JPanel panel1 = new JPanel();
		JLabel enterID = new JLabel("Enter Customer ID");
		JTextField custID = new JTextField(4);
		JLabel enterPIN = new JLabel("Enter PIN");
		JTextField PIN = new JTextField(4);

		JButton logInButton = new JButton("Log In");
		panel1.add(enterID);
		panel1.add(custID);
		panel1.add(enterPIN);
		panel1.add(PIN);
		panel1.add(logInButton);
		frame.add(panel1);
		frame.setVisible(true);

		JFrame frame2 = new JFrame("Information");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JTextArea display1 = new JTextArea();
		panel2.add(display1);
		frame2.add(panel2);

		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// After login
				for (int i = 0; i < cust.size(); i++) {
					if (cust.get(i).getId().equals(custID.getText()) && i < cust.size()) {
						// Get the list of accounts from the customer
						ArrayList<Account> listOfAcc = cust.get(i).getAcc();
						if (cust.get(i).getPin().equals(PIN.getText())) {
							frame2.setVisible(true);
							display1.setText("");
							Collections.sort(logList);
							Collections.sort(listOfAcc, Account.timeComparator);
							for (int j = 0; j < listOfAcc.size(); j++) {
								JOptionPane.showMessageDialog(frame,
										"Account " + listOfAcc.get(j).getNumber() + "'s Information:" + "\n"
												+ "Balance: " + "$" + listOfAcc.get(j).getBalance() + " \n" + "Status: "
												+ listOfAcc.get(j).status() + "\n" + "Latest time change: "
												+ listOfAcc.get(j).getCalendar() + "\n" + "");
							}
							ArrayList<LogEntry> tempLog = new ArrayList<>();
							for (int k = 0; k < logList.size(); k++) {
								if (logList.get(k).getCustomerID().equals(custID.getText()) && k != logList.size()) {
									tempLog.add(logList.get(k));
								}
							}

							String headline = String.format("%-20s %-20s %-20s %-20s %38s %n", "[Transaction]",
									"[Customer ID]", "[Account#]", "[Amount]", "[Time Change]");
							display1.append(headline);
							for (int m = 0; m < tempLog.size(); m++) {
								display1.append(
										String.format("%-39s %-24s %-20s %-23s %20s %n", tempLog.get(m).getTransID(),
												tempLog.get(m).getCustomerID(), tempLog.get(m).getAccNumber(),
												tempLog.get(m).getAmount(), tempLog.get(m).getTime()));
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Wrong PIN");
						}
					}
					if (i > cust.size()) {
						JOptionPane.showMessageDialog(frame, "Wrong ID");
					}
				}
			}
		});
	}

	public int validate(String ID, String PIN) {

		int n = 0; // This will represent the location in the arrayList the cust
					// is found at

		for (Customer c : cust) {
			// DEBUG System.out.println(n);
			n++;
			if (ID.equals(c.getId())) {
				if (PIN.equals(c.getPin())) {
					System.out.println("PIN Validated");
					return n - 1;
				} else {
					System.out.println("Incorrect PIN!");
					return -1;
				}

			}
		}

		return -1;
	}

	public void writeFile() {
		try {
			FileOutputStream out = new FileOutputStream(p1, true); // Make file
																	// persist
																	// using
																	// append
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(cust);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeLog() {
		try {
			FileOutputStream logOut = new FileOutputStream(logFile, true); // Make
																			// file
																			// persist
																			// using
																			// append
			ObjectOutputStream oos = new ObjectOutputStream(logOut);
			oos.writeObject(logList);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found");

		} catch (Exception ae) {
			ae.printStackTrace();
		}
	}
}
