import java.sql.*;
import java.util.*;

public class Oracle {

	/**
	 * @param args
	 */
	String boysNames[] = {"Jacob", "Michael", "Joshua", "Matthew", "Ethan", "Andrew", "Daniel", "Anthony", "Christopher", "Joseph", "William", "Alexander", "Ryan", "David", "Nicholas", "Tyler", "James", "John", "Jonathan", "Nathan", "Samuel", "Christian", "Noah", "Dylan", "Benjamin", "Logan", "Brandon", "Gabriel", "Zachary", "Jose"};
	String girlsNames[] = {"Emily", "Emma", "Madison", "Olivia", "Hannah", "Abigail", "Isabella", "Ashley", "Samantha", "Elizabeth", "Alexis", "Sarah", "Grace", "Alyssa", "Sophia", "Lauren", "Brianna", "Kayla", "Natalie", "Anna", "Jessica", "Taylor", "Chloe", "Hailey", "Ava", "Jasmine", "Sydney", "Victoria", "Ella", "Mia"};
	String lastNames[] = {"Smith", "Johnson", "Jones", "Brown", "Williams", "Miller", "Wilson", "Davis", "Hall", "Moore", "Taylor", "Thomas", "Thompson", "Martin", "Clark", "Jackson", "Adams", "Baker", "Young", "Lewis", "Harris", "Howard", "Turner", "Allen", "Wright", "White", "Collins", "Roberts", "Robinson", "King", "Campbell", "Anderson", "Carter", "Cox", "Mitchell", "Scott", "Hamilton", "Walker", "Combs", "Evans"
	};
	String deptNames[] = {"Art", "Physics", "Chemistry", "Geography", "History", "Mathematics", "Computer Science", "Psychology", "Accounting", "Economics", "Finance", "International Business", "Management", "Marketing", "Engineering", "Biology"};
	
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String user = "system";
	String password = "lydia99";
	Connection connection;
	
	public void connectToDB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			connection = DriverManager.getConnection(url, user, password);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}
	
	public void tupleGenerator(int numOfTuples)
	{
		try {

			Statement statement = connection.createStatement();

			//statement.executeUpdate("DROP TABLE STUDENT");
			statement.executeUpdate("CREATE TABLE STUDENT"
					+ "(ID NUMERIC(10), NAME VARCHAR2(40), GENDER VARCHAR2(6), AGE NUMERIC(2), DEPT VARCHAR2(30), YEAR NUMERIC(1))");

			Random ranGen = new Random();
			int id, age, year;
			String name, gender, dept;
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

				statement.executeUpdate("INSERT INTO STUDENT VALUES"
						+ "(" + String.valueOf(id) + ","
						+ "'" + name + "'"  + ","
						+ "'" + gender + "'"  + ","
						+ String.valueOf(age)  + ","
						+ "'" + dept + "'"  + ","
						+ String.valueOf(year) + ")");	
			}
			
			statement.close();

		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}
	
	private void displaySQLErrors(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("VendorError: " + e.getErrorCode());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Oracle m = new Oracle();
		m.connectToDB();
		m.tupleGenerator(10000);
		m.closeConnection();

	}

}