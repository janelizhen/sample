package edu.uga.cs.zhen.image.processor;

public class RobertsOperator implements ImgProcesser {
	
	public static final int RO_ABS = 0;	// abs mask
	public static final int RO_MAX = 1;	// max mask
	
	public static final int RO_NT = 1;	// threshold with white and original
	public static final int RO_RO = 2;	// threshold with robert's value and black
	public static final int RO_WO = 3;	// threshold with white and black
	public static final int RO_RB = 4;	// without threshold
	public static final int RO_WB = 5;	// threshold with robert's value and original
	
	private int type, function;
	private int threshold;
	
	public RobertsOperator(int type, int function, int threshold){
		this.type = type;
		this.function = function;
		this.threshold = threshold;
	}
	
	public int getType(){
		return type;
	}
	
	public int getFunction(){
		return function;
	}
	
	public int getThreshold(){
		return threshold;
	}
	
	public void setThreshold(int threshold){
		this.threshold = threshold;
	}
	
	public void setFunction(int function){
		this.function = function;
	}
	
	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int[][][] data = new int[imgRows][imgCols][4];
		
		for (int i=0;i<imgRows-1;i++){
			for (int j=0;j<imgCols-1;j++){
				data[i][j][0] = 255;
				int v = 0;
				if (function == RO_ABS)
					v = Math.abs(threeDPix[i][j][1]-threeDPix[i+1][j+1][1])+Math.abs(threeDPix[i+1][j][1]-threeDPix[i][j+1][1]);
				else
					v = Math.max(Math.abs(threeDPix[i][j][1]-threeDPix[i+1][j+1][1]), threeDPix[i+1][j][1]-threeDPix[i][j+1][1]);
				if (type == RO_NT){
					data[i][j][1] = v;
					data[i][j][2] = v;
					data[i][j][3] = v;
				}
				else if (type == RO_NT){
					// robert's value without threshold
					data[i][j][1] = v;
					data[i][j][2] = v;
					data[i][j][3] = v;
				}
				else if (type == RO_RO){
					// robert's value and original
					if (v >= this.threshold){
						data[i][j][1] = v;
						data[i][j][2] = v;
						data[i][j][3] = v;
					}
					else{
						data[i][j][1] = threeDPix[i][j][1];
						data[i][j][2] = threeDPix[i][j][1];
						data[i][j][3] = threeDPix[i][j][1];
					}
				}
				else if (type == RO_WO){
					// white and original
					if (v >= this.threshold){
						data[i][j][1] = 255;
						data[i][j][2] = 255;
						data[i][j][3] = 255;
					}
					else{
						data[i][j][1] = threeDPix[i][j][1];
						data[i][j][2] = threeDPix[i][j][1];
						data[i][j][3] = threeDPix[i][j][1];
					}
				}
				else if (type == RO_RB){
					// roboet's value and black
					if (v >= this.threshold){
						data[i][j][1] = v;
						data[i][j][2] = v;
						data[i][j][3] = v;
					}
					else{
						data[i][j][1] = 0;
						data[i][j][2] = 0;
						data[i][j][3] = 0;
					}
				}
				else{
					// white and black
					if (v >= this.threshold){
						data[i][j][1] = 255;
						data[i][j][2] = 255;
						data[i][j][3] = 255;
					}
					else{
						data[i][j][1] = 0;
						data[i][j][2] = 0;
						data[i][j][3] = 0;
					}
				}
			}
		}
		return data;
	}

}
