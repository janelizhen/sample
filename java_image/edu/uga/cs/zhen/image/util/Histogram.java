package edu.uga.cs.zhen.image.util;

import java.util.*;

public class Histogram {
	
	private int[] hist;
	
	public Histogram(int[][][] data, int row, int col){
		hist = new int[255];
		for (int i=0; i<row; i++){
			for (int j=0; j<col; j++){
				hist[data[i][j][1]]++;
			}
		}
	}
	
	public int[] getHistogram(){
		return hist;
	}
	
	public int[] findPeaks(int num){
		int[] peaks = new int[num];
		
		int[] acc = new int[hist.length];
		int[] dec = new int[hist.length];
		int[] slope = new int[hist.length];
		
		acc[0] = 0;
		dec[0] = hist[1] - hist[0];
		acc[hist.length-1] = hist[hist.length-1] - hist[hist.length-2];
		dec[hist.length-1] = 0;
		slope[0] = Math.abs(acc[0] - dec[0]);
		slope[hist.length-1] = Math.abs(acc[0] - dec[0]);
		for (int i=1;i<hist.length-1;i++){
			acc[i] = acc[i-1] + hist[i]-hist[i-1];
			dec[i] = dec[i-1] + hist[i+1] - hist[i]; 
			slope[i] = Math.abs(acc[i]-dec[i]);
		}
		
		for (int i=0;i<num;i++){
			int most = Integer.MIN_VALUE;
			int mj = -1;
			for (int j=0;j<slope.length;j++){
				if (slope[j] > most) {
					most = slope[j];
					mj = j;
				}
			}
			peaks[i] = mj;
			slope[mj] = 0;
		}
		
		Arrays.sort(peaks);
		return peaks;
	}
}
