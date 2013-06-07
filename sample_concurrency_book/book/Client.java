package book;

import java.util.*;

public class Client implements Runnable {
	final int MAX_JOB_LENGTH;
	final int clientId;
	final Server server;
	final int lifeTime;
	int jobCount;
	
	public Client(int clientId, Server server, int lifeTime, int maxJobLength) {
		this.server = server;
		this.lifeTime = lifeTime;
		this.clientId = clientId;
		this.jobCount = 0;
		this.MAX_JOB_LENGTH = maxJobLength;
	}
	
	public void run() {
		long t1 = System.currentTimeMillis();
		while (System.currentTimeMillis() - t1 <= lifeTime) {
			RunnableJob job = generateJob();
			while(!server.process(job) && System.currentTimeMillis() - t1 <= lifeTime){
				try {
					Thread.sleep((new Random()).nextInt(500));
				}catch (InterruptedException e){}
			}
		}
	}

	private RunnableJob generateJob() {
		int jobId = this.clientId * (10 ^ ((new Integer(lifeTime).toString()).length())) + (jobCount++);
		Boolean jobType = (new Random()).nextBoolean();
		RunnableJob job = new RunnableJob(jobId, jobType);
		job.setServer(server);
		int jobLength = (new Random()).nextInt(MAX_JOB_LENGTH) + 1;
		while (jobLength > 0) {
			Book book = Book.random();
			int quantity = (new Random()).nextInt(20) + 1;
			job.addWork(book, quantity);
			jobLength--;
			printWork(jobId, jobType, book, quantity);
		}
		return job;
	}
	
	void printWork(int jobId, Boolean jobType, Book book, int quantity) {
	    if (jobType) {
	      System.out.println("A restocking work( " + book.bookname() + " , " + quantity + " ) will be performed by job id = " + jobId);
	    } else {
	      System.out.println("A shipping work( " + book.bookname() + " , " + quantity + " ) will be performed by job id = " + jobId);
	    }
	  }
}
