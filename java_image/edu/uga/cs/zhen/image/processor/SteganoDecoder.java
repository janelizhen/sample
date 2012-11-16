package edu.uga.cs.zhen.image.processor;

public class SteganoDecoder implements ImgProcesser {
	
	private int numBits;
	private StringBuffer decodedTxt;
	
	public SteganoDecoder(int numBits){
		this.numBits = numBits;
	}
	
	public String getText(){
		return decodedTxt.substring(0, decodedTxt.length());
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRow = threeDPix.length;
		int imgCol = threeDPix[0].length;
		
		// extract text
		int total = 8*imgRow*imgCol;
		byte[] bitVals = new byte[total];
		for (int k=0,a=0;k<total;a++){
			for (int i=0;i<imgRow;i++){
				for (int j=0;j<imgCol;j++){
					bitVals[k] = (byte) ((threeDPix[i][j][1] & (0x1<<a))>>>a);
					k++;
					if (k>=total) break;
				}
				if (k>=total) break;
			}
		}
		byte[] charVals = new byte[total/7];
		for (int i=0;i<charVals.length;i++){
			for (int j=0;j<7;j++){
				charVals[i] = (byte) (charVals[i] | (bitVals[i*7+j]<<(6-j)));
			}
		}
		decodedTxt = new StringBuffer();
		for (int k=0;k<charVals.length;k++){
			decodedTxt.append((char)charVals[k]);
		}
		
		// extract image
		int restBits = 8-numBits;
		int[][][] data = new int[imgRow][imgCol][4];
		
		for (int i=0;i<imgRow;i++){
			for (int j=0;j<imgCol;j++){
				data[i][j][0] = 255;
				int v = threeDPix[i][j][1]<<restBits;
				data[i][j][1] = v;
				data[i][j][2] = v;
				data[i][j][3] = v;
			}
		}
		return data;
	}

}