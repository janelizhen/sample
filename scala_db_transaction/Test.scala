object Test extends Application with ReadWrite {
	
	// meta schedule for generating random transactions
	var s = new Schedule(List());
	
	// Three parameters to tune
	// Number of transactions:  nTrans
	// Number of operations for each transactions:  nOpers
	// Number of data objects:  nObject
	
	println("Input number of transactions: (1-100)");
	var nTrans = readInt();
	println("Input number of operations for each transaction:");
	var nOpers = readInt();
	println("Input number of data objects: (1-100)");
	var nObject = readInt();
	
	var result2PLTime:Int = 0;
	var resultTSOTime:Int = 0;
	var resultSQLTime:Int = 0;
	
	var result2PLSuc:Int = 0;
	var resultTSOSuc:Int = 0;
	var resultSQLSuc:Int = 0;
	
	var b:Boolean = true;
	var i:Int = 0;
	while (b){
		println("running... " + i);
		val transaction2PLs = new Array[Transaction](nTrans);
		for (i <- 0 until transaction2PLs.length){
			transaction2PLs(i) = new Transaction2PL(i, s.genTransaction(i,nOpers,nObject));
		}
		var monitor2PL:Monitor = new Monitor(transaction2PLs);
		monitor2PL.start();
    
		while (monitor2PL.isAlive()){}
		result2PLTime += monitor2PL.getResult.getTime;
		result2PLSuc += monitor2PL.getResult.getSuc;
    
		val transactionTSOs = new Array[Transaction](nTrans);
		for (i <- 0 until transactionTSOs.length){
			transactionTSOs(i) = new TransactionTS(i, s.genTransaction(i,nOpers,nObject));
		}
		var monitorTSO:Monitor = new Monitor(transactionTSOs);
		monitorTSO.start();
    
		while (monitorTSO.isAlive()){}
		resultTSOTime += monitorTSO.getResult.getTime;
		resultTSOSuc += monitorTSO.getResult.getSuc;
    
		val transactionSQLs = new Array[Transaction](nTrans);
		for (i <- 0 until transactionSQLs.length){
			transactionSQLs(i) = new TransactionSQL(i, s.genTransaction(i,nOpers,nObject));
		}
		var monitorSQL:Monitor = new Monitor(transactionSQLs);
		monitorSQL.start();
    
		while (monitorSQL.isAlive()){}
		resultSQLTime += monitorSQL.getResult.getTime;
		resultSQLSuc += monitorSQL.getResult.getSuc;
    
		i += 1;
		if (i < 5) b = true;
		else b = false;
	}
	
	println();
	println("Transaction in 2PL");
	println("Average Time = " + result2PLTime/i + " ms");
	println("#Commit Transaction = " + result2PLSuc/i);
	var thp2PL:Double = result2PLSuc*1000/result2PLTime;
	println(thp2PL);
	
	println();
	println("Transaction in TSO");
	println("Average Time = " + resultTSOTime/i + " ms");
	println("#Commit Transaction = " + resultTSOSuc/i);
	var thpTSO:Double = resultTSOSuc*1000/resultTSOTime;
	println(thpTSO);
	
	println();
	println("Transaction in MySQL");
	println("Average Time = " + resultSQLTime/i + " ms");
	println("#Commit Transaction = " + resultSQLSuc/i);
	var thpSQL:Double =resultSQLSuc*1000/resultSQLTime;
	println(thpSQL);
}