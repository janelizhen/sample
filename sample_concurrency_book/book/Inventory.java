package book;

import java.util.*;

public class Inventory {
	
	EnumMap<Book, Integer> inventory;
	
	public Inventory() {
		inventory = new EnumMap<Book, Integer>(Book.class);
	}
	
	private void printWork(Boolean jobType, int jobSeqNum, Book book, int oldQuan, int newQuan) {
		if (jobType) {
	      System.out.println("A restocking work( " + book.bookname() + " , " + oldQuan + " -> " + newQuan + " ) is performed by job seqNum = " + jobSeqNum);
	    } else {
	    	System.out.println("A shipping work( " + book.bookname() + " , " + oldQuan + " -> " + newQuan + " ) is performed by job seqNum = " + jobSeqNum);
	    }
	}
	
	public synchronized void increaseStock(Book book, int quantity, int jobSeqNum) {
		if (inventory.containsKey(book))
			inventory.put(book, inventory.get(book)+quantity);
		else
			inventory.put(book, quantity);
		printWork(true, jobSeqNum, book, inventory.get(book)-quantity, inventory.get(book));
		notifyAll();
	}
	
	public synchronized void decreaseStock(Book book, int quantity, int jobSeqNum) throws InterruptedException {
		while (!inventory.containsKey(book) || inventory.get(book).intValue() < quantity) {
			wait();
		}
		inventory.put(book, inventory.get(book)-quantity);
		printWork(false, jobSeqNum, book, inventory.get(book)+quantity, inventory.get(book));
	}
}
