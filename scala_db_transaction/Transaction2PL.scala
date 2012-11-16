class Transaction2PL(tid:Int,s:Schedule) extends Transaction(tid,s) with ReadWrite {
	
	override def run () {		
		var value:Double = 0;
		super.begin();
		for (op <- s){
			//println(op._1 + "" + tid + "(" + op._3 + ")");
			if (op._1 == 'r'){
				if (Database.lt.rl(tid, op._3)){
					rollback();
					return;
				}
				else{
					value = super.read(op._3);
				}
			}
			else{
				if (Database.lt.wl(tid, op._3)){
					rollback();
					return;
				}
				else{
					super.write(op._3, value+1);
					oldValues(op._3) = Database.data(op._3);
					written(op._3) = true;
				}
			}
		}
		for (op <- s){
			Database.lt.ul(tid, op._3);
		}
		super.commit();
	}
	
	def rollback(){
		Thread.sleep(30);
		for (i <- 0 until written.length if written(i) == true){
			Database.data(i) = oldValues(i);
			Database.lt.ul(tid, i);
		}
		for (op <- s){
			Database.lt.ul(tid, op._3);
		}
		//println("rollback transaction: " + tid);
	}
}

object Transaction2PLTest extends Application with ReadWrite {
    println ("Test Transactions in 2PL");
    val t1 = new Transaction2PL (1, new Schedule (List ( ('w', 1, 0), ('w', 1, 1) )));
    val t2 = new Transaction2PL (2, new Schedule (List ( ('w', 2, 1), ('w', 2, 0) )));

    t1.start ();
    t2.start ();
}
