import scala.collection.mutable.Set;
import scala.util.Random;

class Schedule (s:List[Tuple3[Char,Int,Int]]){
	
	def isCSR(nTrans:Int):Boolean={
		val pg = new PrecedenceGraph(nTrans);
		for (si <- s;sj <- s if (si._1 == 'w' || sj._1 == 'w') && si._2 != sj._2 && si._3 == sj._3)
					pg.addEdge(si._2, sj._2);
		!pg.hasCycle;
	}
	
	def isVSR(nTrans:Int):Boolean={
		//if (isCSR(nTrans)) return true;
		
		var pg = new PolyGraph(nTrans+2);
		var its:List[Tuple3[Int,Int,Int]] = List();
		
		var lwn = 0;
		var lwc = -1;
		var lastw = true;
		for (si <- s){
			if (si._1 == 'w') {
				lwn = si._2+1;
				lwc = si._3;
				lastw = true;
			}
			else {
				// a read operation
				if (si._2+1 != lwn){
					if (lwc == -1 || si._3 == lwc){
						its ++= List(Tuple3(lwn,si._2+1,si._3));
					}
					lastw = false;
				}
			}
		}
		if (lastw) its ++= List(Tuple3(lwn,nTrans+1,lwc));
		
		for (it <- its){
			for (si <- s){
				if (si._1 == 'w' && (si._2+1 != it._1 && si._2+1 != it._2) && si._3 == it._3 ){
					if (it._1 == 0){
						// not able to go before
						pg.addEdge(it._2, si._2+1);
					}
					else if (it._2 == nTrans+1){
						// not able to go after
						pg.addEdge(si._2+1, it._1);
					}
					else{
						pg.addPolyEdgeSet(Set(Tuple2(it._2,si._2+1),Tuple(si._2+1,it._1)));
					}
				}
			}
		}
		!pg.hasCycle;
	}
	
	def genOperations(transID:Int,nObject:Int):Tuple3[Char,Int,Int]={
		var rand:Random = new Random();
		if (rand.nextBoolean){
			return Tuple3('r',transID,rand.nextInt(nObject));
		}
		else{
			return Tuple3('w',transID,rand.nextInt(nObject));
		}
		null;
	}
	
	def genSchedule(nTrans:Int,nOperations:Int,nObject:Int):Schedule={
		var result:List[Tuple3[Char,Int,Int]] = List();
		var rand:Random = new Random();
		for (i <- 0 until nOperations){
			result :+= genOperations(rand.nextInt(nTrans),nObject);
		}
		new Schedule(result);
	}
	
	def genTransaction(transID:Int,nOperations:Int,nObject:Int):Schedule={
		var result:List[Tuple3[Char,Int,Int]] = List();
		var rand:Random = new Random();
		for (i <- 0 until nOperations){
			result :+= genOperations(transID,nObject);
		}
		new Schedule(result);
	}
	
	def foreach [U] (f: Tuple3 [Char, Int, Int] => U)
    {
        s.foreach (f)
    }
	
	override def toString: String =
    {
        "Schedule ( " + s + " )"
    }
}

object ScheduleTest extends Application with ReadWrite
{
    val s1 = new Schedule (List ( (r, 0, 0), (r, 1, 0), (w, 0, 0), (w, 1, 0) ))
    val s2 = new Schedule (List ( (r, 0, 0), (r, 1, 1), (w, 0, 0), (w, 1, 1) ))

    println ("s1 = " + s1 + " is CSR? " + s1.isCSR (2))
    println ("s1 = " + s1 + " is VSR? " + s1.isVSR (2))
    println ("s2 = " + s2 + " is CSR? " + s2.isCSR (2))
    println ("s2 = " + s2 + " is VSR? " + s2.isVSR (2))
    
    val s3 = s1.genSchedule(10, 30, 5);
    println ("s3 = " + s3 + " is CSR? " + s3.isCSR (10))
    println ("s3 = " + s3 + " is VSR? " + s3.isVSR (10))

} 