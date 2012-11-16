package edu.uga.db;

import java.io.*;
import java.util.*;

/**
 * @file MyMap.java
 * @author zhen
 * @version 0.1
 */
public class MyHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * default number of buckets
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * maximum number of buckets
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;
	/**
	 * default load factor
	 */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	/**
	 * default bucket capacity
	 */
	static final int DEFAULT_BUK_SIZE = 2;
	
	/**
	 * my table
	 */
	List<MyBucket<K,V>> table;
	/**
	 * mod factor
	 */
	final int M;
	/**
	 * table capacity (number of buckets)
	 */
	int capacity;
	/**
	 * table size (number of entry)
	 */
	transient int size;
	/**
	 * split pointer
	 */
	int n;
	/**
	 * load factor
	 */
	final float loadFactor;
	/**
	 * number of phrase
	 */
	transient int phrase;


	
	/**
	 * Constructs an empty MyMap with the default initial capacity (16)
	 * and the default load factor (0.75)
	 */
	public MyHashMap(){
		M = DEFAULT_INITIAL_CAPACITY;
		loadFactor = DEFAULT_LOAD_FACTOR;
		
		capacity = DEFAULT_INITIAL_CAPACITY;
		size = 0;
		n = 0;
		phrase = 0;
		
		table = new ArrayList<MyBucket<K,V>>(DEFAULT_INITIAL_CAPACITY);
		init(0);
	}
	
	/**
	 * Construct an empty MyMap with the specified initial
	 * capcity and load factor
	 * 
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 * @throws IllegalArgumentException if the initial capacity is negative
	 * 		   or the load factor is non positive
	 */
	public MyHashMap(int initialCapacity, float loadFactor){
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		if (initialCapacity > MAXIMUM_CAPACITY)
			initialCapacity = MAXIMUM_CAPACITY;
		if (loadFactor < 0 || Float.isNaN(loadFactor))
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		
		int capacity = 1;
		while (capacity < initialCapacity)
			capacity <<= 1;
		
		M = capacity;
		this.capacity = capacity;
		this.loadFactor = loadFactor;
		size = 0;
		n = 0;
		phrase = 0;
		
		table = new ArrayList<MyBucket<K,V>>(capacity);
		init(0);
	}
	
	/**
	 * Constructs an empty MyMap with the specified initial
	 * capacity and the default load factor (0.75)
	 * 
	 * @param initialCapacity the initial capacity
	 * @throws IllegalArgumentException if the initial capacity is negative
	 */
	public MyHashMap(int initialCapacity){
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Initialize new expanded buckets
	 * @param from the index of first expanded buckets
	 */
	private void init(int from){
		for (int i=from;i<capacity;i++){
			table.add(new MyBucket<K,V>());
		}
	}
	
	/**
	 * Hash function to calculate insertion index
	 * 
	 * @param h the hash code for hashing
	 * @param M the current mod factor
	 * @param phrase the current phrase
	 * @return the hashing value
	 */
	private int insertHash(int h, int M, int phrase){
		return h % (M << phrase);
	}
	
	/**
	 * Hash function to calculate splitting index
	 * 
	 * @param h the hash code for hashing
	 * @param M the current mod factor
	 * @param phrase the current phrase
	 * @return the hashing value
	 */
	private int splitHash(int h, int M, int phrase){
		return h % (M << (phrase+1));
	}
	
	/**
	 * Supplemental hash function to a given hash code for insertion
	 * 
	 * @param h the hash code for hashing
	 * @return new hash code
	 */
	int insertHash(int h){
		return insertHash(h, this.M, this.phrase);
	}
	
	/**
	 * Supplemental hash function to a given hash code for splitting
	 * 
	 * @param h the hash code for hashing
	 * @return new hash code
	 */
	int splitHash(int h){
		return splitHash(h, this.M, this.phrase);
	}
	
	/**
	 * Returns insertion index for hash code h
	 * 
	 * @param h hash code
	 * @return insertion index in this map
	 */
	int indexForInsert(int h){
		return h & ((1<<(phrase+1))-1);
	}
	
	/**
	 * Returns splitting index for hash code h
	 * 
	 * @param h hash code
	 * @return splitting index in this map
	 */
	int indexForSplit(int h){
		return h & ((1<<(phrase+2))-1);
	}
	
	@Override
	public int size(){
		return size;
	}
	
	@Override
	public boolean isEmpty(){
		return size == 0;
	}
	
	/**
	 * Get current load factor of the map
	 * 
	 * @return current load factor
	 */
	float getLoadFactor(){
		return ((float)size()) / ((float)(DEFAULT_BUK_SIZE * capacity));
	}
	
	@Override
	public V put(K key, V value){
		addEntry(new MyEntry<K,V>(key,value),-1);
		size++;
		if (getLoadFactor() >= loadFactor){
			split();
		}
		return value;
	}
	
	/**
	 * Add an entry into the map at specified index
	 * 
	 * @param e the entry to be added
	 * @param index the specified index, -1 to be calculated
	 */
	private void addEntry(MyEntry<K,V> e, int index){		
		if (index < 0){
			index = indexForInsert(insertHash(e.getKey().hashCode()));
		}
		
		table.get(index).addEntry(e);
	}
	
	/**
	 * Remove an enry from the map at specified index
	 * 
	 * @param e the entry to be removed
	 * @param index the specified index, -1 to be calculated
	 */
	private void removeEntry(MyEntry<K,V> e, int index){
		if (index < 0){
			index = indexForInsert(insertHash(e.getKey().hashCode()));
		}
		
		table.get(index).removeEntry(e);
	}
	
	/**
	 * Split buckets
	 */
	private void split(){
		// expand buckets
		capacity += 1;
		init(capacity-1);
		
		List<MyEntry<K,V>> temp = new ArrayList<MyEntry<K,V>>();		
		Iterator<MyEntry<K,V>> itr = table.get(n).iterator();
		
		while (itr.hasNext()){
			temp.add(itr.next());
		}
		
		itr = temp.iterator();
		
		while (itr.hasNext()){
			MyEntry<K,V> e = itr.next();
			int s_idx = indexForSplit(splitHash(e.getKey().hashCode()));
			int i_idx = indexForInsert(insertHash(e.getKey().hashCode()));
			if (s_idx != i_idx){
				// re-schedule the entry
				removeEntry(e,i_idx);
				addEntry(e,s_idx);
			}
		}
		
		n++;
		if (n == (M << phrase)){
			n = 0;
			phrase++;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key){
		int index = indexForInsert(insertHash(((K)key).hashCode()));
		Iterator<MyEntry<K,V>> itr = table.get(index).iterator();
		while (itr.hasNext()){
			MyEntry<K,V> e = itr.next();
			if (e.getKey() == key || e.getKey().equals(key)){
				return e.getValue();
			}
		}
		
		index = indexForSplit(splitHash(((K)key).hashCode()));
		itr = table.get(index).iterator();
		while (itr.hasNext()){
			MyEntry<K,V> e = itr.next();
			if (e.getKey() == key || e.getKey().equals(key)){
				return e.getValue();
			}
		}
		
		index = indexForSplit(splitHash(((K)key).hashCode(),M,phrase-1));
		itr = table.get(index).iterator();
		while (itr.hasNext()){
			MyEntry<K,V> e = itr.next();
			if (e.getKey() == key || e.getKey().equals(key)){
				return e.getValue();
			}
		}
		
		index = indexForSplit(splitHash(((K)key).hashCode(),M,phrase-1));
		itr = table.get(index).iterator();
		while (itr.hasNext()){
			MyEntry<K,V> e = itr.next();
			if (e.getKey() == key || e.getKey().equals(key)){
				return e.getValue();
			}
		}
		
		for (int i=0;i<table.size();i++){
			itr = table.get(i).iterator();
			while (itr.hasNext()){
				MyEntry<K,V> e = itr.next();
				if (e.getKey() == key || e.getKey().equals(key)){
					return e.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * Print the map
	 */
	public void printTable(){
		Iterator<MyBucket<K,V>> t_itr = table.iterator();
		int i = 0;
		System.out.print(i+": ");
		while (t_itr.hasNext()){
			t_itr.next().printBucket();
			System.out.println();
			i++;
		}
	}
	
	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<K> keyList() {
		List<K> result = new ArrayList<K>();
		Iterator<MyBucket<K,V>> t_itr = table.iterator();
		while (t_itr.hasNext()){
			Iterator<MyEntry<K,V>> e_itr = t_itr.next().iterator();
			while (e_itr.hasNext()){
				result.add(e_itr.next().getKey());
			}
		}
		return result;
	}
}
