package edu.uga.cs.zhen.image.processor;

public class NaiveProcessor implements ImgProcesser{
	private static final long serialVersionUID = 1L;

	public NaiveProcessor(){
		
	}
	
	public int[][][] processImg(int[][][] threeDPix){
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		
		for (int i=0;i<imgRows;i++){
			for (int j=0;j<imgCols;j++){
				data[i][j][1] = Math.min(data[i][j][1]*2, 255);
				data[i][j][2] = Math.min(data[i][j][2]*2, 255);
				data[i][j][3] = Math.min(data[i][j][3]*2, 255);
			}
		}
		
		return data;
	}
}