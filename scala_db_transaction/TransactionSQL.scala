class TransactionSQL(tid:Int,s:Schedule) extends Transaction(tid,s) with ReadWrite {
	
	var writeobjs:Set[Int] = Set();
	var readobjs:Set[Int] = Set();
	
	override def run () {
		var value:Double = 0;
		// begin
		super.begin();
		// compute objects need
		for (op <- s){
			if (op._1 == r) readobjs += op._3 ;
			else {
				readobjs -= op._3 ;
				writeobjs += op._3 ;
			}
		}
		// grab all locks
		for (ob <- writeobjs if Database.lt.wl(tid, ob)) return;
		for (ob <- readobjs if Database.lt.rl(tid, ob)) return;
		// execution
		for (op <- s){
			if (op._1 == r) value = super.read(op._3);
			else {
				super.write(op._3, value+1);
				oldValues(op._3) = Database.data(op._3);
				written(op._3) = true;
			}
		}
		// release lock
		for (op <- s) Database.lt.ul(tid, op._3);
		// commit
		super.commit();
	}
}

object TransactionSQLTest extends Application with ReadWrite {
	
    println ("Test Transactions in MySQL");
        var s = new Schedule(List());
    
    val transactionSQLs = new Array[Transaction](30);
    for (i <- 0 until transactionSQLs.length){
    	transactionSQLs(i) = new TransactionSQL(i, s.genTransaction(1,2,2));
    }
    var monitorSQL:Monitor = new Monitor(transactionSQLs);
    monitorSQL.start();
    
    while (monitorSQL.isAlive()){}
}
