object Database {
	val lt = new LockTable();
	val data = new Array[Double](100);
}

class Transaction (tid:Int,s:Schedule) extends Thread with ReadWrite {
	
	val oldValues = new Array[Double](100);
	val written = new Array[Boolean](100);
	
	{
		for (i <- 0 until written.length) written(i) = false;
		for (i <- 0 until oldValues.length) oldValues(i) = 0;
	}
	
	var state:Boolean = false;
	
	override def run(){
		var value:Double = 0;
        begin();
        for (op <- s) {
            if (op._1 == 'r') {
                Database.lt.rl (tid, op._3); value = read (op._3);
            } else {
                Database.lt.wl (tid, op._3); write (op._3, value + 1.);
            }
        }
        for (op <- s) Database.lt.ul (op._2, op._3);
        commit();
	}
	
	def begin(){
		Thread.sleep (5);
        //println ("begin transaction " + tid);
	}
	
	def read (oid:Int):Double={
		 Thread.sleep (10);
		 val value = Database.data(oid); 
		 //println ("read " + tid + " ( " + oid + " ) value = " + value);
		 value;
	}
	
	def write(oid:Int,value:Double){
		Thread.sleep (15);
        //println ("write " + tid + " ( " + oid + " ) value = " + value);
        Database.data(oid) = value;
	}
	
	def commit(){
		Thread.sleep (20);
		state = true;
       // println ("commit transaction " + tid);
	}
}
