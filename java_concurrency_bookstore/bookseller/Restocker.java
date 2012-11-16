package bookseller;

import java.util.*;

public class Restocker implements Runnable {
	
	private BookInventory inventory;
	
	public Restocker(BookInventory inventory){
		this.inventory = inventory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random random = new Random();
		while (true){
			Enumeration<String> bookNames = inventory.getBookNames();
			while (bookNames.hasMoreElements()){
				String bookname = bookNames.nextElement();
				inventory.increaseStock(bookname, 3+random.nextInt(3));
			}
			try{
				Thread.sleep(30000+random.nextInt(1000));
			}
			catch (InterruptedException e){}
		}
	}

}
