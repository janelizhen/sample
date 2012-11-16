package edu.uga.cs.zhen.image.processor;

public class LaplacianOperator implements ImgProcesser {
	
	public static final int LO_NE = 0;
	public static final int LO_PS = 1;
	
	public static final int LO_DG = 3;
	public static final int LO_HV = 4;
	public static final int LO_AL = 5;
	
	private int function, size, mid;
	private int type;
	
	public LaplacianOperator(int function, int size, int type){
		this.function = function;
		this.size = size;
		this.type = type;
		this.mid = (size-1)/2;
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int[][][] data = new int[imgRows][imgCols][4];
		int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
		
		for (int i=mid; i<imgRows-mid; i++){
			for (int j=mid; j<imgCols-mid; j++){
				data[i][j][0] = 255;
				int v = 0;
				if (type > 2 && size == 3){
					if (type == LO_DG){
						if (function == LO_NE)
							v = threeDPix[i-1][j-1][1]+threeDPix[i-1][j+1][1]+threeDPix[i-1][j+1][1]+threeDPix[i+1][j+1][1]-4*threeDPix[i][j][1];
						else
							v = 4*threeDPix[i][j][1]-(threeDPix[i-1][j-1][1]+threeDPix[i-1][j+1][1]+threeDPix[i-1][j+1][1]+threeDPix[i+1][j+1][1]);
					}
					else if (type == LO_HV){
						if (function == LO_NE)
							v = threeDPix[i+1][j][1]+threeDPix[i-1][j][1]+threeDPix[i][j+1][1]+threeDPix[i][j-1][1]-4*threeDPix[i][j][1];
						else
							v = 4*threeDPix[i][j][1]-(threeDPix[i+1][j][1]+threeDPix[i-1][j][1]+threeDPix[i][j+1][1]+threeDPix[i][j-1][1]);
					}
					else{
						if (function == LO_NE){
							for (int l=-1*mid;l<=mid;l++){
								for (int m=-1*mid;m<=mid;m++){
									v += threeDPix[i+l][j+m][1];
								}
							}
							v -= (size*size-1)*threeDPix[i][j][1];
						}
						else{
							for (int l=-1*mid;l<=mid;l++){
								for (int m=-1*mid;m<=mid;m++){
									v += threeDPix[i+l][j+m][1];
								}
							}
							v = (size*size-1)*threeDPix[i][j][1] - v;
						}
					}
				}
				else if (type < 2){
					if (function == LO_NE){
						for (int l=-1*mid;l<=mid;l++){
							for (int m=-1*mid;m<=mid;m++){
								v += threeDPix[i+l][j+m][1];
							}
						}
						v -= (size*size-1)*threeDPix[i][j][1];
					}
					else{
						for (int l=-1*mid;l<=mid;l++){
							for (int m=-1*mid;m<=mid;m++){
								v += threeDPix[i+l][j+m][1];
							}
						}
						v = (size*size-1)*threeDPix[i][j][1] - v;
					}
				}
				data[i][j][1] = v;
				data[i][j][2] = v;
				data[i][j][3] = v;
				
				if (min > v) min = v;
				if (max < v) max = v;
			}
		}
		
		max -= min;		// laplacian range
		
		// for displaying purpose
		for (int r=mid;r<imgRows-mid;r++){
			for (int c=mid;c<imgCols-mid;c++){
				data[r][c][0] = 255;
				int v = data[r][c][1];
				v = (int) Math.round(Math.floor(255*(v-min)/max));
				data[r][c][1] = v;
				data[r][c][2] = v;
				data[r][c][3] = v;
			}
		}
		
		return data;
	}

}
