import java.io.Serializable;

@SuppressWarnings("serial")
public class Task implements Serializable{
	
	private SearchFile file; // ficheiro da noticia
	private String word; // palavra a procurar
	private int clientID;
	
	public Task(SearchFile file, String word){
		this.file = file;
		this.word = word;
	}
	
	public SearchFile getFile(){
		return file;
	}
	
	public void setFile(SearchFile file){
		this.file = file;
	}
	
	public String getWord(){
		return word;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public void setClientID(int clientID){
		this.clientID = clientID;
	}
	
	public String toString(){
		return ("Task: looking for " + word + " for client " + clientID);
	}

}
