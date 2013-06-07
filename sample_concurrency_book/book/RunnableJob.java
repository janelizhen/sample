package book;

import java.util.*;

public class RunnableJob implements Runnable {
	final int jobId;
	// true: restocking job
	// false: shipping job
	final Boolean jobType;
	Server server;
	Inventory inventory;
	int seqNum;
	final List<Work> works;
	
	public RunnableJob(int jobId, Boolean jobType) {
		works = new ArrayList<Work>();
		this.jobType = jobType;
		this.jobId = jobId;
	}
	
	public void addWork(Book book, int quantity) {
		works.add(new Work(book, quantity));
	}
	
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	
	public Boolean getType() {
		return jobType;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void run() {
		if (jobType) {
			for (Work work : works) {
				inventory.increaseStock(work.book, work.quantity, seqNum);
			}
			server.finishRestock();
		} else {
			for (Work work : works) {
				try {
					inventory.decreaseStock(work.book, Math.abs(work.quantity), seqNum);
				}catch(InterruptedException e){}
			}
			server.finishShip();
		}
	}
}
