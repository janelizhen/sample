package utility;

import java.io.Serializable;
import java.util.*;

public class Job implements Serializable{
	
	private static final long serialVersionUID = -599256627921306983L;
	String bookName;
	int quantity;
	
	public Job(String bookName, int quantity){
		this.bookName = bookName;
		this.quantity = quantity;
	}
	
	public String getBookname(){
		return this.bookName;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	public static Job parseJob(String description){
		Job job = null;
		try{
			StringTokenizer token = new StringTokenizer(description, ",", false);
			job = new Job(token.nextToken(),Integer.parseInt(token.nextToken()));
		}
		catch (NoSuchElementException e){
			e.printStackTrace();
		}
		return job;
	}
	
	public String toString(){
		return bookName + "," + quantity;
	}
}
