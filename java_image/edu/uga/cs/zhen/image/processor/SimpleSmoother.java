package edu.uga.cs.zhen.image.processor;

public class SimpleSmoother implements ImgProcesser{
	private int size,mid;
	
	public SimpleSmoother(int size){
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
				int total = 0;
				for (int i=-1*mid;i<=mid;i++){
					for (int j=-1*mid;j<=mid;j++){
						total += threeDPix[r+i][c+j][1];
						k++;
					}
				}
				int avg = Math.round(total/(size*size));
				data[r][c][0] = 255;
				data[r][c][1] = avg;
				data[r][c][2] = avg;
				data[r][c][3] = avg;
			}
		}
		return data;
	}
}
