package edu.uga.cs.zhen.image.processor;

import java.util.*;

public class NoiseIntroducer implements ImgProcesser{
	private static final long serialVersionUID = 1L;
	
	private static final int NOISE_TYPE_RANDOM = 0;
	private static final int NOISE_TYPE_WHITE = 1;
	private static final int NOISE_TYPE_BLACK = 2;
	
	Random random;
	int type;
	double percentage;

	public NoiseIntroducer(int type, double percentage){
		random = new Random();
		this.type = type;
		this.percentage = percentage;
	}
	
	public int[][][] processImg(int[][][] threeDPix){
		int[][][] data = threeDPix;
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		
		for (int i=0;i<imgRows;i++){
			for (int j=0;j<imgCols;j++){
				switch(this.type){
				case NOISE_TYPE_RANDOM:
					if (random.nextDouble() <= this.percentage) {
						int value = random.nextBoolean()?data[i][j][1]-random.nextInt(data[i][j][1]):data[i][j][1]+random.nextInt(255-data[i][j][1]);
						data[i][j][1] = value;
						data[i][j][2] = value;
						data[i][j][3] = value;
					}
					break;
					
				case NOISE_TYPE_WHITE:
					if (random.nextDouble() <= this.percentage){
						int value = 255;
						data[i][j][1] = value;
						data[i][j][2] = value;
						data[i][j][3] = value;
					}
					break;
					
				case NOISE_TYPE_BLACK:
					if (random.nextDouble() <= this.percentage){
						int value = 0;
						data[i][j][1] = value;
						data[i][j][2] = value;
						data[i][j][3] = value;
					}
					break;
					
					default:
						break;
				}
			}
		}
		
		return data;
	}
}
