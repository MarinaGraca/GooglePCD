import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SearchResponse implements Serializable{
	
	private int createdTasks;
	
	private ArrayList<SearchResult> results;
	
	public SearchResponse(int createdTasks){
		this.createdTasks = createdTasks;
		results = new ArrayList<SearchResult>();
	}
	
	public synchronized void add(SearchResult result){
		results.add(result);
		
		notifyAll();
	}
	
	public synchronized void awaitResults() throws InterruptedException{
		while(createdTasks != results.size())
			wait();
	}
		
	public ArrayList<SearchResult> getResults(){
		return results;
	}
	
}
