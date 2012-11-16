package edu.uga.db;

import java.util.*;

/**
 * @file Bucket.java
 * @author zhen
 * @version 0.1
 */
public class MyBucket<K,V> {
	// polymorphism mechanism
	List<MyEntry<K,V>> bucket;
	
	/**
	 * Default Constructor
	 */
	public MyBucket(){
		bucket = new ArrayList<MyEntry<K,V>>();
	}
	
	/**
	 * Add an entry
	 * 
	 * @param e
	 */
	void addEntry(MyEntry<K,V> e){
		bucket.add(e);
	}
	
	/**
	 * Remove an entry
	 * 
	 * @param e
	 */
	void removeEntry(MyEntry<K,V> e){
		bucket.remove(e);
	}
	
	/**
	 * Get bucket size
	 * 
	 * @return bucket size
	 */
	int size(){
		return bucket.size();
	}
	
	/**
	 * Get bucket iterator
	 * 
	 * @return iterator
	 */
	Iterator<MyEntry<K,V>> iterator(){
		return bucket.iterator();
	}
	
	/**
	 * Print bucket
	 */
	void printBucket(){
		Iterator<MyEntry<K,V>> itr = iterator();
		while (itr.hasNext()){
			itr.next().printEntry();
		}
	}
}
