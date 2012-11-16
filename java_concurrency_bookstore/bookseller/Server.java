package bookseller;

import java.io.*;
import java.net.*;
import utility.*;

public class Server {

	ServerSocket providerSocket;
	Socket connection = null;

	ObjectOutputStream out;
	ObjectInputStream in;

	String message;

	void run(){
		JobQueue queue = new JobQueue();
		BookInventory inventory = new BookInventory();
		inventory.initInventory();
		
		Worker w1 = new Worker(queue, inventory);
		Worker w2 = new Worker(queue, inventory);
		Worker w3 = new Worker(queue, inventory);
		Worker w4 = new Worker(queue, inventory);
		Worker w5 = new Worker(queue, inventory);
		
		(new Thread(w1)).start();
		(new Thread(w2)).start();
		(new Thread(w3)).start();
		(new Thread(w4)).start();
		(new Thread(w5)).start();		
		
		Restocker r1 = new Restocker(inventory);
		
		(new Thread(r1)).start();
		
		try{
			providerSocket = new ServerSocket(9999, 10);

			while (true){
				connection = providerSocket.accept();
				System.out.println("Connection received from " + connection.getInetAddress().getHostName());

				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());

				Job job = null;
				try{
					while ((job=(Job)in.readObject())!=null){
						queue.addJob(job);
						System.out.println("Add to queue: " + job.toString());
					}
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
			}
		}
		catch(IOException ioException){ 
			ioException.printStackTrace();
		}
		finally{
			try{
				in.close();
				out.close();
				providerSocket.close();
			}
			catch(IOException ioException){ ioException.printStackTrace(); }
		}
	}
	
	public static void main(String args[]){
		Server server = new Server();
		server.run();
	}
}
