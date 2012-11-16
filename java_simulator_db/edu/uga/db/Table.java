package edu.uga.db;
/*****************************************************************************************
 * @file  Table.java
 *
 * @author   John Miller
 * @version  0.9, Tue Jun 17 15:43:46 EDT 2008
 */

import static java.lang.System.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;


@SuppressWarnings("unchecked")
/*****************************************************************************************
 * This class implements relational database tables (including attribute names, domains
 * and a list of tuples.  Five basic relational algebra operators are provided: project,
 * select, union and minus.  The insert data manipulation operator is also provided.
 * Missing are update and delete data manipulation operators, primary keys, duplicate
 * elimination and indexes.
 */
public class Table implements Serializable, Cloneable
{
    /**  Serializable class version number.
     */
    private static final long serialVersionUID = 11L;

    /** Counter for naming temporary tables.
     */
    private static int count = 0;

    /** Table name.
     */
    private final String name;

    /** Array of attribute names.
     */
    private final String [] attribute;

    /** Array of attribute domains: a domain may be
     *  real: Double, Float; integer: Long, Integer, Short, Byte; string: String
     */
    private final Class [] domain;

    /** Collection of tuples.
     */
    private final List<Comparable []> tuples;
    
    /** Indexing map
     */
    public MyHashMap<Comparable[],Integer> indexMap;
    
    /** Keys
     */
    private String [] keys;

    /*************************************************************************************
     * Construct an empty table from the meta-data specifications.
     * @param  name       The name of the relation.
     * @param  attribute  String containing attributes names
     * @param  domain     String containing attribute domains (data types).
     */  
    public Table (String name, String [] attribute, Class [] domain)
    {
        this.name      = name;
        this.attribute = attribute;
        this.domain    = domain;
        this.tuples    = new ArrayList<Comparable []> ();
        this.indexMap  = new MyHashMap<Comparable[],Integer>();
        this.keys      = null;
    } // Table
    
    /*************************************************************************************
     * Construct an empty table from the meta-data specifications.
     * @param  name       The name of the relation.
     * @param  attribute  String containing attributes names
     * @param  domain     String containing attribute domains (data types).
     */  
    public Table (String name, String [] attribute, Class [] domain, String[] key)
    {
        this.name      = name;
        this.attribute = attribute;
        this.domain    = domain;
        this.tuples    = new ArrayList<Comparable []> ();
        this.indexMap  = new MyHashMap<Comparable[],Integer>();
        this.keys	   = key;
    } // Table

    /*************************************************************************************
     * Construct an empty table from the raw string specifications.
     * @param  name        The name of the relation.
     * @param  attributes  String containing attributes names
     * @param  domains     String containing attribute domains (data types).
     */
    public Table (String name, String attributes, String domains)
    {
        this.name      = name;
        this.attribute = attributes.split (" ");
        this.domain    = findClass (domains.split (" "));
        this.tuples    = new ArrayList<Comparable []> ();
        this.indexMap  = new MyHashMap<Comparable[],Integer>();
        this.keys	   = null;
    } // Table
 
    /*************************************************************************************
     * Construct an empty table from the raw string specifications with key definitions
     * @param  name        The name of the relation.
     * @param  attributes  String containing attributes names
     * @param  domains     String containing attribute domains (data types).
     */
    public Table (String name, String attributes, String domains, String keys)
    {
        this(name,attributes,domains);
        this.keys = keys.split(" ");
    } // Table

    /*************************************************************************************
     * Construct an empty table using the meta-data of an existing table.
     * @param  tab     The table supplying the meta-data.
     * @param  suffix  The suffix appended to create new table name.
     */
    public Table (Table tab, String suffix)
    {
    	this (tab.name + suffix, tab.attribute, tab.domain, tab.keys);
    } // Table


    /*************************************************************************************
     * Project the tuples onto a lower dimension by keeping only the given attributes.
     * #usage   movie.project ("title year studioName")
     * @param   attributeList  Attributes to project onto.
     * @return  Table of projected tuples.
     */
    public Table project (String attributeList)
    {
        out.println ("" + name + " . project ( " + attributeList + " )");

        String [] pAttribute = attributeList.split (" ");
        Class []  colDomain  = extractDom (match (pAttribute), domain);
        Table     result     = new Table (name + count++, pAttribute, colDomain);
        
        int [] colpos = match(pAttribute);

        for (Comparable[] tup : tuples) {
            //\\//\\ TO BE IMPLEMENTED //\\//\\
        	Comparable[] ptup = new Comparable [pAttribute.length];
        	for (int j = 0; j < colpos.length; j++){
        		ptup[j] = tup[colpos[j]];
        	}
        	result.tuples.add(ptup);
        	
        } // for

        return result;

    } // project


    /*************************************************************************************
     * Select the tuples satisfying the given condition.
     * A condition is written as infix expression consists of 
     *   6 comparison operators: "==", "!=", "<", "<=", ">", ">="
     *   2 Boolean operators:    "&", "|"  (from high to low precedence)
     * #usage   movie.select ("1979 < year & year < 1990")
     * @param   condition  Check condition for tuples.
     * @return  Table with tuples satisfying the condition.
     */
    public Table select (String condition)
    {
        //out.println ("" + name + " . select ( " + condition + " )");

        Table result = new Table (name + count++, attribute, domain);

        for (Comparable [] tup : tuples) {
            if (evalTup (condition, tup)) {
                result.tuples.add (tup);
            } // if
        } // for

        return result;

    } // select

    /**
     * Select the specified tuple
     * 
     * @param tup tuple for select
     * @return Table with tuple selected
     */
    public Table select (Comparable[] key){
    	//out.println("" + name + " . select by key");
    	
    	Table result = new Table(name + count++, attribute, domain);
    	
    	if (indexMap.get(key)!=null){
    		int index = indexMap.get(key);
        	result.tuples.add(tuples.get(index));
    	}
    	
		return result;
    }

    /*************************************************************************************
     * Union this table and table 2 without the consideration of duplication.
     * #usage   movie.union (show)
     * @param   table2  The rhs table in the union operation.
     * @return  Table representing the union.
     */
    public Table union (Table table2)
    {
        out.println ("" + name + " . union ( " + table2.name + " )");

        Table result = new Table (name + count++, attribute, domain);

        //\\//\\ TO BE IMPLEMENTED //\\//\\        
        for (Comparable [] tup : tuples){
        	result.tuples.add(tup);
        }
        
        for (Comparable [] tup : table2.tuples){
        	if (typeCheck(tup)){
        		result.tuples.add(tup);
        	}
        }

        return result;

    } // union
    
    /*************************************************************************************
     * Union this table and table 2 with the consideration of duplication elimination.
     * #usage   movie.union (show)
     * @param   table2  The rhs table in the union operation.
     * @return  Table representing the union.
     */
    public Table unionDupElim (Table table2)
    {
        out.println ("" + name + " . union ( " + table2.name + " )");

        Table result = new Table (name + count++, attribute, domain);

        //\\//\\ TO BE IMPLEMENTED //\\//\\
        HashMap<Comparable[], Comparable[]> map = new HashMap<Comparable[], Comparable[]>();
        for (Comparable [] tup : tuples){
        	result.tuples.add(tup);
        	map.put(tup, tup);
        }
        
        for (Comparable [] tup : table2.tuples){
        	if (typeCheck(tup) && !map.containsKey(tup)){
        		result.tuples.add(tup);
        	}
        }

        return result;

    } // union


    /*************************************************************************************
     * Take the difference of this table and table 2.
     * #usage   movie.minus (show)
     * @param   table2  The rhs table in the minus operation.
     * @return  Table representing the difference.
     */
    public Table minus (Table table2)
    {
        out.println ("" + name + " . minus ( " + table2.name + " )");

        Table result = new Table (name + count++, attribute, domain);

        //\\//\\ TO BE IMPLEMENTED //\\//\\
        HashMap<Comparable[], Comparable[]> map = new HashMap<Comparable[], Comparable[]>();
        for (Comparable[] tup : table2.tuples){
        	map.put(tup, tup);
        }
        
        for (Comparable[] tup : tuples){
        	if (typeCheck(tup) && !map.containsKey(tup)){
        		result.tuples.add(tup);
        	}
        }

        return result;

    } // minus


    /*************************************************************************************
     * Insert a tuple to the table.
     * #usage   movie.insert ("'Star_Wars'", 1977, 124, "T", "Fox", 12345)
     * @param   tuple    Array of attribute values.
     * @return  Whether insertion was successful.
     */
    public boolean insert (Comparable [] tup)
    {
        //out.println ("insert ( " + tup + " )");

        if (typeCheck (tup)) {
            tuples.add (tup);
            if (keys != null){
            	Comparable[] key = new Comparable[keys.length];
            	for (int i=0;i<key.length;i++){
            		key[i] = tup[columnPos(keys[i])];
            	}
            	indexMap.put(key, tuples.size()-1);
            }
            return true;
        } else {
            return false;
        } // if

    } // insert
    
    /**************************************************************************************
     * Insert a list of tuples to the table
     * #usage movie.insert(tuples)
     * @param tuples	List of tuples
     * @return	Whether all insertion was successful
     */
    public boolean insert (List<Comparable[]> tuples)
    {
    	//out.println ("insert " + tuples.size() + " tuples");
    	
    	boolean b = true;
    	Iterator<Comparable[]> t_itr = tuples.iterator();
    	
    	while (t_itr.hasNext()){
    		b = b && insert(t_itr.next());
    	}
    	
    	return b;
    } // insert
    
    /**
     * Simple Nest Loop Join which joins the current table instance with parameter table
     * by join condition parameter
     * @param condition the join condition
     * @param table the table to join with
     * @return the new joint table
     */
    public Table Join_NL(String condition, Table table){
    	// Construct Result Table
    	String[] attributes = new String[attribute.length + table.attribute.length];
    	Class[] domains = new Class[domain.length + table.domain.length];
    	for (int i=0;i<attributes.length;i++){
    		if (i<attribute.length){
    			attributes[i] = attribute[i];
    			domains[i] = domain[i];
    		}
    		else{
    			attributes[i] = table.attribute[i-attribute.length];
    			domains[i] = table.domain[i-domain.length];
    		}
    	}
    	Table result = new Table(name+count++,attributes,domains);
    	
    	// Decide inner outer
    	if (tuples.size() > table.tuples.size()){
    		// Do Nested Loop Join
        	for (Comparable[] tup1 : table.tuples){
        		for (Comparable[] tup2 : tuples){
        			if (evalJoinTup(condition,tup1,table,tup2)){
        				Comparable[] tup = new Comparable[tup1.length + tup2.length];
        				for (int i=0;i<tup.length;i++){
        					if (i < tup1.length){
        						tup[i] = tup1[i];
        					}
        					else{
        						tup[i] = tup2[i-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        			}
        		}
        	}
    	}
    	else{
    		// Do Nested Loop Join
        	for (Comparable[] tup1 : tuples){
        		for (Comparable[] tup2 : table.tuples){
        			if (evalJoinTup(condition,tup1,table,tup2)){
        				Comparable[] tup = new Comparable[tup1.length + tup2.length];
        				for (int i=0;i<tup.length;i++){
        					if (i < tup1.length){
        						tup[i] = tup1[i];
        					}
        					else{
        						tup[i] = tup2[i-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        			}
        		}
        	}
    	}
    	return result;
    }
    
    /**
     * Predict whether a condition A op B is a join condition on this table
     * and the table specified
     * @param condition		the condition A op B
     * @param table			the specified join table
     * @return				true if both A B are attributes on this table
     * 							 and the joint table respectively
     * 						false if either A or B is not an attribute on this
     * 							  table or the joint table respectively
     */
    private boolean isJoinCondition(String condition, Table table){
    	String[] postfix = infix2postfix(condition.split(" "));
    	if (columnPos(postfix[0]) == -1 || table.columnPos(postfix[1]) == -1){
    		return false;
    	}
    	else{
    		return true;
    	}
    }
    
    /**
     * Predict whether a given condition A op B is a condition specified table
     * @param condition		the condition A op B
     * @return				true if this is a condition on this table
     */
    private boolean isConditionOn(String condition){
    	String[] postfix = infix2postfix(condition.split(" "));
    	if (columnPos(postfix[0]) == -1){
    		return false;
    	}
    	else{
    		return true;
    	}
    }
    
    /**
     * Merge Join which joins current table instance with a parameter table
     * by join condition parameter
     * @param condition the join condition
     * @param table the table to join with
     * @return the new joint table
     */
    public Table Join_Merge(String condition, Table table){
    	if (condition.contains("&") && !condition.contains("|")){
    		String[] conditions = condition.split("&");
    		int[] types = new int[conditions.length];
    		
    		for (int i=0;i<conditions.length;i++){
    			conditions[i] = conditions[i].trim();
    			if (isJoinCondition(conditions[i],table)){
    				types[i] = 0;
    			}
    			else if (isConditionOn(conditions[i])){
    				types[i] = 1;
    			}
    			else if (table.isConditionOn(conditions[i])){
    				types[i] = 2;
    			}
    			else{
    				types[i] = 3;
    			}
    		}
    		
    		String cond_1 = "", cond_2 = "";
    		for (int i=0;i<types.length;i++){
    			if (types[i] == 1){
    				cond_1 += conditions[i] + " & ";
    			}
    			else if (types[i] == 2){
    				cond_2 += conditions[i] + " & ";
    			}
    		}
    		
    		Table table1 = select(cond_1);
    		Table table2 = table.select(cond_2);
    		
    		int first = 0;
    		String cond_sel = "";
    		
    		// Construct Result Table
    		String[] attributes = new String[attribute.length + table.attribute.length];
        	Class[] domains = new Class[domain.length + table.domain.length];
        	for (int i=0;i<attributes.length;i++){
        		if (i<attribute.length){
        			attributes[i] = attribute[i];
        			domains[i] = domain[i];
        		}
        		else{
        			attributes[i] = table.attribute[i-attribute.length];
        			domains[i] = table.domain[i-domain.length];
        		}
        	}
        	Table result = new Table(name+count++,attributes,domains);
        	
    		for (int i=0;i<types.length;i++){
    			if (types[i] == 0 && first == 0){
    				result = table1.Join_Merge(conditions[i], table2);
    				first++;
    			}
    			else if (types[i] == 0){
    				cond_sel += conditions[i] + " & ";
    			}
    		}
    		
    		if (cond_sel.equals("")){
    			return result;
    		}
    		else{
    			return result.select(cond_sel);
    		}
    	}
    	else if (condition.contains("|") && !condition.contains("&")){
    		String[] conditions = condition.split("|");
    		
    		// Construct Result Table
    		String[] attributes = new String[attribute.length + table.attribute.length];
        	Class[] domains = new Class[domain.length + table.domain.length];
        	for (int i=0;i<attributes.length;i++){
        		if (i<attribute.length){
        			attributes[i] = attribute[i];
        			domains[i] = domain[i];
        		}
        		else{
        			attributes[i] = table.attribute[i-attribute.length];
        			domains[i] = table.domain[i-domain.length];
        		}
        	}
        	Table result = new Table(name+count++,attributes,domains);
        	
        	for (int i=0;i<conditions.length;i++){
        		result = result.union(Join_Merge(conditions[i],table));
        	}
        	
        	return result;
    	}
    	else if (!condition.contains("&") && !condition.contains("|")){
    		// Extract condition for sorting
    		String[] postfix = infix2postfix(condition.split(" "));
    		String operator = postfix[2];

    		String attribute1 = postfix[0];
    		String attribute2 = postfix[1];
    		
    		int pos1 = columnPos(attribute1);
    		int pos2 = table.columnPos(attribute2);
    		
    		// Sort table1 by attribute in join condition (as A in r)
    		Comparable[] A = new Comparable[tuples.size()];
    		List<Comparable> Apos = new ArrayList<Comparable>();
    		for (int i=0;i<tuples.size();i++){
    			A[i] = tuples.get(i)[pos1];
    			Apos.add(A[i]);
    		}
    		Arrays.sort(A);
    		
    		List<Comparable[]> sortedTuples1 = new ArrayList<Comparable[]>();
    		for (int i=0;i<A.length;i++){
    			int idx = Apos.indexOf(A[i]);
    			sortedTuples1.add(tuples.get(idx));
    			Apos.set(idx,null);
    		}
    		
    		// Sort table2 by attribute in join condition (as B in t)
    		Comparable[] B = new Comparable[table.tuples.size()];
    		List<Comparable> Bpos = new ArrayList<Comparable>();
    		for (int i=0;i<table.tuples.size();i++){
    			B[i] = table.tuples.get(i)[pos2];
    			Bpos.add(B[i]);
    		}
    		Arrays.sort(B);
    		
    		List<Comparable[]> sortedTuples2 = new ArrayList<Comparable[]>();
    		for (int i=0;i<B.length;i++){
    			int idx = Bpos.indexOf(B[i]);
    			sortedTuples2.add(table.tuples.get(idx));
    			Bpos.set(idx, null);
    		}
    		
    		// Construct Result Table
    		String[] attributes = new String[attribute.length + table.attribute.length];
        	Class[] domains = new Class[domain.length + table.domain.length];
        	for (int i=0;i<attributes.length;i++){
        		if (i<attribute.length){
        			attributes[i] = attribute[i];
        			domains[i] = domain[i];
        		}
        		else{
        			attributes[i] = table.attribute[i-attribute.length];
        			domains[i] = table.domain[i-domain.length];
        		}
        	}
        	Table result = new Table(name+count++,attributes,domains);
    		
    		if (condition.contains("<")){
    			// from bottom to top
    			for (Comparable[] tup1:sortedTuples1){
    				int i = sortedTuples2.size()-1;
    				Comparable[] tup2 = sortedTuples2.get(i);
    				Comparable operand1 = tup1[pos1];
    				Comparable operand2 = tup2[pos2];
    				while (operand1.compareTo(operand2) < 0 && i >0){
    					Comparable[] tup = new Comparable[tup1.length + tup2.length];
        				for (int j=0;j<tup.length;j++){
        					if (j < tup1.length){
        						tup[j] = tup1[j];
        					}
        					else{
        						tup[j] = tup2[j-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        				i--;
        				if (i>=0){
        					tup2 = sortedTuples2.get(i);
        					operand2 = tup2[pos2];
        				}
    				}
    			}
    		}
    		else if (condition.contains("<=")){
    			// from bottom to top
    			for (Comparable[] tup1:sortedTuples1){
    				int i = sortedTuples2.size()-1;
    				Comparable[] tup2 = sortedTuples2.get(i);
    				Comparable operand1 = tup1[pos1];
    				Comparable operand2 = tup2[pos2];
    				while (operand1.compareTo(operand2) <= 0 && i >0){
    					Comparable[] tup = new Comparable[tup1.length + tup2.length];
        				for (int j=0;j<tup.length;j++){
        					if (j < tup1.length){
        						tup[j] = tup1[j];
        					}
        					else{
        						tup[j] = tup2[j-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        				i--;
        				if (i>=0){
        					tup2 = sortedTuples2.get(i);
        					operand2 = tup2[pos2];
        				}
    				}
    			}
    		}
    		else if (condition.contains(">")){
    			// from top to bottom
    			for (Comparable[] tup1:sortedTuples1){
    				int i = 0;
    				Comparable[] tup2 = sortedTuples2.get(i);
    				Comparable operand1 = tup1[pos1];
    				Comparable operand2 = tup2[pos2];
    				while (operand1.compareTo(operand2) > 0 && i < sortedTuples2.size()){
    					Comparable[] tup = new Comparable[tup1.length + tup2.length];
    					for (int j=0;j<tup.length;j++){
        					if (j < tup1.length){
        						tup[j] = tup1[j];
        					}
        					else{
        						tup[j] = tup2[j-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        				i++;
        				if (i<sortedTuples2.size()){
        					tup2 = sortedTuples2.get(i);
        					operand2 = tup2[pos2];
        				}
    				}
    			}
    		}
    		else if (condition.contains(">=")){
    			// from top to bottom
    			for (Comparable[] tup1:sortedTuples1){
    				int i = 0;
    				Comparable[] tup2 = sortedTuples2.get(i);
    				Comparable operand1 = tup1[pos1];
    				Comparable operand2 = tup2[pos2];
    				while (operand1.compareTo(operand2) >= 0 && i < sortedTuples2.size()){
    					Comparable[] tup = new Comparable[tup1.length + tup2.length];
    					for (int j=0;j<tup.length;j++){
        					if (j < tup1.length){
        						tup[j] = tup1[j];
        					}
        					else{
        						tup[j] = tup2[j-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        				i++;
        				if (i<sortedTuples2.size()){
        					tup2 = sortedTuples2.get(i);
        					operand2 = tup2[pos2];
        				}
    				}
    			}
    		}
    		else if (condition.contains("==")){
    			// from break to bottom
    			int i=0, j=0;
    			while (i<sortedTuples1.size() && j<sortedTuples2.size()){
    				Comparable[] tup1 = sortedTuples1.get(i);
    				Comparable[] tup2 = sortedTuples2.get(j);
    				if (tup1[pos1].compareTo(tup2[pos2]) < 0){
    					// less than
    					i++;
    				}
    				else if (tup1[pos1].compareTo(tup2[pos2]) > 0){
    					// greater than
    					j++;
    				}
    				else{
    					// equal
    					Comparable[] tup = new Comparable[tup1.length + tup2.length];
    					for (int k=0;k<tup.length;k++){
        					if (k < tup1.length){
        						tup[k] = tup1[k];
        					}
        					else{
        						tup[k] = tup2[k-tup1.length];
        					}
        				}
        				result.tuples.add(tup);
        				i++;
    				}
    			}
    		}
    		else{
    			return Join_NL(condition,table);
    		}
    		return result;
    	}
    	else{
    		return Join_NL(condition,table);
    	}
    }
    
    /**
     * Hash Join which joins current table instance with a parameter table
     * by join condition parameter
     * @param condition the join condition
     * @param table the table to join with
     * @return the new joint table
     */
    public Table Join_Hash(String condition, Table table){
    	// Construct Result Table
		String[] attributes = new String[attribute.length + table.attribute.length];
    	Class[] domains = new Class[domain.length + table.domain.length];
    	for (int i=0;i<attributes.length;i++){
    		if (i<attribute.length){
    			attributes[i] = attribute[i];
    			domains[i] = domain[i];
    		}
    		else{
    			attributes[i] = table.attribute[i-attribute.length];
    			domains[i] = table.domain[i-domain.length];
    		}
    	}
    	Table result = new Table(name+count++,attributes,domains);
    	
    	if (condition.contains(">") || condition.contains("<") || condition.contains(">=") || condition.contains("<=") || condition.contains("!=") || condition.contains("|")){
    		return Join_Merge(condition,table);
    	}
    	else{
    		List<String[]> attripairs = parseConditionOnKey(condition);
    		List<String> attributes1 = new ArrayList<String>();
    		List<String> attributes2 = new ArrayList<String>();
    		
    		for (int i=0;i<attripairs.size();i++){
    			attributes1.add(attripairs.get(i)[0]);
    			attributes2.add(attripairs.get(i)[1]);
    		}
    		
    		HashMap<Comparable[],Integer> map = new HashMap<Comparable[],Integer>();
    		
    		if (tuples.size() < table.tuples.size()){
    			// Hash on this table (attributes, position)
    			for (Comparable[] tup:tuples){
    				Comparable[] hashkey = new Comparable[attributes1.size()];
    				for (int i=0;i<hashkey.length;i++){
    					hashkey[i] = tup[columnPos(attributes1.get(i))];
    				}
    				map.put(hashkey, tuples.indexOf(tup));
    			}
    			
    			// Probe on parameter table
    			for (Comparable[] tup2:table.tuples){
    				Comparable[] hashkey = new Comparable[attributes1.size()];
       				for (int i=0;i<hashkey.length;i++){
    					hashkey[i] = tup2[table.columnPos(attributes2.get(i))];
    				}
       				Set<Comparable[]> keys = map.keySet();
       				Iterator<Comparable[]> k_itr = keys.iterator();
       				while (k_itr.hasNext()){
       					Comparable[] key = k_itr.next();
       					if (Arrays.equals(key, hashkey)){
       						Comparable[] tup1 = tuples.get(map.get(key));
            				Comparable[] tup = new Comparable[tup1.length + tup2.length];
        					for (int k=0;k<tup.length;k++){
            					if (k < tup1.length){
            						tup[k] = tup1[k];
            					}
            					else{
            						tup[k] = tup2[k-tup1.length];
            					}
            				}
            				result.tuples.add(tup);
       					}
       				}
    			}
    			return result;
    		}
    		else{
    			// Hash on parameter table (attributes, position)
    			for (Comparable[] tup:table.tuples){
    				Comparable[] hashkey = new Comparable[attributes2.size()];
    				for (int i=0;i<hashkey.length;i++){
    					hashkey[i] = tup[table.columnPos(attributes2.get(i))];
    				}
    				map.put(hashkey, table.tuples.indexOf(tup));
    			}
    			
    			// Probe on this table
    			for (Comparable[] tup1:tuples){
    				Comparable[] hashkey = new Comparable[attributes1.size()];
    				for (int i=0;i<hashkey.length;i++){
    					hashkey[i] = tup1[columnPos(attributes1.get(i))];
    				}
    				Set<Comparable[]> keys = map.keySet();
    				Iterator<Comparable[]> k_itr = keys.iterator();
    				while (k_itr.hasNext()){
    					Comparable[] key = k_itr.next();
    					if (Arrays.equals(key, hashkey)){
    						Comparable[] tup2 = table.tuples.get(map.get(key));
            				Comparable[] tup = new Comparable[tup1.length + tup2.length];
        					for (int k=0;k<tup.length;k++){
            					if (k < tup1.length){
            						tup[k] = tup1[k];
            					}
            					else{
            						tup[k] = tup2[k-tup1.length];
            					}
            				}
            				result.tuples.add(tup);
    					}
    				}
    			}
    			return result;
    		}
    	}
    }
    
    /**
     * Index Join which joins current table instance and a parameter table on given conditions
     * @param condition	the given conditions
     * @param table		the parameter table
     * @return			the joint table
     */
    public Table Join_Index(String condition, Table table){
    	// Construct Result Table
    	String[] attributes = new String[attribute.length + table.attribute.length];
    	Class[] domains = new Class[domain.length + table.domain.length];
    	for (int i=0;i<attributes.length;i++){
    		if (i<attribute.length){
    			attributes[i] = attribute[i];
    			domains[i] = domain[i];
    		}
    		else{
    			attributes[i] = table.attribute[i-attribute.length];
    			domains[i] = table.domain[i-domain.length];
    		}
    	}
    	Table result = new Table(name+count++,attributes,domains);
    	
    	if (condition.contains(">") || condition.contains("<") || condition.contains(">=") || condition.contains("<=") || condition.contains("!=") || condition.contains("|")){
    		return Join_Merge(condition,table);
    	}
    	else{
    		List<String[]> attripairs = parseConditionOnKey(condition);
    		List<String> attributes1 = new ArrayList<String>();
    		List<String> attributes2 = new ArrayList<String>();
    		
    		for (int i=0;i<attripairs.size();i++){
    			attributes1.add(attripairs.get(i)[0]);
    			attributes2.add(attripairs.get(i)[1]);
    		}
    		
    		if (keys != null && attributesOnKey(attributes1)){
    			// inner loop is this table
    			for (Comparable[] tup2:table.tuples){
    				Comparable[] key2 = new Comparable[keys.length];
    				for (int i=0;i<keys.length;i++){
    					key2[i] = tup2[table.columnPos(attributes2.get(attributes1.indexOf(keys[i])))];
    				}
    				
    				List<Comparable[]> keySet = indexMap.keyList();
    				for (int i=0;i<keySet.size();i++){
    					Comparable[] key1 = keySet.get(i);
    					if (Arrays.equals(key1, key2)){
    						Comparable[] tup1 = tuples.get(indexMap.get(key1));
    						Comparable[] tup = new Comparable[tup1.length + tup2.length];
            				for (int k=0;k<tup.length;k++){
            					if (k < tup1.length){
            						tup[k] = tup1[k];
            					}
            					else{
            						tup[k] = tup2[k-tup1.length];
            					}
            				}
            				result.tuples.add(tup);
    					}
    				}
    			}
    			return result;
    		}
    		else if (table.keys != null && table.attributesOnKey(attributes2)){
    			// inner loop is parameter table
    			for (Comparable[] tup1:tuples){
    				Comparable[] key1 = new Comparable[table.keys.length];
    				for (int i=0;i<keys.length;i++){
    					key1[i] = tup1[columnPos(attributes1.get(attributes2.indexOf(keys[i])))];
    				}
    				List<Comparable[]> keySet = table.indexMap.keyList();
    				for (int i=0;i<keySet.size();i++){
    					Comparable[] key2 = keySet.get(i);
    					if (Arrays.equals(key1, key2)){
    						Comparable[] tup2 = table.tuples.get(indexMap.get(key1));
    						Comparable[] tup = new Comparable[tup1.length + tup2.length];
            				for (int k=0;k<tup.length;k++){
            					if (k < tup1.length){
            						tup[k] = tup1[k];
            					}
            					else{
            						tup[k] = tup2[k-tup1.length];
            					}
            				}
            				result.tuples.add(tup);
    					}
    				}
    			}
    			return result;
    		}
    		else{
    			return Join_Merge(condition,table);
    		}
    	}
    }
    
    /**
     * Parsing condition in forms r.A == s.B & r.C == s.D & .... & r.Y == S.Z
     * @param condition		the condition to be parsed
     * @return				list of string array each contains two attribute names
     */
    private List<String[]> parseConditionOnKey(String condition){
    	List<String[]> result = new ArrayList<String[]>();
    	
    	condition = condition.concat(" &");
    	String[] infix = condition.split(" ");
    	
    	String first = "";
    	String second = "";
    	for (int i=0;i<infix.length;i++){
    		switch (i%4){
    		case 0:
    			first = infix[i];
    			break;
    			
    		case 1:
    			break;
    			
    		case 2:
    			second = infix[i];
    			break;
    			
    		case 3:
    			result.add(new String[]{first,second});
    			break;
    		}
    	}
    	return result;
    }
    
    /**
     * Predict whether a given set of attributes is a key
     * @param attributes	the given set of attributes
     * @return				true if they are defines as a key
     */
    private boolean attributesOnKey(List<String> attributes){
    	if (attributes.size() != keys.length){
    		return false;
    	}
    	boolean result = true;
    	for (int i=0;i<keys.length;i++){
    		result = result && attributes.contains(keys[i]);
    	}
    	return result;
    }
    
    /**************************************************************************************
     * Clear up the table
     */
    public void clear(){
    	this.tuples.removeAll(tuples);
    }


    /*************************************************************************************
     * Print the table.
     */
    public void print ()
    {
        out.println (" Table " + name);
        for (String a : attribute) {
            out.print (" " + a + " ");
        } // for
        out.println ();
        for (Comparable [] tup : tuples) {
            out.print ("[");
            for (Comparable attr : tup) {
                out.print (attr + " ");
            } // for
            out.println ("]");
        } // for

    } // print


    /*************************************************************************************
     * Get the column position for the given attribute name.
     * @param   attr  The given attribute name.
     * @return  The column position.
     */
    private int columnPos (String attr)
    {
        for (int i = 0; i < attribute.length; i++) {
           if (attribute[i].equals(attr)) {
               return i;
           } // if
        } // for

        return -1;  // not found

    } // columnPos
    
    private List<Integer> columnPoses (String attr){
    	List<Integer> poses = new ArrayList<Integer>();
    	for (int i = 0; i < attribute.length; i++) {
            if (attribute[i].equals(attr)) {
                poses.add(i);
            }
         }
    	return poses;
    }
    
    
    private boolean evalJoinTup (String condition, Comparable[] tup1, Table table, Comparable[] tup2){
    	if (condition == null || condition == "") {
            return true;
        } // if

    	//System.out.println(condition);
        //String [] postfix = infix2postfix (condition.split (" "));
        String[] postfix = {"ID","stuID","<"};
    	
        for (int i = 0;i<postfix.length;i++){
        	System.out.println(postfix[i]);
        }
               
        Stack<Object> s   = new Stack<Object> ();

        s.push (Boolean.TRUE);
        
        Comparable[] operands = new Comparable[2];
        boolean operandIdx = true;
        for (int i = 0; i < postfix.length; i++){
        	String expr = postfix[i];
        	
        	if (getPrecedence(expr) == 0){
        		if (operandIdx){
        			int colpos = columnPos(expr);
        			if (colpos != -1){
        				Class c = domain[colpos];
        				operands[0] = (Comparable) c.cast(tup1[colpos]);
        			}
        			else{
        				operands[0] = (Comparable)(expr);
        			}
        		}
        		else{
        			int colpos = table.columnPos(expr);
        			if (colpos != -1){
        				Class c = table.domain[colpos];
        				operands[1] = (Comparable) c.cast(tup2[colpos]);
        			}
        			else{
        				operands[1] = (Comparable)(expr);
        			}
        		}
        		operandIdx = !operandIdx;
        	}
        	else if (getPrecedence(postfix[i]) == 1){
        		int result = (operands[0]).compareTo(operands[1]);

        		if (expr.equals("==") && result == 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("!=") && result != 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals(">=") && result >= 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("<=") && result <= 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals(">") && result > 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("<") && result < 0){
        			s.push(Boolean.TRUE);
        		}
        		else{
        			s.push(Boolean.FALSE);
        		}
        	}
        	else if (getPrecedence(postfix[i]) == 2){
        		boolean first = (Boolean)s.pop();
    			boolean second = (Boolean)s.pop();
        		if (expr.equals("&")){
        			s.push((Boolean)(first && second));
        		}
        		else if (expr.equals("|")){
        			s.push((Boolean)(first || second));
        		}
        		else{
        			s.push(Boolean.FALSE);
        		}
        	}
        }
        
        while (s.size() > 1){
        	boolean first = (Boolean)s.pop();
			boolean second = (Boolean)s.pop();
        	s.push((Boolean)(first && second));
        }

        return (Boolean) s.pop ();
    }


    /*************************************************************************************
     * Check whether the tuple satisfies the condition.
     * @param   condition  The infix expression for the condition
     * @param   tup        The tuple to check.
     * @return  Whether to keep the tuple.
     */
    private boolean evalTup (String condition, Comparable [] tup)
    {
        if (condition == null || condition == "") {
            return true;
        } // if

        String [] postfix = infix2postfix (condition.split (" "));
               
        Stack<Object> s   = new Stack<Object> ();

        //\\//\\ TO BE IMPLEMENTED //\\//\\
        s.push (Boolean.TRUE);
        
        Comparable[] operands = new Comparable[2];
        boolean operandIdx = true;
        for (int i = 0; i < postfix.length; i++){
        	String expr = postfix[i];
        	
        	if (getPrecedence(expr) == 0){
        		int lastpos = -1;
        		if (operandIdx){
        			int colpos = columnPos(expr);
        			lastpos = colpos;
        			if (colpos != -1){
        				Class c = domain[colpos];
        				operands[0] = (Comparable) c.cast(tup[colpos]);
        			}
        			else{
        				operands[0] = (Comparable)(expr);
        			}
        		}
        		else{
        			int colpos = columnPos(expr);
        			if (colpos != -1){
        				List<Integer> poses = columnPoses(expr);
        				if (poses.size() > 1)
        					colpos = poses.get(1);
        				Class c = domain[colpos];
        				operands[1] = (Comparable) c.cast(tup[colpos]);
        			}
        			else{
        				if (expr.charAt(0) == '\''){
            				expr = expr.substring(1, expr.length()-1);
            			}
            			try{
            				Class cls = operands[0].getClass();
            				Constructor cons = cls.getConstructor(String.class);
            				
            				operands[1] = (Comparable) (cons.newInstance(expr));
            			}
            			catch (Exception e){
            				e.printStackTrace();
            			}
        			}
        		}
        		operandIdx = !operandIdx;
        	}
        	else if (getPrecedence(postfix[i]) == 1){
        		int result = (operands[0]).compareTo(operands[1]);

        		if (expr.equals("==") && result == 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("!=") && result != 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals(">=") && result >= 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("<=") && result <= 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals(">") && result > 0){
        			s.push(Boolean.TRUE);
        		}
        		else if (expr.equals("<") && result < 0){
        			s.push(Boolean.TRUE);
        		}
        		else{
        			s.push(Boolean.FALSE);
        		}
        	}
        	else if (getPrecedence(postfix[i]) == 2){
        		boolean first = (Boolean)s.pop();
    			boolean second = (Boolean)s.pop();
        		if (expr.equals("&")){
        			s.push((Boolean)(first && second));
        		}
        		else if (expr.equals("|")){
        			s.push((Boolean)(first || second));
        		}
        		else{
        			s.push(Boolean.FALSE);
        		}
        	}
        }
        
        while (s.size() > 1){
        	boolean first = (Boolean)s.pop();
			boolean second = (Boolean)s.pop();
        	s.push((Boolean)(first && second));
        }

        return (Boolean) s.pop ();

    } // evalTup


    /*************************************************************************************
     * Convert an infix expression to a postfix expression.  This implementation does not
     * handle parentheses.
     * @param   infix  A tokenized infix expression.
     * @return  The resultant tokenized postfix expression.
     */
    private String [] infix2postfix (String [] infix)
    {
        String [] postfix = new String [infix.length];

        //\\//\\ TO BE IMPLEMENTED //\\//\\
        
        Stack<String> operator = new Stack<String> ();
        int j = 0;
        
        for (int i = 0; i < infix.length; i++){
        	if (getPrecedence(infix[i]) == 0){
        		postfix[j] = infix[i];
        		j++;
        	}
        	else{
        		if (operator.size() == 0){
        			operator.push(infix[i]);
        		}
        		else{
        			boolean loopsig = true;
        			while (loopsig && operator.size() > 0){
        				String top = operator.peek();
        				if (getPrecedence(top) > getPrecedence(infix[i])){
        					loopsig = false;
        				}
        				else{
        					postfix[j] = operator.pop();
        					j++;
        				}
        			}
        			operator.push(infix[i]);        			
        		}// else
        	}// else
        }
        
        while (operator.size() != 0){
        	postfix[j] = operator.pop();
        	j++;
        }
        
        return postfix;

    } // infix2postfix
    
    /*************************************************************************************
     * Predict the precedence of an symbol.
     * This implementation deals with all other symbols except following operators as the
     * lowest precedence.
     * This implementation deals with "==" "!=" ">=" "<=" ">" "<" as lower precedence.
     * This implementation deals with "&" "|" as higher precedence.
     * @param   symbol  A symbol.
     * @return  The resultant precedence of a symbol.
     */
    private int getPrecedence (String symbol)
    {
        int precedence = 0;
        
        //\\//\\ TO BE IMPLEMENTED //\\//\\
        if (symbol.equals("==")){
        	precedence = 1;
        }
        else if (symbol.equals("!=")){
        	precedence = 1;
        }
        else if (symbol.equals("<=")){
        	precedence = 1;
        }
        else if (symbol.equals(">=")){
        	precedence = 1;
        }
        else if (symbol.equals(">")){
        	precedence = 1;
        }
        else if (symbol.equals("<")){
        	precedence = 1;
        }
        else if (symbol.equals("&")){
        	precedence = 2;
        }
        else if (symbol.equals("|")){
        	precedence = 2;
        }
        else{
        	precedence = 0;
        }
        
        return precedence;

    } // getPrecedence


    /*************************************************************************************
     * Match the column and attribute names to determine the domains.
     * @param   column  Array of column names.
     * @return  Array of column index position.
     */
    private int [] match (String [] column)
    {
        int [] colPos = new int [column.length];

        for (int j = 0; j < column.length; j++) {
            boolean matched = false;
            for (int k = 0; k < attribute.length; k++) {
                if (column [j].equals (attribute [k])) {
                    matched = true;
                    colPos [j] = k;
                } // for
            } // for
            if ( ! matched) {
                out.println ("match: domain not found for " + column [j]);
            } // if
        } // for

        return colPos;

    } // match


    /*************************************************************************************
     * Check the size of the tuple (number of elements in list) as well as the type of
     * each value to ensure it is from the right domain. 
     * @param   tup  The tuple as a list of attribute values.
     * @return  Whether the tuple has the right size and values that comply
     *          with the given domains.
     */
    private boolean typeCheck (Comparable [] tup)
    { 

        //\\//\\ TO BE IMPLEMENTED //\\//\\
    	if (tup.length != attribute.length){
    		return false;
    	}
    	else{
    		for (int i = 0; i < domain.length; i++){
    			if (!tup[i].getClass().equals(domain[i])){
    				return false;
    			}
    		}
    	}

        return true;
    
    } // typeCheck
 

    /*************************************************************************************
     * Find the classes in the "java.lang" package with given names.
     * @param   className  Array of class name (e.g., {"Integer", "String"})
     * @return  Array of Java classes.
     */
    private Class [] findClass (String [] className)
    {
        Class [] classArray = new Class [className.length];

        for (int i = 0; i < className.length; i++) {
            try {
                classArray [i] = Class.forName ("java.lang." + className [i]);
            } catch (ClassNotFoundException ex) {
                out.println ("findClass: " + ex);
            } // try
        } // for

        return classArray;

    } // findClass


    /*************************************************************************************
     * Extract the corresponding domains.
     * @param   colPos     Column positions to extract.
     * @param   group      Where to extract from.
     * @return  The extracted domains.
     */
    private Class [] extractDom (int [] colPos, Class [] group)
    {
        Class [] obj = new Class [colPos.length];

        for (int j = 0; j < colPos.length; j++) {
            obj [j] = group [colPos [j]];
        } // for

        return obj;

    } // extractDom

} // Table class

