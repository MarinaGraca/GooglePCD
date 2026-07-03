import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server{
	
	public static int PORTO = 9090;
	
	private ServerSocket ss;
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private LinkedList<ClientConnect> clients;
	private LinkedList<WorkerConnect> workers;
	
	private String pasta;
	
	private SearchFile[] searchFiles;
	
	private BlockingQueue queue;
	
	public Server(String pasta){
		System.out.println("Server running");
		
		this.pasta = pasta;
		
		try{
			setTxt(pasta);
		} catch(FileNotFoundException e){
			System.err.println("ERROR Server file not found");
		}
			
		clients = new LinkedList<ClientConnect>();
		workers = new LinkedList<WorkerConnect>();

		queue = new BlockingQueue();
	}
	
	private void serve() throws IOException, ClassNotFoundException{
		try{
			ss = new ServerSocket(PORTO);
			System.out.println("Server socket: " + ss);
			
			while(true){
				socket = ss.accept();
				System.out.println("Conection accepted on socket: " + socket);
				
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
				String tipo = (String) in.readObject();
				
				if(tipo.equals("Client")){
					ClientConnect cc = new ClientConnect(this, socket, in, out);
					cc.start();
					clients.add(cc);
				}
				else if(tipo.equals("Worker")){
					WorkerConnect wc = new WorkerConnect(this, socket, in, out);
					wc.start();
					workers.add(wc);
				}
			}
		} finally{
			ss.close();
		}
	}
	
	public void setTxt(String pasta) throws FileNotFoundException{
		File[] files = new File(pasta).listFiles();
		
		System.out.println(files.length + " files in the folder");
		
		searchFiles = new SearchFile[files.length];
		
		int fileID = 0;
		
		for(int i = 0; i < files.length; i++){
			String titulo = getTitle(files[i]);
			String texto = getContent(files[i]);
			
			searchFiles[i] = new SearchFile(titulo, texto, fileID);
			fileID++;
		}
		
	}
	
	public String getTitle(File file) throws FileNotFoundException{
		String title = "";
		
		Scanner scan = new Scanner(file, "UTF-8");
		title = scan.nextLine();
		scan.close();
		
		return title;
	}
	
	public String getContent(File file) throws FileNotFoundException{
		String conteudo = "";
		
		Scanner scan = new Scanner(file, "UTF-8");
		
		while(scan.hasNext())
			conteudo = scan.nextLine();
		
		scan.close();
		
		return conteudo;
	}
	
	public void sendResult(SearchResult result) throws IOException{
		out.writeObject(result);
		out.flush();
	}
	
	public void addTask(Task task) throws InterruptedException{
		queue.offer(task);
	}
	
	public Task removeTask() throws InterruptedException{
		return queue.poll();
	}
	
	public ClientConnect getClientConnect(int clientID){
		for(int i = 0; i < clients.size(); i++){
			ClientConnect cc = clients.get(i);
			
			if(clientID == cc.getClientID())
				return cc;
		}
		return null;
	}
	
	public String getPasta(){
		return pasta;
	}
	
	public static int getPorto(){
		return PORTO;
	}
	
	public static void setPorto(int porto){
		PORTO = porto;
	}
	
	public SearchFile[] getSearchFiles(){
		return searchFiles;
	}
	
	public static void main(String[] args) throws IOException, NumberFormatException, ClassNotFoundException{
		new Server(args[0]).serve();
	}

}
