import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerConnect extends Thread{
	
	private Server server;
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public WorkerConnect(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out){
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
	}
	
	public void run(){
		try{
			work();
		} catch(InterruptedException | IOException e){
			System.err.println("ERROR WorkerConnect work");
		} finally{
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERROR WorkerConnect socket close");
			}
		}
	}
	
	private void work() throws IOException, InterruptedException{
		try{
			while(!interrupted()){
				
				Task task = server.removeTask();
				out.writeObject(task);
				out.flush();
				
				SearchResult searchResult = (SearchResult) in.readObject();
				ClientConnect cc = server.getClientConnect(task.getClientID());
				cc.getResponse().add(searchResult);
			}
		} catch(ClassNotFoundException e){
			System.err.println("ERROR WorkerConnect class not found");
		}
	}

}
