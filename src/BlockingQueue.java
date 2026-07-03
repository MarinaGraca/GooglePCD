import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue{
	
	private Queue<Task> queue;
	
	public BlockingQueue(){
		queue = new LinkedList<Task>();
	}		
	
	public synchronized void offer(Task task) throws InterruptedException{
		queue.offer(task);
		notifyAll();
	}
	
	public synchronized Task poll() throws InterruptedException{
		while(queue.size() == 0)
			wait();
		
		return queue.poll();
	}

}
