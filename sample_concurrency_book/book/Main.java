package book;

public class Main {
	
	public static void main(String args[]) {
		if (args.length < 4) {
			System.out.println("Usage: Main [num_of_client] [client_life_time] [max_num_running_job] [max_job_length]");
			System.exit(1);
		}
		try {
			int numOfClient = Integer.parseInt(args[0]);
			int clientLifeTime = Integer.parseInt(args[1]);
			int maxNumJob = Integer.parseInt(args[2]);
			int maxJobLength = Integer.parseInt(args[3]);
			
			Inventory inventory = new Inventory();
			
			Server server = new Server(maxNumJob, inventory);
			
			Thread[] clients = new Thread[numOfClient];
			for (int i=0; i<numOfClient; i++) {
				Thread t = new Thread(new Client(i, server, clientLifeTime, maxJobLength));
				clients[i] = t;
			}
			
			for (int i=0; i<numOfClient; i++) {
				clients[i].start();
			}
			
			for (int i=0; i<numOfClient; i++) {
				clients[i].join();
			}
			
			System.exit(0);
		} catch (NumberFormatException e) {
			System.out.println("Arguments are not in correct number format.");
			e.printStackTrace();
		} catch (InterruptedException e) {}
	}

}
