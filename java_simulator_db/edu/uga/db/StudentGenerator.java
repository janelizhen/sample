package edu.uga.db;

import java.util.*;

/**
 * @author zhen li
 * @version 0.1
 */
public class StudentGenerator {
	static String boysNames[] = {"Jacob", "Michael", "Joshua", "Matthew", "Ethan", "Andrew", "Daniel", "Anthony", "Christopher", "Joseph", "William", "Alexander", "Ryan", "David", "Nicholas", "Tyler", "James", "John", "Jonathan", "Nathan", "Samuel", "Christian", "Noah", "Dylan", "Benjamin", "Logan", "Brandon", "Gabriel", "Zachary", "Jose"};
	static String girlsNames[] = {"Emily", "Emma", "Madison", "Olivia", "Hannah", "Abigail", "Isabella", "Ashley", "Samantha", "Elizabeth", "Alexis", "Sarah", "Grace", "Alyssa", "Sophia", "Lauren", "Brianna", "Kayla", "Natalie", "Anna", "Jessica", "Taylor", "Chloe", "Hailey", "Ava", "Jasmine", "Sydney", "Victoria", "Ella", "Mia"};
	static String lastNames[] = {"Smith", "Johnson", "Jones", "Brown", "Williams", "Miller", "Wilson", "Davis", "Hall", "Moore", "Taylor", "Thomas", "Thompson", "Martin", "Clark", "Jackson", "Adams", "Baker", "Young", "Lewis", "Harris", "Howard", "Turner", "Allen", "Wright", "White", "Collins", "Roberts", "Robinson", "King", "Campbell", "Anderson", "Carter", "Cox", "Mitchell", "Scott", "Hamilton", "Walker", "Combs", "Evans"};
	static String deptNames[] = {"Art", "Physics", "Chemistry", "Geography", "History", "Mathematics", "Computer Science", "Psychology", "Accounting", "Economics", "Finance", "International Business", "Management", "Marketing", "Engineering", "Biology"};
	
	public static List<Comparable[]> getTuples(int numOfTuples){
			Random ranGen = new Random();
			int id, age, year;
			String name, gender, dept;
			List<Comparable[]> results = new ArrayList<Comparable[]>();
			
			for (int i = 0; i < numOfTuples; i++) {

				id = i; //id
			
				int tmp = ranGen.nextInt(2);
				if (tmp == 0) {
					gender = "male";
					name = boysNames[ranGen.nextInt(30)] + " " + boysNames[ranGen.nextInt(30)] + " " + lastNames[ranGen.nextInt(40)]; 				
				}
				else { 
					gender = "female";
					name = girlsNames[ranGen.nextInt(30)] + " " + girlsNames[ranGen.nextInt(30)] + " " + lastNames[ranGen.nextInt(40)];
				}
				age = ranGen.nextInt(10) + 18; //age
				dept = deptNames[ranGen.nextInt(16)]; //dept
				year = ranGen.nextInt(4) + 1; //year
				
				Comparable[] tuple = new Comparable[6];
				for (int j=0;j<tuple.length;j++){
					switch(j){
					case 0:
						tuple[j] = id;
						break;
						
					case 1:
						tuple[j] = name;
						break;
						
					case 2:
						tuple[j] = gender;
						break;
						
					case 3:
						tuple[j] = age;
						break;
						
					case 4:
						tuple[j] = dept;
						break;
						
					case 5:
						tuple[j] = year;
						break;
					}
				}

				results.add(tuple);
			}
			return results;
	}
}
