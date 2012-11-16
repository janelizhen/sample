class Monitor(threads:Array[Transaction]) extends Thread{
	
	var result:Result = new Result();
	
	override def run{
		var start = System.currentTimeMillis();
		for (i <- 0 until threads.length){
			threads(i).run();
		}

		var hasAlive:Boolean = false;
		var success:Int = 0;
		do {
			hasAlive = false;
			for (i <- 0 until threads.length){
				if (threads(i).isAlive) hasAlive = true;
				else if (!threads(i).state) {
					threads(i).run();
					hasAlive = true;
				}
				else success += 1;
			}
		}
		while (hasAlive);

		var time = System.currentTimeMillis() - start;
		result.setSuc(success);
		result.setTime(time.toInt);
	}
	
	def getResult:Result={
		result;
	}
}