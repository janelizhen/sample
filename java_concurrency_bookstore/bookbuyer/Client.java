package bookbuyer;

import java.io.*;
import java.net.*;
import java.util.*;

import utility.Job;

public class Client {
	
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	String id;
	
	public Client(String id){
		this.id = id;
	}

	void run(String args[]){
		try{
			//1. create a socket to connect to the server
			requestSocket = new Socket("localhost", 9999);
			System.out.println("Connected to localhost server in port 9999");


			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			//3: Communicate with the server
			List<Job> jobs = parseJobs(args);
			Iterator<Job> itr = jobs.iterator();
			Random random = new Random();
			while (itr.hasNext()){
				try{
					sendJob(itr.next());
					Thread.sleep(random.nextInt(500));
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sendJob(null);
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}

	void sendJob(Job job) {
		try{
				out.writeObject(job);
				out.flush();
				if (job != null){
					System.out.println("client " + id + " >" + job.toString());
				}
			}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	List<Job> parseJobs(String args[]){
		List<Job> jobs = new ArrayList<Job>();
		for (int i=1;i<args.length;i++){
			jobs.add(Job.parseJob(args[i]));
		}
		return jobs;
	}

	public static void main(String args[]){
			Client client = new Client(args[0]);
			client.run(args);
		}
}
