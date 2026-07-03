import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchResult implements Serializable, Comparable<SearchResult>{
	
	private int n;
	private SearchFile file;
	private int clientID;
	
	public SearchResult(){
		n = 0;
	}
	
	public SearchResult(SearchFile file, int clientID){
		n = 0;
		this.file = file;
		this.clientID = clientID;
	}
	
	public SearchFile getFile(){
		return file;
	}
	
	public void setFile(SearchFile file){
		this.file = file;
	}
	
	public int getN(){
		return n;
	}
	
	public void setN(int n){
		this.n = n;
	}
	
	public int getFileID(){
		return file.getID();
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public String toString(){
		return (getN() + " - " + file.getTitle());
	}

	@Override
	public int compareTo(SearchResult compareSR){
		int compareN = ((SearchResult) compareSR).getN();
		return compareN - this.getN();
	}
	
}
