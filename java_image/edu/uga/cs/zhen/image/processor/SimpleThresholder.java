package edu.uga.cs.zhen.image.processor;

public class SimpleThresholder implements ImgProcesser{
	
	private int threshold; 
	
	public SimpleThresholder(int threshold){
		this.threshold = threshold;
	}
	
	public int[][][] processImg(int[][][] threeDPix){
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		
		for (int i=0;i<imgRows;i++){
			for (int j=0;j<imgCols;j++){
					int value = data[i][j][1];
					value = value > threshold ? 255 : 0;
					data[i][j][1] = value;
					data[i][j][2] = value;
					data[i][j][3] = value;
			}
		}
		
		return data;
	}
}
