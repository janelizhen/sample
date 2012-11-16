package bookseller;

import java.util.*;

public class BookInventory {
	
	Hashtable<String, Integer> inventory;
	
	public BookInventory(){
		inventory = new Hashtable<String, Integer>();
	}
	
	public void initInventory(){
		inventory.put("gone with the wind", 5);
		inventory.put("kill a mock bird", 5);
		inventory.put("computer architecture", 5);
	}
	
	public Enumeration<String> getBookNames(){
		return inventory.keys();
	}
	
	public synchronized void increaseStock(String bookName, int incQuantity){
		if (inventory.containsKey(bookName)){
			inventory.put(bookName, inventory.get(bookName)+incQuantity);
		}
		else{
			inventory.put(bookName, incQuantity);
		}
		System.out.println("Restock: " + bookName + " " + inventory.get(bookName));
		notifyAll();
	}
	
	public synchronized void decreaseStock(String bookName, int decQuantity){
		Integer quantity = inventory.get(bookName);
		while (quantity == null || quantity < decQuantity){
			try{
				wait();
			}
			catch (InterruptedException e){}
		}
		inventory.put(bookName, quantity-decQuantity);
		System.out.println("Sell: " + bookName + " " + inventory.get(bookName));
	}
}
