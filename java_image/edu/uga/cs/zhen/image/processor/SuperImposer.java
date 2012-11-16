package edu.uga.cs.zhen.image.processor;

public class SuperImposer implements ImgProcesser {
	
	public static final int WHITE_THROUGH = 0;
	public static final int BLACK_THROUGH = 1;
	
	private int[][][] origin;
	private int type;
	
	public SuperImposer(int type, int[][][] origin){
		this.type = type;
		this.origin = origin;
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int [][][] data = new int[imgRows][imgCols][4];
		
		for (int i=0; i<imgRows; i++){
			for (int j=0; j<imgCols; j++){
				data[i][j][0] = 255;
				if (type == WHITE_THROUGH){
					if (threeDPix[i][j][1] == 255){
						data[i][j][1] = origin[i][j][1];
						data[i][j][2] = origin[i][j][2];
						data[i][j][3] = origin[i][j][3];
					}
					else{
						data[i][j][1] = 0;
						data[i][j][2] = 0;
						data[i][j][3] = 0;
					}
				}
				else{
					if (threeDPix[i][j][1] == 0){
						data[i][j][1] = origin[i][j][1];
						data[i][j][2] = origin[i][j][2];
						data[i][j][3] = origin[i][j][3];
					}
					else{
						data[i][j][1] = 0;
						data[i][j][2] = 0;
						data[i][j][3] = 0;
					}
				}
			}
		}
		return data;
	}

}
