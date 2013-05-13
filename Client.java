import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author lalluanthoor
 *
 * Client class for running chat using RMI
 * 
 * The class extends JFrame (from javax.swing). Alternate way is to include the frame as a member. You can also
 * use the concepts of Container here.
 * 
 * The class also implements ActionListener. Same class is used as event handler since I DON'T KNOW how to access
 * members of this class from a separate event handler class.
 */
public class Client extends JFrame implements ActionListener {
	
	/**
	 * GUI Components
	 */
	private JLabel l_messageBox, l_chatBox;
	private JTextField tf_messageBox;
	private JTextArea ta_chatBox;
	private JButton btn_send, btn_clear;
	
	/**
	 * TCP Connection Components
	 */
	private Socket connection;
	private BufferedReader inFromServer;
	int clientNumber;
	
	/**
	 * RMI Connection Component (Stub)
	 */
	CommunicateInterface comInterface;
	
	public Client(){
		/**
		 * Initialize GUI Components
		 */
		this.setLayout(null);
		this.setBounds(50, 50, 900, 600);
		this.setVisible(true);
		this.setResizable(true);
		this.setTitle("RMI Chat Client");
		
		//otherwise user will have to terminate it by hand (clicking close button won't work)
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		l_chatBox = new JLabel("Chat Line");
		l_chatBox.setLayout(null);
		l_chatBox.setBounds(10, 10, 150, 30);
		l_chatBox.setVisible(true);
		this.add(l_chatBox);
		
		l_messageBox = new JLabel("Message");
		l_messageBox.setLayout(null);
		l_messageBox.setBounds(10, 530, 150, 30);
		l_messageBox.setVisible(true);
		this.add(l_messageBox);
		
		ta_chatBox = new JTextArea();
		ta_chatBox.setLayout(null);
		ta_chatBox.setBounds(10, 40, 880, 480);
		ta_chatBox.setBorder(BorderFactory.createLineBorder(getForeground()));
		ta_chatBox.setVisible(true);
		ta_chatBox.setEditable(false);
		this.add(ta_chatBox);
		
		tf_messageBox = new JTextField();
		tf_messageBox.setLayout(null);
		tf_messageBox.setBounds(90, 530, 580, 30);
		tf_messageBox.setBorder(BorderFactory.createLineBorder(getForeground()));
		tf_messageBox.setVisible(true);
		this.add(tf_messageBox);
		
		btn_send = new JButton("Send");
		btn_send.setLayout(null);
		btn_send.setBounds(690, 530, 90, 30);
		btn_send.setActionCommand("SEND");
		btn_send.addActionListener(this);
		btn_send.setVisible(true);
		this.add(btn_send);
		
		btn_clear = new JButton("Clear");
		btn_clear.setLayout(null);
		btn_clear.setBounds(800, 530, 90, 30);
		btn_clear.setActionCommand("CLEAR");
		btn_clear.addActionListener(this);
		btn_clear.setVisible(true);
		this.add(btn_clear);
		
		try {
			//gets the remote object reference from RMI Registry
			comInterface = (CommunicateInterface)Naming.lookup("rmi://localhost/Chat");
			
			//establishes TCP connection for reading messages
			connection = new Socket("localhost",6655);
			inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			clientNumber = Integer.parseInt(inFromServer.readLine());
			this.setTitle(this.getTitle() + " " + clientNumber);		//add client number to title
			tf_messageBox.requestFocusInWindow();
			
			/**
			 * accept and print incoming messages
			 */
			while(true){
				ta_chatBox.append("HIM>>" + inFromServer.readLine() + "\n");
			}
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Event Handler method. It is invoked automatically whenever and ActionEvent (here click) occurs.
	 * Based on the action commands specified, it will either call the remote method or will clear the
	 * message box.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		//if Send button is pressed, call the remote method to pass message
		if(actionEvent.getActionCommand().equals("SEND")){
			if(tf_messageBox.getText().length()>0){
				ta_chatBox.append("ME>> " + tf_messageBox.getText() + "\n");			//adds message to chat line
				try {
					comInterface.sendMessage(clientNumber, tf_messageBox.getText());	//call to remote method
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				tf_messageBox.setText("");				//clears the message box after sending
			}
			tf_messageBox.requestFocusInWindow();		//set the cursor to the message box
		}else if(actionEvent.getActionCommand().equals("CLEAR")){  //if Clear button is pressed, clear the box
			tf_messageBox.setText("");					//clears the message box
			tf_messageBox.requestFocusInWindow();		//set the cursor to the message box
		}
		
	}
	
	public static void main(String args[]){
		new Client();
	}

}
