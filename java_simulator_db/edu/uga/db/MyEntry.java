package edu.uga.db;

/**
 * @file MyEntry.java
 * @author zhen
 * @version 0.1
 */
public class MyEntry<K,V> {
	private K key;
	private V value;
	
	/**
	 * Default Constructor
	 */
	public MyEntry(){
		this.key = null;
		this.value = null;
	}
	
	/**
	 * Constructor with specified key and value
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public MyEntry(K key, V value){
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Constructor with specified key and default value
	 * 
	 * @param key the key
	 */
	public MyEntry(K key){
		this.key = key;
		this.value = null;
	}
	
	/**
	 * Set key
	 * 
	 * @param key
	 */
	void setKey(K key){
		this.key = key;
	}
	
	/**
	 * Set Value
	 * 
	 * @param value
	 */
	void setValue(V value){
		this.value = value;
	}
	
	/**
	 * Get key
	 * 
	 * @return key
	 */
	K getKey(){
		return key;
	}
	
	/**
	 * Get Value
	 * 
	 * @return value
	 */
	V getValue(){
		return value;
	}
	
	/**
	 * Print entry
	 */
	void printEntry(){
		System.out.print("(" + key + ", " + value + ")  ");
	}
}