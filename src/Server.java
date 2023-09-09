import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	// to do --> data structure to hold server threads 
	Vector<ServerThread> serverThreads;
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			while(true) {		
				// to do --> accept socket connections
				Socket s = ss.accept();	
			
				// to do --> add new ServerThread(s)
				ServerThread st = new ServerThread(s,this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in Server constructor: " + ioe.getMessage());
		}		
	}
	
	public static void main(String [] args) {
		// to do --> implement your main(), starting a server on port 6789
		Server server = new Server(6789);
		
	}
}
