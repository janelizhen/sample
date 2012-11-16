package edu.uga.cs.zhen.image.processor;

import edu.uga.cs.zhen.image.util.*;

public class PTiler implements ImgProcesser{
	
	int bound1,bound2;
	boolean sign;
	double percentage;
	
	public PTiler(int bound1, int bound2){
		this.bound1 = bound1;
		this.bound2 = bound2;
		this.percentage = 0;
	}
	
	public PTiler(int bound, boolean sign, double percentage){
		if (sign){
			// + lower bound given
			this.bound1 = bound;
			this.sign = sign;
			this.percentage = percentage;
		}
		else{
			// - upper bound given
			this.bound2 = bound;
			this.sign = sign;
			this.percentage = percentage;
		}
	}
	
	private void calculateBounds(Histogram hist, int row, int col){
		if (percentage == 0){
			return;
		}
		else{
			int size = (int)Math.ceil(row * col * percentage);
			int[] h = hist.getHistogram();
			if (sign){
				int total = 0, i = bound1;
				for (;total <= size;i++){
					total += h[i];
				}
				bound2 = bound1 + i;
			}
			else{
				int total = 0, i = bound2-1;
				for (;total <= size;i--){
					total += h[i];
				}
				bound1 = bound2 - i;
			}
		}
	}
	
	public int[][][] processImg(int[][][] threeDPix){
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		
		calculateBounds(new Histogram(threeDPix, imgRows, imgCols),imgRows,imgCols);
		
		for (int i=0;i<imgRows;i++){
			for (int j=0;j<imgCols;j++){
					int value = data[i][j][1];
					value = (value > bound1 && value < bound2) ? 255 : 0;
					data[i][j][1] = value;
					data[i][j][2] = value;
					data[i][j][3] = value;
			}
		}
		
		return data;
	}

}
