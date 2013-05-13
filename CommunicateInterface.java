import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @author lalluanthoor
 * 
 * The interface for building stub and skeleton
 */
public interface CommunicateInterface extends Remote {
	
	/**
	 * signature of the remote method
	 * @param fromClient clientNumber of the sender
	 * @param message message content
	 * @throws RemoteException
	 */
	public void sendMessage(int fromClient, String message) throws RemoteException;
}
