import scala.collection.mutable.HashMap;
import java.util.concurrent.Semaphore;

class LockTable {
	final val MAX_VALUE = 100;
	
	class Lock(tid:Int,s:Boolean){
		var shared:Boolean = s;						// lock is shared by reads or not
		var tids:List[Int] = List(tid);				// transactions that holds the lock
		val sem = new Semaphore(MAX_VALUE, true);	// concurrency control semaphore
		
		// add a new transaction
		def addTid(tid:Int){
			tids :+= tid;
		}
		
		// remove a transaction
		def removeTid(tid:Int){
			tids -= tid;
		}
		
		// true: Operations of same transaction hold the lock
		// false: Operations of different transaction hold the lock
		def sameTransaction(tid:Int):Boolean={
			return tids.head == tid;
		}
		
		// lock string
		override def toString():String={
			var s:String = "";
			if (shared) s += "shared: ";
			else s += "not shared: ";
			for (tid <- tids){
				s += tid + ",";
			}
			s;
		}
	}
	
	val locks = new HashMap[Int, Lock];	// lock table
	val waitforgraph = new PrecedenceGraph(MAX_VALUE);	// wait for graph
	
	def rl(tid:Int,oid:Int):Boolean={
		var lock:Lock = null;
		var wait = true;
		var same = false;
		var share = false;
		var cycle = false;
		
		synchronized{
			try {
				lock = locks(oid);
				if (lock.shared) {share = true; wait = false;}
				else if (lock.sameTransaction(tid)) {same = true; wait = false;}
				else {
					for (t <- lock.tids if t != tid){
						waitforgraph.addEdge(t, tid);
					}
					waitforgraph.reColor();
					if (waitforgraph.hasCycle){
						cycle = true;
						for (t <- lock.tids if t != tid){
							waitforgraph.removeEdge(t, tid);
						}
					}
				}
			}
			catch{
				case ex: Exception => wait = false;
			}
		}
		
		if (cycle) return true;	// deadlock exist if lock granted.
					if (wait){
			lock.sem.acquire();
		}
		
		synchronized{
			if (lock == null) locks(oid) = new Lock(tid,true);
			else if (share) lock.addTid(tid);
		}
		
		false;
	}
	
	def wl(tid:Int,oid:Int):Boolean={
		var lock:Lock = null;
		var wait = true;
		var same = false;
		var cycle = false;
		
		synchronized {
			try {
				lock = locks(oid);
				if (lock.sameTransaction(tid)) {same = true; wait = false;}
				else {
					for (t <- lock.tids if t != tid){
						waitforgraph.addEdge(t, tid);
					}
					waitforgraph.reColor();
					if (waitforgraph.hasCycle){
						cycle = true;
						for (t <- lock.tids if t != tid){
							waitforgraph.removeEdge(t, tid);
						}
					}
				}
			}
			catch{
				case ex: Exception => wait = false;
			}
		}
			
		if (cycle) return true;	// deadlock occur if lock granted
		if (wait){
			lock.sem.acquire();
		}
		
		synchronized{
			if (lock == null) locks(oid) = new Lock(tid,false);
			else if (same) lock.shared = false;
		}
		
		false;
	}
	
	def ul(tid:Int,oid:Int):Boolean={
		var lock:Lock = null;
		var notfound = false;
		var notowned = false;
		synchronized{
			try{
				lock = locks(oid);
			}
			catch{
				case ex: Exception => notfound = true;
			}
			if (!notfound){
				if (lock.tids.contains(tid)){
					lock.removeTid(tid);
					waitforgraph.removeAllEdges(tid);
					if (lock.tids.isEmpty){
						locks -= oid;
					}
					lock.sem.release();
				}
				else{
					notowned = true;
				}
			}
		}
		
		if (!waitforgraph.hasCycle){
			return false;
		}
		
		//if (notfound) println ("Error: ul: no lock for oid = " + oid + " found");
		//if (notowned) println ("Error: ul: no lock for oid = " + oid + " owned");
		
		true;
	}
}

object LockTableTest extends Application {
	// this test is not able to show the concurrent effect since only one thread is presented.
	// to see the deadlock effect, go Transaction2PL, sometime, one of the two testing transaction will rollback that is because of the detection of deadlock.
    val lt = new LockTable ()
    println (lt.rl(0, 0));
    println (lt.wl(1, 0));
    println (lt.rl(1, 1));
    println (lt.wl(0, 1));
    println(lt.locks(0).toString());
}