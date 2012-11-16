class Result {
	val results:Array[Int] = new Array(2);
	
	{
		results(0) = 0;
		results(1) = 0;
	}
	
	def setTime(t:Int){
		results(0) = t;
	}
	
	def getTime:Int={
		results(0);
	}
	
	def setSuc(s:Int){
		results(1) = s;
	}
	
	def getSuc:Int={
		results(1);
	}
}
