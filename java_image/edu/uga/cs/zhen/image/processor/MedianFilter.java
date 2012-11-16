package edu.uga.cs.zhen.image.processor;

import java.util.*;

public class MedianFilter implements ImgProcesser{
	private int size,mid;
	
	public MedianFilter(int size){
		this.size = size;
		this.mid = (size-1)/2;
	}
	
	public int[][][] processImg(int[][][] threeDPix){
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int[][][] data = new int[imgRows][imgCols][4];
		
		for (int r=mid; r < imgRows-mid; r++){
			for (int c=mid; c < imgCols-mid; c++){
				int k=0;
				int[] all = new int[size*size];
				for (int i=-1*mid;i<=mid;i++){
					for (int j=-1*mid;j<=mid;j++){
						all[k] = threeDPix[r+i][c+j][1];
						k++;
					}
				}
				Arrays.sort(all);
				int median = all[(size*size-1)/2];
				data[r][c][0] = 255;
				data[r][c][1] = median;
				data[r][c][2] = median;
				data[r][c][3] = median;
			}
		}
		return data;
	}
}