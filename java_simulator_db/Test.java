import edu.uga.db.*;
import edu.uga.db.sql.Range;

import java.util.*;

/**
 * @author Li Zhen
 * @version 0.1
 */
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Table student = new Table("Student","ID NAME GENDER AGE DEPT YEAR","Integer String String Integer String Integer","ID");
		Table student1 = new Table("Student","ID NAME GENDER AGE DEPT YEAR","Integer String String Integer String Integer","ID");
		Table student2 = new Table("Student","stuID NAME GENDER AGE DEPT YEAR","Integer String String Integer String Integer","stuID");
		
		List<Comparable[]> students = StudentGenerator.getTuples(10000);
		List<Comparable[]> students1 = StudentGenerator.getTuples(10000);
		List<Comparable[]> students2 = StudentGenerator.getTuples(10000);
		
		student.insert(students);
		student1.insert(students1);
		student2.insert(students2);
		
		long start = 0;
		
		start = System.currentTimeMillis();
		student.project("ID");
		System.out.println("SELECT ID FROM STUDENT:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student.project("DEPT");
		System.out.println("SELECT DEPT FROM STUDENT:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student1.union(student2);
		System.out.println("SELECT * FROM STUDENT1 UNION SELECT * FROM STUDENT2:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student1.minus(student2);
		System.out.println("SELECT * FROM STUDENT1 UNION SELECT * FROM STUDENT2:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student.select("ID == 1234");
		System.out.println("SELECT * FROM STUDENT WHERE ID = 1234:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student.select("ID > 1234 & ID < 1245");
		System.out.println("SELECT * FROM STUDENT WHERE ID > 1234 AND ID < 1245:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student.select("ID > 1234 & ID < 1289 & DEPT == 'Chemistry'");
		System.out.println("SELECT * FROM STUDENT WHERE ID > 1234 AND ID < 1289 AND DEPT = 'Chemistry':\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student.select("ID > 1234 & ID < 1245 | ID > 3456 & ID < 3478");
		System.out.println("SELECT * FROM STUDENT WHERE (ID > 1234 AND ID < 1245) OR (ID > 3456 AND ID < 3478):\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student1.Join_Merge("ID == stuID", student2);
		System.out.println("SELECT * FROM STUDENT1 S1, STUDENT2 S2 WHERE S1.ID = S2.ID:\t\t" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		student1.Join_Merge("ID < stuID & ID > 1234 & ID < 1245 & stuID < 1245", student2);
		System.out.println("SELECT * FROM STUDENT S1, STUDENT S2 WHERE S1.ID < S2.ID AND S1.ID > 1234 AND S1.ID < 1245 AND S2.ID < 1245:\t\t" + (System.currentTimeMillis() - start));
	}
}
