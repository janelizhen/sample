package edu.uga.db;
/****
 * @file TupleGenerator.java
 * @author Li Zhen
 */

import java.util.*;
import edu.uga.db.sql.*;

@SuppressWarnings("unchecked")
/***
 * This class generates as many numbers of tuples as 
 * specified by the user according to specified schema description
 */
public class TupleGenerator {
	Schema schema;
	
	/**
	 * Parse the table schema into specified schema object
	 * @param schema the schema specification
	 */
	public TupleGenerator(String schema){
		this.schema = Schema.parseSchema(schema);
	}
	
	/**
	 * Generate specified number of tuples according to schema object of this generator
	 * @param numTuple	number of tuples to be generated
	 * @return	List of tuples generated
	 */
	public List<Comparable[]> getTuples(int numTuple){
		List<Comparable[]> result = new ArrayList<Comparable[]>();
		
		Random rand = new Random();
		for (int i=0;i<numTuple;i++){
			Comparable[] tuple = new Comparable[schema.getAttributes().size()];
			for (int j=0;j<schema.getAttributes().size();j++){
				Attribute a = schema.getAttributes().get(j);
				Range r = a.getRange().get(rand.nextInt(a.getRange().size()));
				Comparable v;
				if (r.isDiscrete()){
					v = r.values().get(rand.nextInt(r.values().size()));
				}
				else{
					if (a.getDomain().equals("Integer"))
						v = rand.nextInt(Integer.parseInt(r.getEnd())-Integer.parseInt(r.getStart())) + Integer.parseInt(r.getStart());
					else if (a.getDomain().equals("Float"))
						v = rand.nextFloat() * rand.nextInt(Integer.parseInt(r.getEnd())-Integer.parseInt(r.getStart())) + Integer.parseInt(r.getStart());
					else if (a.getDomain().equals("Double"))
						v = rand.nextDouble() * rand.nextInt(Integer.parseInt(r.getEnd())-Integer.parseInt(r.getStart())) + Integer.parseInt(r.getStart());
					else if (a.getDomain().equals("Long"))
						v = rand.nextLong() * rand.nextInt(Integer.parseInt(r.getEnd())-Integer.parseInt(r.getStart())) + Integer.parseInt(r.getStart());
					else
						v = rand.toString();
				}
				tuple[j] = v;
			}
			result.add(tuple);
		}		
		return result;
	}
}
