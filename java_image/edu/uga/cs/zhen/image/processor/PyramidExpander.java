package edu.uga.cs.zhen.image.processor;

public class PyramidExpander implements ImgProcesser {
	
	public static final int ORDER_ZERO = 0;
	public static final int ORDER_ONE = 1;
	
	private int type,level;
	
	public PyramidExpander(int type,int level){
		this.type = type;
		this.level = level;
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		if (type == ORDER_ZERO){			
			return threeDPix;
		}
		else{
			int imgRows = threeDPix.length;
			int imgCols = threeDPix[0].length;
			int[][][] data = new int[imgRows][imgCols][4];
			
			int size = (int) Math.pow(2, level); // size of each block
			int num = size >= imgRows ? 1: imgRows / size; // number of blocks per row/col
			
			int[][] origin = new int[num][num];
			int exnum = num*2;
			int[][] expand = new int[exnum][exnum];
			
			// get origin
			for (int r=0;r<num;r++){
				for (int c=0;c<num;c++){
					origin[r][c] = threeDPix[r*size][c*size][1];
				}
			}
			
			// expand
			for (int r=0;r<exnum;r+=2){
				for (int c=0;c<exnum;c+=2){
					// update even number row and column
					expand[r][c] = origin[r/2][c/2];
				}
			}
			for (int r=0;r<exnum;r+=2){
				for (int c=1;c<exnum;c+=2){
					// update even row and odd column.
					if (c != exnum-1)
						expand[r][c] = (expand[r][c-1]+expand[r][c+1])/2;
					else
						expand[r][c] = expand[r][c-1]/2;
				}
			}
			for (int r=1;r<exnum;r+=2){
				for (int c=0;c<exnum;c++){
					// update odd row.
					if (r != exnum-1)
						expand[r][c] = (expand[r-1][c]+expand[r+1][c])/2;
					else
						expand[r][c] = expand[r-1][c]/2;
				}
			}
			
			// fill
			size = size / 2;
			for (int r=0; r<exnum; r++){
				for (int c=0; c<exnum; c++){
					for (int i=r*size; i<(r+1)*size; i++){
						for (int j=c*size; j<(c+1)*size; j++){
							data[i][j][0] = 255;
							data[i][j][1] = expand[r][c];
							data[i][j][2] = expand[r][c];
							data[i][j][3] = expand[r][c];
						}
					}
				}
			}
			return data;
			}
		}
}
