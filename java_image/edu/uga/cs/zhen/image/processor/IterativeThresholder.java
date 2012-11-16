package edu.uga.cs.zhen.image.processor;

public class IterativeThresholder implements ImgProcesser {
	
	private int init;
	private double change;

	public IterativeThresholder(int init, double change){
		this.init = init;
		this.change = change;
	}
	
	public int[][][] processImg(int[][][] threeDPix) {
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		
		double change = Double.MAX_VALUE;
		int T = this.init;
		while (change >= this.change){
			int total1=0,num1=0;
			int total2=0,num2=0;
			for (int i=0;i<imgRows;i++){
				for (int j=0;j<imgCols;j++){
					if (data[i][j][1] >= T){
						total1 += data[i][j][1];
						num1++;
					}
					else{
						total2 += data[i][j][1];
						num2++;
					}
				}
			}
			double avg1 = num1==0?0:total1/num1;
			double avg2 = num2==0?0:total2/num2;
			int betterT = (int)(Math.round((avg1+avg2)/2));
			change = Math.abs(T-betterT);
			T = betterT;
		}
		
		for (int i=0;i<imgRows;i++){
			for (int j=0;j<imgCols;j++){
				if (data[i][j][1] > T){
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
		
		return data;
	}

}
