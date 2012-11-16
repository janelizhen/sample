package edu.uga.cs.zhen.image.processor;

import java.util.*;

public class AdaptiveThresholder implements ImgProcesser {
	
	private int size,mid;
	private boolean Ttype;

	public AdaptiveThresholder(int size, boolean Ttype){
		this.size = size;
		this.mid = (size-1)/2;
		this.Ttype = Ttype;
	}
	
	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int[][][] data = new int[imgRows][imgCols][4];
		
		if (Ttype){
			// Use median value of a window as threshold
			for (int r=mid; r<imgRows-mid; r++){
				for (int c=mid; c<imgCols-mid; c++){
					int[] intensities = new int[size*size];
					int k = 0;
					for (int i=-1*mid;i<=mid;i++){
						for (int j=-1*mid;j<=mid;j++){
							intensities[k] = threeDPix[r+i][c+j][1];
							k++;
						}
					}
					Arrays.sort(intensities);
					int median = intensities[(size*size-1)/2];
					
					data[r][c][0] = 255;
					int value = threeDPix[r][c][1];
					if (value > median){
						data[r][c][1] = 255;
						data[r][c][2] = 255;
						data[r][c][3] = 255;
					}
					else{
						data[r][c][1] = 0;
						data[r][c][2] = 0;
						data[r][c][3] = 0;
					}
				}
			}
		}
		else{
			// Use average value of a window as threshold
			for (int r=mid; r<imgRows-mid; r++){
				for (int c=mid; c<imgCols-mid; c++){
					int total = 0;
					int k = 0;
					for (int i=-1*mid;i<=mid;i++){
						for (int j=-1*mid;j<=mid;j++){
							total += threeDPix[r+i][c+j][1];
							k++;
						}
					}
					int avg = Math.round(total/(size*size));
					
					data[r][c][0] = 255;
					int value = threeDPix[r][c][1];
					if (value > avg){
						data[r][c][1] = 255;
						data[r][c][2] = 255;
						data[r][c][3] = 255;
					}
					else{
						data[r][c][1] = 0;
						data[r][c][2] = 0;
						data[r][c][3] = 0;
					}
				}
			}
		}
		return data;
	}
}
