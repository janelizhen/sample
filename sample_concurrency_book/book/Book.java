package book;

import java.util.*;

public enum Book {
	BOOK1 ("Gone with the wind"),
	BOOK2 ("How to kill a mock bird"),
	BOOK3 ("Programming Concurrency"),
	BOOK4 ("Operating Systems"),
	BOOK5 ("Introductory Robotics");
	
	private final String bookname;
	
	Book(String bookname) {
		this.bookname = bookname;
	}
	
	public String bookname() {
		return bookname;
	}
	
	public static Book random() {
		switch ((new Random()).nextInt(5)+1) {
		case 1:
			return BOOK1;
			
		case 2:
			return BOOK2;
			
		case 3:
			return BOOK3;
			
		case 4:
			return BOOK4;
			
		case 5:
			return BOOK5;
			
		default:
			return BOOK1;
		}
	}
}
