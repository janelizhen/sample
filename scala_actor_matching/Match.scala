package matching

import scala.actors._
import Actor._
import scala.util.Random

case class CheckIn(boy:Actor)
case class Wait(girl:Actor)
case class Go(id:Actor)
case class Potential(list:List[Actor])
case class Dismiss(id:Actor)

case class Stop

class Girl(coordinator:Actor) extends Actor{
	def act(){
		coordinator ! Wait(self)
		loop{
			react{
				case Go(boy) =>
				    if ((new Random()).nextDouble < 1/(mailboxSize+1)){
				      boy ! Go(self)
				      coordinator ! Dismiss(self)
				      println("girl " + self + " with " + boy)
				      exit()
				    }
			}
		}
	}
}

class Boy(coordinator:Actor) extends Actor{
	def act(){
		var wait:Actor = null
		var list:List[Actor] = null
		coordinator ! CheckIn(self)
		loop{
			react{
				case Potential(girls) =>
					list = girls
					if (mailboxSize == 0){
						if (wait == null){
							if (list.size != 0){
								wait = list((new Random()).nextInt(list.size)) 
								wait ! Go(self)
							}
						}
						else{
							if (!list.contains(wait)){
								wait = null
							}
						}
						coordinator ! CheckIn(self)
					}
		
		
				case Go(girl) =>
					coordinator ! Dismiss(self)
					println("boy " + self + " with " + girl)
					exit()
			}
		}
	}
}

class Coordinator() extends Actor{
	def act(){
		var girls:List[Actor] = Nil
		var boys:List[Actor] = Nil
		loop{
			react{
				case Wait(girl) =>
					girls = girls:::List(girl)
					for (boy <- boys)
						boy ! Potential(girls)
		
				case CheckIn(boy) =>
					if (!boys.contains(boy))
						boys = boys:::List(boy)
					boy ! Potential(girls)
		
		
				case Dismiss(id) =>
					boys = boys.filterNot(List(id) contains)
					girls = girls.filterNot(List(id) contains)
			}
		}
	}
}

object Match extends Application {
	val coordinator = new Coordinator()
	coordinator.start

	for (i <- 0 to 49 toList){
		if ((new Random()).nextBoolean()){
			val girl = (new Girl(coordinator))
			//println(i + ": " + girl)
			girl.start
		}
		else{
			val boy = (new Boy(coordinator))
			//println(i + ": " + boy)
			boy.start
		}
	}
}