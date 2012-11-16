package bookseller;

import java.util.*;
import java.util.concurrent.*;
import utility.*;

public class JobQueue {
	
	Queue<Job> jobQueue;
	
	public JobQueue(){
		jobQueue = new ConcurrentLinkedQueue<Job>();
	}

	public synchronized boolean hasJob(){
		return jobQueue.size()!=0;
	}
	
	public synchronized void addJob(Job job){
		jobQueue.add(job);
	}
	
	public synchronized Job getJob(){
		return jobQueue.poll();
	}
}
