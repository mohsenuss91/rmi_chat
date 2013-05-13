import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 * @author lalluanthoor
 *
 * Communicate class implements the remote methods and implements chat server
 */
public class Communicate extends UnicastRemoteObject implements CommunicateInterface {
	
	private ServerSocket serverSocket;				//for obtaining connection
	private Socket clientOne, clientTwo;			//for maintaining connection
	private PrintWriter outToOne, outToTwo;			//for sending messages to clients

	public Communicate() throws RemoteException {
		super();
		try {
			serverSocket = new ServerSocket(6655);	//create ServerSocket
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try {
			clientOne = serverSocket.accept();		//accept connection from client one
			clientTwo = serverSocket.accept();		//accept connection from client two
			
			outToOne = new PrintWriter(clientOne.getOutputStream());
			outToOne.println("1");					//send client number to client one
			outToOne.flush();
			outToTwo = new PrintWriter(clientTwo.getOutputStream());
			outToTwo.println("2");					//send client number to client two
			outToTwo.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void sendMessage(int fromClient, String message) throws RemoteException {
		if(fromClient == 1){					//if from client one, send message to client two
			outToTwo.println(message);
			outToTwo.flush();
		}else if(fromClient == 2){				//else if from client two, send message to client one
			outToOne.println(message);
			outToOne.flush();
		}
	}

}
