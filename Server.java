import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * 
 * @author lalluanthoor
 * 
 * Server program for creating and registering the remote object
 */
public class Server {
	public static void main(String args[]){
		
		Communicate comObject;									//remote object
		try {
			comObject = new Communicate();
			Naming.rebind("rmi://localhost/Chat", comObject);	//registering to rmi registry
			comObject.connect();								//method to establish connection with clients
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
