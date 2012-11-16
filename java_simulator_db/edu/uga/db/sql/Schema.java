package edu.uga.db.sql;

import java.util.*;

/**
 * Class defines a schema object
 * @author Li Zhen
 * @version 0.1
 */
public class Schema {
	List<Attribute> attributes;
	
	public Schema(String attributes,String domains,String constraints){
		this.attributes = new ArrayList<Attribute>();
		String[] attris = attributes.split(" ");
		String[] doms = domains.split(" ");
		String[] cons = constraints.split(" ");
		if (attris.length == doms.length && doms.length == cons.length){
			for (int i=0;i<attris.length;i++){
				this.attributes.add(new Attribute(attris[i],doms[i],cons[i]));
			}
		}
	}
	
	public List<Attribute> getAttributes(){
		return this.attributes;
	}
	
	/**
	 * Parse a string description into a schema object
	 * @param schema
	 * @return
	 */
	public static Schema parseSchema(String schema){
		StringTokenizer token = new StringTokenizer(schema,",");
		String attributes = "", domains = "", constraints = "";
		while (token.hasMoreTokens()){
			String[] attribute = token.nextToken().split(" ");
			for (int i=0;i<attribute.length;i++){
				switch(i){
				case 0:
					attributes += attribute[i] + " ";
					break;
					
				case 1:
					domains += attribute[i] + " ";
					break;
					
				case 2:
					constraints += attribute[i] + " ";
					break;
				}
			}
		}
		return new Schema(attributes,domains,constraints);
	}
}
