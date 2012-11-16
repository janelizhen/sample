package edu.uga.cs.zhen.image.processor;

public class PyramidConstructor implements ImgProcesser {

	private int iterator, level;
	
	public PyramidConstructor(int level){
		this.iterator = 1;
		this.level = level;
	}
	
	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRows = threeDPix.length;
		int imgCols = threeDPix[0].length;
		int [][][] data = new int[imgRows][imgCols][4];
		
		if (iterator <= level){
			int size = (int)Math.pow(2, iterator);
			int num = size >= imgRows ? 1: imgRows / size;
			
			for (int r=0; r<num; r++){
				for (int c=0; c<num; c++){
					int sum = 0;
					for (int i=r*size; i<(r+1)*size; i++){
						for (int j=c*size; j<(c+1)*size; j++){
							sum += threeDPix[i][j][1];
						}
					}
					int avg = sum / (size*size);
					for (int i=r*size; i<(r+1)*size; i++){
						for (int j=c*size; j<(c+1)*size; j++){
							data[i][j][0] = 255;
							data[i][j][1] = avg;
							data[i][j][2] = avg;
							data[i][j][3] = avg;
						}
					}
				}
			}
			iterator++;
			return data;
		}
		else{
			iterator = 0;
			return null;
		}
	}

}
