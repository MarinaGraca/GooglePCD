import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Worker extends Thread{
	
	private Socket socket;
	private String ip;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Worker(String ip){
		this.ip = ip;
		
		System.out.println("Worker thread");
	}
	
	public void run(){
		try{
			connect();
			work();
		} catch(IOException e){
			System.err.println("ERROR Worker connect");
		} finally{
			System.out.println("Worker stopping...");
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERROR Worker socket close");
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
			
			out.writeObject("Worker");
			out.flush();
			
		} catch(UnknownHostException e){
			System.err.println("ERROR Worker host");
		}
	}
	
	private void work() throws IOException{
		while(!interrupted()){
			try{				
				Task task = (Task) in.readObject();
				SearchResult searchResult = search(task);
				out.writeObject(searchResult);
				out.flush();
				
			} catch(ClassNotFoundException e){
				System.err.println("ERROR Worker serve class not found");
			} catch(EOFException e){
				System.err.println("ERROR Worker serve eof");
			}
		}
	}
	
	public SearchResult search(Task task) throws IOException{
		SearchResult searchResult = new SearchResult();
		
		searchResult.setFile(task.getFile());
		
		int n = findWord(task.getFile(), task);
		searchResult.setN(n);
		
		return searchResult;
	}
	
	public int findWord(SearchFile file, Task task){	
		int n = 0;
		
		String[] words = file.getContent().split(" ");
		
		for(String w : words)
			if(w.equals(task.getWord()))
				n++;
		
		return n;
	}
	
	public static void main(String[] args) throws IOException{
		new Worker(args[0]).start();
	}
	
}
