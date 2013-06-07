package book

import scala.actors._
import Actor._
import scala.util.Random
import scala.collection.mutable.Map

/** A base class for enumeration book objects */
class Book(name:String) {
  def bookname():String = name
}

/** Enumeration book objects **/
case object BOOK1 extends Book("Gone with the wind")
case object BOOK2 extends Book("How to kill a mock bird")
case object BOOK3 extends Book("Programming Concurrency")
case object BOOK4 extends Book("Operating Systems")
case object BOOK5 extends Book("Introductory Robotics")

/** BookEnum object that returns a random book when random() method is called */
object BookEnum {
  def random():Book = {
    ((new Random()).nextInt(5)+1) match {
      case 1 =>
        return BOOK1
      
      case 2 =>
        return BOOK2
        
      case 3 =>
        return BOOK3
        
      case 4 =>
        return BOOK4
        
      case 5 =>
        return BOOK5
    }
  }
}

/** A work class with the book to manipulate and the corresponding quantity */
class Work(book:Book, quantity:Int){
  def getBook():Book = book
  def getQuantity():Int = quantity
}

/** Messages exchanged in the system */
case class restock(book:Book, quantity:Int, runnableJob:Actor)
case class ship(book:Book, quantity:Int, runnableJob:Actor)
case class succeed()
case class finish(jobType:Boolean)
case class issue(runnableJob:RunnableJob, client:Actor)
case class fail()
case class quit()

/** A runnable job class.
 *  A runnable job has a unique jobId that is assigned by the client who creates it
 *  A runnable job has a jobType.
 *    A jobType of "true" indicates all works contained in it are restocking works.
 *    A jobType of "false" indicates all works contained in it are shipping works.
 *  The client that creates a runnable job will assign the server for it to run within
 *  The server for a runnable job to run within will assign the book inventory for it to manipulate
 *  The server for a runnable job to run within will also assign a seqNum (sequential number) to the job
 */
class RunnableJob(jobId:Int, jobType:Boolean) extends Actor {
  var server:Actor = null
  var inventory:Actor = null
  var seqNum:Int = 0
  var works:List[Work] = List()
  var succeedWorks:Int = 0
  
  def addWork(book:Book, quantity:Int) {
    works = works.:+(new Work(book, quantity))
  }
  
  def setServer(server:Actor) {
    this.server = server
  }
  
  def setInventory(inventory:Actor) {
    this.inventory = inventory
  }
  
  def setSeqNum(seqNum:Int) {
    this.seqNum = seqNum
  }
  
  def getJobId():Int = jobId
  
  def getSeqNum():Int = seqNum
  
  def getType():Boolean = jobType
  
  def run() {
    for (work <- works) {
      if (jobType) {
        inventory ! restock(work.getBook, work.getQuantity, this)
      } else {
        inventory ! ship(work.getBook, work.getQuantity, this)
      }
    }
  }
  
  def act () {
    run()
    loop {
      react {
        case succeed() =>
          succeedWorks += 1
          if (works.size == succeedWorks) {
            server ! finish(jobType)
          }
      }
    }
  }
}

/** A book inventory class.
 *  This class uses a map to store the stocking information of all books (BOOK1 - BOOK5)
 *  It supports two different manipulations: increase and decrease
 *    An increase manipulation fulfills a certain restocking work
 *    A decrease manipulation fulfills a certain shipping work if stock is sufficient or delays a certain shipping work if stock is insufficient
 */
class Inventory extends Actor {
  var inventory:Map[Book, Int] = Map()
  
  def printWork(job:RunnableJob, book:Book, oldQuan:Int, newQuan:Int) {
    if (job.getType()) {
      println("A restocking work( " + book.bookname() + " , " + oldQuan + " -> " + newQuan + " ) is performed by job id = " + job.getJobId() + " and seqNum = " + job.getSeqNum())
    } else {
      println("A shipping work( " + book.bookname() + " , " + oldQuan + " -> " + newQuan + " ) is performed by job id = " + job.getJobId() + " and seqNum = " + job.getSeqNum())
    }
  }
  
  def increaseInventory(book:Book, quantity:Int, job:RunnableJob) {
    if (inventory.contains(book)) {
      inventory(book) += quantity
    } else {
      inventory = inventory.+((book, quantity))
    }
    job ! succeed()
    printWork(job, book, inventory(book)-quantity, inventory(book))
  }
  
  def decreaseInventory(book:Book, quantity:Int, job:RunnableJob) {
    if (inventory.contains(book)) {
      if (inventory(book) >= quantity) {
        inventory(book) -= quantity
        job ! succeed()
        printWork(job, book, inventory(book)+quantity, inventory(book))
      } else {
        self ! ship(book, quantity, job)
      }
    } else {
      self ! ship(book, quantity, job)
    }
  }
  
  def act() {
    loop {
      react {
        case restock(book:Book, quantity:Int, runnableJob:Actor) =>
          increaseInventory(book, quantity, runnableJob.asInstanceOf[RunnableJob])
          
        case ship(book:Book, quantity:Int, runnableJob:Actor) =>
          decreaseInventory(book, quantity, runnableJob.asInstanceOf[RunnableJob])
      }
    }
  }
}

/** A server class.
 *  This class processes the jobs posted by clients
 *  It has a maxNumJob which indicates the maximum number of running jobs of each type
 *    e.g. if maxNumJob is 5, at any time, at most 5 shipping jobs and 5 restocking jobs (10 in total) could run in server
 *    When the server reaches maxNumJob for a certain type of job, it declines the requests of processing new job of that type 
 *  It has a book inventory for all jobs posted to manipulate
 */
class Server(maxNumJob:Int, inventory:Actor) extends Actor {
  var numRestockJob:Int = 0
  var numShipJob:Int = 0
  
  def act() {
    loop {
      react {
        case issue(runnableJob:RunnableJob, client:Actor) =>
          if (runnableJob.getType) {
            if (numRestockJob < maxNumJob) {
              runnableJob.setInventory(inventory)
              runnableJob.setServer(this)
              runnableJob.setSeqNum(numRestockJob)
              runnableJob.start()
              client ! succeed()
              numRestockJob += 1
            } else {
              client ! fail()
            }
          } else {
            if (numShipJob < maxNumJob) {
              runnableJob.setInventory(inventory)
              runnableJob.setServer(this)
              runnableJob.setSeqNum(numShipJob)
              runnableJob.start()
              client ! succeed()
              numShipJob += 1
            } else {
              client ! fail()
            }
          }
          
        case finish(jobType:Boolean) =>
          if (jobType) {
            numRestockJob -= 1
          } else {
            numShipJob -= 1
          }          
      }
    }
  }
}

/** A client class
 *  This simulated client class randomly generates some different jobs and requests the server to process them
 *  It has a clientId which is assigned by main function (sequentially from 1 to the number of clients as specified in program argument)
 *  It has a server to make requests to
 *  It has a lifeTime which is specified in program argument as milliseconds
 *    A client only lives for this specific number of time to generate jobs and make requests to server
 *    When this amount of lifeTime expires, a client quite anyway
 *  It has a maxJobLength which is specified in program argument and all jobs it generates contains no more than that amount of works
 */
class Client(clientId:Int, server:Actor, lifeTime:Int, maxJobLength:Int, main:Actor) extends Actor {
  var jobCount = 0
  
  def printWork(jobId:Int, jobType:Boolean, book:Book, quantity:Int) {
    if (jobType) {
      println("A restocking work( " + book.bookname() + " , " + quantity + " ) will be performed by job id = " + jobId)
    } else {
      println("A shipping work( " + book.bookname() + " , " + quantity + " ) will be performed by job id = " + jobId)
    }
  }
  
  def generateJob():RunnableJob = {
    val jobId:Int = clientId*(10^((lifeTime.toString()).length))+jobCount
    val jobType:Boolean = (new Random()).nextBoolean()
    val job:RunnableJob = new RunnableJob(jobId, jobType);
    jobCount += 1
    job.setServer(server);
	var jobLength:Int = (new Random()).nextInt(maxJobLength)+1;
	  while (jobLength > 0) {
	    val book:Book = BookEnum.random()
	    val quantity:Int = (new Random()).nextInt(20) + 1
		job.addWork(book, quantity);
		jobLength -= 1;
		printWork(jobId, jobType, book, quantity)
	  }
	return job;
  }
  
  def checkTime(t0:Long) {
    if (System.currentTimeMillis() - t0 > lifeTime) {
      main ! quit()
      exit()
    }
  }
  
  def act() {
    val t0:Long = System.currentTimeMillis()
    var job:RunnableJob = generateJob()
    server ! issue(job, this)
    loop {
      react {
        case succeed() =>
          job = generateJob()
          server ! issue(job, this)
          checkTime(t0)
          
        case fail() =>
          Thread.sleep(500)
          server ! issue(job, this)
          checkTime(t0)
      }
    }
  }
}

/**
 * The main function of simulation
 */
object Main {
  val usage = """Usage: Main [num_of_client] [client_life_time] [max_num_job]"""
  def main(args:Array[String]) {
    val mainActor:Actor = actor {
	  if (args.length < 4) {
		println("Usage: [num_of_client] [client_life_time] [max_num_running_job_for_each_type] [max_job_length]")
		System.exit(1)
	  }
	  
	  val numOfClient:Int = args(0).toInt
	  val clientLifeTime:Int = args(1).toInt
	  val maxNumJob:Int = args(2).toInt
	  val maxJobLength:Int = args(3).toInt
				
	  val inventory:Inventory = new Inventory()
	  inventory.start()
	  val server:Server = new Server(maxNumJob, inventory)
	  server.start()
		
	  for (i <- 0 to numOfClient toList) {
		val client:Client = new Client(i, server, clientLifeTime, maxJobLength, self)
		client.start()
	  }
		
	  loop {
		react {
		  case quit() =>
		    if (mailboxSize + 1 >= numOfClient) {
		      System.exit(0)
		    } else {
		      self ! quit()
		    }
		}
	  }
    }
  }
}