package edu.uga.db.sql;

import java.util.*;

/**
 * Class defines an attribute in relation
 * @author Li Zhen
 * @version 0.1
 */
public class Attribute {
	String name;
	boolean unique;
	String domain;
	List<Range> constraints;
	
	public Attribute(String name,String domain,String constraint){
		this.name = name;
		this.domain = domain;
		this.unique = false;
		this.constraints = Range.parseRange(constraint);
	}
	
	public void setUnique(){
		unique = true;
	}
	
	public void unsetUnique(){
		unique = false;
	}
	
	public String getDomain(){
		return this.domain;
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Range> getRange(){
		return this.constraints;
	}
}
