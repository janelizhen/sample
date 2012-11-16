package edu.uga.cs.zhen.image.processor;

public class SobelOperator implements ImgProcesser {

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int [][][] data = new int[imgRows][imgCols][4];
		int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
		
		for (int i=1; i<imgRows-1; i++){
			for (int j=1; j<imgCols-1; j++){
				data[i][j][0] = 255;
				int x = threeDPix[i-1][j-1][1] + 2*threeDPix[i-1][j][1] + threeDPix[i-1][j+1][1] + (-1)*threeDPix[i+1][j-1][1] + (-2)*threeDPix[i+1][j][1] + (-1)*threeDPix[i+1][j+1][1];
				int y = threeDPix[i-1][j-1][1] + 2*threeDPix[i][j-1][1] + threeDPix[i+1][j-1][1] + (-1)*threeDPix[i-1][j+1][1] + (-2)*threeDPix[i][j+1][1] + (-1)*threeDPix[i+1][j+1][1];
				int strength = (int)Math.floor(Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)));
				data[i][j][1] = strength;
				data[i][j][2] = strength;
				data[i][j][3] = strength;
				
				if (min > strength) min = strength;
				if (max < strength) max = strength;
			}
		}
		
		for (int r=1;r<imgRows-1;r++){
			for (int c=1;c<imgCols-1;c++){
				data[r][c][0] = 255;
				int v = data[r][c][1];
				//v = (int) Math.round(Math.floor(255*(v-min)/max));
				data[r][c][1] = v;
				data[r][c][2] = v;
				data[r][c][3] = v;
			}
		}
		
		return data;
	}

}
