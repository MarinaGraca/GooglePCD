import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnect extends Thread{
	
	private static int clientCounter = 0;
	private int clientID;
	
	private Server server;
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private SearchResponse searchResponse;
	
	public ClientConnect(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out){
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
		clientID = clientCounter++;
	}
	
	public void run(){
		try{
			ask();
		} catch(InterruptedException | IOException e){
			System.err.println("ERROR ClientConnect ask");
		} finally{
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERROR ClientConnect socket close");
			}
		}
	}
		
	private void ask() throws IOException, InterruptedException{
		try{
			while(!interrupted()){
//				SearchRequest request = (SearchRequest) in.readObject();
//				setRequest(request);
				
				String word = (String) in.readObject();
				setRequest(word);
				
				int n = server.getSearchFiles().length;
				searchResponse = new SearchResponse(n);
				searchResponse.awaitResults();
				out.writeObject(searchResponse);
				out.flush();
			}
		} catch(ClassNotFoundException e){
			System.err.println("ERROR ClientConnect serve class not found");
		}
	}
	
	public synchronized void setRequest(String word) throws InterruptedException{
		SearchFile[] files = server.getSearchFiles();
		
		for(int i = 0; i < files.length; i++){
			SearchFile file = files[i];
			Task task = new Task(file, word);
			task.setClientID(getClientID());
			server.addTask(task);
		}
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public SearchResponse getResponse(){
		return searchResponse;
	}

}
