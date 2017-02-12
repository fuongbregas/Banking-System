import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class p2 extends JFrame {
	public static void main(String[] a) {
		Atm atm = new Atm();
		JFrame mainFrame = new JFrame("Banking System");
		mainFrame.setSize(300, 300);
		mainFrame.setVisible(true);

		JPanel mainPanel = new JPanel();
		JButton create = new JButton("Create Customer");
		JButton logIn = new JButton("Already Customer");
		JLabel enterID = new JLabel("Enter ID");
		JTextField iD = new JTextField(4); 
		JLabel enterPIN = new JLabel("Enter Pin");
		JTextField PIN = new JTextField(4);
		mainPanel.add(enterID);
		mainPanel.add(iD);
		mainPanel.add(enterPIN);
		mainPanel.add(PIN);
		mainPanel.add(logIn);
		mainPanel.add(create);
		mainFrame.add(mainPanel);

		// If already customer button clicked, show frame2
		JFrame frame2 = new JFrame("Choose...");
		frame2.setSize(300, 300);
		JPanel panel2 = new JPanel();
		JButton openAccount = new JButton("Open Account");
		JButton deposit = new JButton("Deposit");
		JButton withdraw = new JButton("Withdraw");
		JButton transfer = new JButton("Transfer");
		JButton close = new JButton("Close Account");
		JButton info = new JButton("Customer Information");
		JButton out = new JButton("LogOut");
		panel2.add(openAccount);
		panel2.add(deposit);
		panel2.add(withdraw);
		panel2.add(transfer);
		panel2.add(close);
		panel2.add(info);
		panel2.add(out);
		frame2.add(panel2);

		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				atm.create();
			}
		});
		
		logIn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if(iD.getText().equals("000") && PIN.getText().equals("abcd")){
					atm.admin();
				}
				else if (atm.validate(iD.getText(), PIN.getText()) != -1){
					frame2.setVisible(true);
					
					openAccount.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.open();
						}
					});
					
					deposit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.deposit();
						}
					});
					
					withdraw.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.withdraw();
						}
					});
					
					transfer.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.transfer();
						}
					});
					
					close.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.closeAccount();
						}
					});
					
					info.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							atm.getBalance();
						}
					});
					
					out.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							frame2.dispose();
						}
					});
				}
				else{
					JOptionPane.showMessageDialog(frame2, "Log In Failed");
				}
			}
		});
	}
}
