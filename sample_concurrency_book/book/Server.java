package book;

public class Server {

	public final int MAX_NUM_JOB;
	private int numOfShipJob, numOfRestockJob;
	private final Inventory inventory;
	
	
	public Server(int maxNumWorkers, Inventory inventory) {
		MAX_NUM_JOB = maxNumWorkers;
		numOfShipJob = 0;
		numOfRestockJob = 0;
		this.inventory = inventory;
	}
	
	public synchronized boolean process(RunnableJob job) {
		job.setInventory(this.inventory);
		if (job.getType()){
			if (numOfRestockJob < MAX_NUM_JOB) {
				job.setSeqNum(numOfRestockJob++);
				(new Thread(job)).start();
				return true;
			} else
				return false;
		}else{
			if (numOfShipJob < MAX_NUM_JOB) {
				job.setSeqNum(numOfShipJob++);
				(new Thread(job)).start();
				return true;
			} else
				return false;
		}
		
	}
	
	public synchronized void finishShip() {
		numOfShipJob--;
	}
	
	public synchronized void finishRestock() {
		numOfRestockJob--;
	}
}
