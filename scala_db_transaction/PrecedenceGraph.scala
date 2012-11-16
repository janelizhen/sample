import scala.collection.mutable.Set;

class PrecedenceGraph (nNodes:Int){
	
	private val graph = new Array[Set[Int]](nNodes);
	private val color = new Array[Char](nNodes);
	
	{
		for (i <- 0 until nNodes){
			graph(i) = Set[Int]();
			color(i) = 'g';
		}
	}
	
	def addEdge(i:Int, j:Int){
		graph(i) += j;
	}
	
	def removeAllEdges(i:Int){
		graph(i) = Set[Int]();
	}
	
	def removeEdge(i:Int, j:Int){
		graph(i) -= j;
	}
	
	def reColor(){
		for (i <- 0 until nNodes) color(i) = 'g';
	}
	
	def hasCycle:Boolean={
		for (i <- 0 until nNodes if color(i) == 'g' && loopback(i)) return true;
		false;
	}
	
	def loopback(i:Int):Boolean={
		if (color(i)=='y') return true;
		color(i) ='y';
		for (j<-graph(i) if color(j)!='r' && loopback(j)) return true;
		color(i) ='r';
		false;
	}
	
	override def toString: String = 
    {
        var s = "PrecedenceGraph ( "
        for (i <- 0 until nNodes) s += "{ " + i + " => " + graph(i) + " } "
        s + ")"
    }
}

object PrecedenceGraphTest extends Application
{
    val pg = new PrecedenceGraph (3)
    pg.addEdge (0, 1)
    pg.addEdge (1, 2)
    pg.addEdge (2, 0)

    println ("pg = " + pg + "\nhas cycle = " + pg.hasCycle)
    
}