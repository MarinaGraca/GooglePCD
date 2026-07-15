
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
	
	private Socket socket;
	private String ip;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private GUI gui;
	
	public Client(String ip){
		this.ip = ip;
		
		gui = new GUI(this);
		gui.init();
		
		System.out.println("Client thread");
	}
	
	public void run(){
		try{
			connect();
			ask();
		} catch(IOException e){
			System.err.println("ERROR Client connect");
		} finally{
			System.out.println("Client stopping...");
			try{
				socket.close();
			} catch(IOException e){
				System.out.println("ERROR Client socket close");
			}
		}
	}
	
	private void connect() throws IOException{
		try{
			InetAddress a = InetAddress.getByName(ip);
			socket = new Socket(a, Server.PORTO);
			System.out.println("Connected to server socket: " + socket);
			System.out.println("Address: " + a);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject("Client");
			out.flush();
		} catch(UnknownHostException e){
			System.err.println("ERROR Client host");
		}
	}
	
	private void ask() throws IOException{
		while(!interrupted()){
			try{
				SearchResponse response = (SearchResponse) in.readObject();				
				gui.addResponse(response);
				
			} catch(ClassNotFoundException e){
				System.err.println("ERRO Client serve class not found");
			}
		}
	}
	
	public void sendRequest(String word) throws IOException{
		System.out.println("Client looking for: " + word);
		out.writeObject(word);
		out.flush();
	}
	
	public static void main(String[] args) throws IOException{
		new Client(args[0]).start();
	}

}
