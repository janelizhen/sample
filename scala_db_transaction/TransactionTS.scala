object Timestamp {
	val stamps = new Array[Long](2);
	stamps(0) = 0;		//RTS
	stamps(1) = 0;		//WTS
}

class TransactionTS (tid:Int,s:Schedule) extends Transaction(tid,s) with ReadWrite {
	
	override def run () {
		var timestamp:Long = System.currentTimeMillis();
		var value:Double = 0;
		super.begin();
		for (op <- s){
			//println(op._1 + "" + tid + "(" + op._3 + ")");
			if (op._1 == 'r'){
				if (timestamp >= Timestamp.stamps(1)){
					if (timestamp > Timestamp.stamps(0))
						Timestamp.stamps(0) = timestamp;
					value = super.read(op._3);
				}
				else{
					rollback();
					return;
				}
			}
			else{
				if (timestamp < Timestamp.stamps(0)){
					rollback();
					return;
				}
				else if (timestamp < Timestamp.stamps(1)){
					// NOP
				}
				else{
					Timestamp.stamps(1) = timestamp;
					super.write(op._3,value);
				}
			}
		}
		super.commit();
	}
	
	def rollback(){
		Thread.sleep(30);
		for (i <- 0 until written.length if written(i) == true){
			Database.data(i) = oldValues(i);
		}
		//println("rollback transaction: " + tid);
	}
}

object TransactionTSTest extends Application with ReadWrite {
    println ("Test Transactions in Timestamp");
    var s = new Schedule(List());
    
    val transactionTSOs = new Array[Transaction](90);
    for (i <- 0 until transactionTSOs.length){
    	transactionTSOs(i) = new TransactionTS(i, s.genTransaction(i,2,20));
    }
    for (i <- 0 until transactionTSOs.length){
    	transactionTSOs(i).start();
    }
    
    var hasAlive:Boolean = false;
    do {
    	hasAlive = false;
    	for (i <- 0 until transactionTSOs.length if transactionTSOs(i).isAlive)
    		hasAlive = true;
    }
    while (hasAlive);
    println("finish");
}