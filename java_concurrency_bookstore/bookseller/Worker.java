package bookseller;

import utility.Job;
import java.util.*;

public class Worker implements Runnable {
	
	private JobQueue queue;
	private BookInventory inventory;
	
	public Worker(JobQueue queue, BookInventory inventory){
		this.queue = queue;
		this.inventory = inventory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random random = new Random();
		while(true){
			Job job = queue.getJob();
			if (job != null){
				inventory.decreaseStock(job.getBookname(), job.getQuantity());
				System.out.println("Done: " + job.toString());
			}
			try{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e){}
		}
	}

}
