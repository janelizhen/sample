package edu.uga.cs.zhen.image.processor;

import edu.uga.cs.zhen.image.util.*;

public class ValleyThresholder implements ImgProcesser {
	
	private int iterator,num;
	private int[] peaks;
	
	public ValleyThresholder(int num,int[][][] threeDPix){
		this.num = num;
		this.iterator = 0;
		int row = threeDPix.length;
		int col = threeDPix[0].length;
		Histogram hist = new Histogram(threeDPix, row, col);
		peaks = hist.findPeaks(this.num);
	}
	
	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		if (iterator <= peaks.length-2){
			for (int i=0;i<imgRows;i++){
				for (int j=0;j<imgCols;j++){
					int value = data[i][j][1];
					if (value > peaks[iterator] && value <= peaks[iterator+1]){
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
			iterator++;
			return data;
		}
		else{
			iterator = 0;
			return null;
		}
	}
}
