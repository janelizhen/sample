import scala.collection.mutable.Set;

class PolyGraph (nNodes:Int) extends PrecedenceGraph (nNodes){
	
	private var poly:List[Set[Tuple2[Int,Int]]] = List();
	
	def addPolyEdgeSet(n:Set[Tuple2[Int,Int]]){
		var npoly:List[Set[Tuple2[Int,Int]]] = List();
		if (poly.size == 0){
			for (e<-n){
				npoly :+= Set(e);
			}
		}
		else{
			for (e<-n){
				for (s<-poly){
					var set = s+e;
					npoly :+= set;
				}
			}
		}
		poly = npoly;
	}
	
	override def hasCycle:Boolean={
		if (poly.size == 0) return super.hasCycle;
		for (s<-poly){
			for (e<-s){
				this.addEdge(e._1, e._2);
			}
			if (!super.hasCycle) return false;
			for (e<-s){
				this.removeEdge(e._1, e._2);
			}
		}
		true;
	}
	
	override def toString: String = 
    {
        var s = super.toString;
        s += "\nPoly Edges: \n";
        for (si<-poly){
        	s += "{";
        	for (e<-si){
        		s += "( " + e._1 + " => " + e._2 + " ) ";
        	}
        	s += "}";
        }
        s;
    }
}

object PolyGraphTest extends Application
{
    val pg = new PolyGraph (3)
    pg.addEdge (0, 1)
    pg.addEdge (0, 2)
    pg.addEdge (1, 2)
    pg.addPolyEdgeSet(Set(Tuple2(0,2),Tuple2(2,0)));
    
    println ("pg = " + pg + "\nhas cycle = " + pg.hasCycle)
    
}
