package edu.uga.cs.zhen.image.processor;

public class SteganoEncoder implements ImgProcesser {
	
	private StringBuffer hiddenTxt;
	private int[][][] hiddenImg;
	private int numBits;
	
	public SteganoEncoder(StringBuffer hiddenTxt){
		this.hiddenTxt = hiddenTxt;
	}
	
	public SteganoEncoder (int[][][] hiddenImg, int numBits){
		this.hiddenImg = hiddenImg;
		this.numBits = numBits;
	}
	
	public int[][] preProcess(int[][][] threeDPix){
		int coverRow = threeDPix.length;
		int coverCol = threeDPix[0].length;
		
		if (hiddenImg != null){
			// embed image
			int hiddenRow = hiddenImg.length;
			int hiddenCol = hiddenImg[0].length;
			
			if (hiddenRow > coverRow || hiddenCol > coverCol){
				System.out.println("Hidden image is too big.");
				return null;
			}
			else{
				int restBits = 8 - numBits;
				int[][] data = new int[coverRow][coverCol];
				int startRow = (coverRow-hiddenRow) / 2;
				int startCol = (coverCol-hiddenCol) / 2;
				for (int i=0;i<hiddenRow;i++){
					for (int j=0;j<hiddenCol;j++){
						data[i+startRow][j+startCol] = hiddenImg[i][j][1] >>> restBits;
					}
				}
				return data;
			}
		}
		else{
			// embed text
			int totalBits = hiddenTxt.length()*7;
			int bitsPixel = (int)Math.ceil(totalBits / (coverRow*coverCol));
			if (bitsPixel > 8){
				System.out.println("Hidden information is too big");
				return null;
			}
			else{
				byte[] bitVals = new byte[totalBits];
				for (int i=0;i<hiddenTxt.length();i++){
					for (int j=i*7,a=0;j<(i+1)*7;j++,a++){
						bitVals[j] = (byte) ((((byte)(hiddenTxt.charAt(i))) & (0x1<<(6-a)))>>>(6-a));
					}
				}
				int[][] data = new int[coverRow][coverCol];
				for (int k=0,a=0;;a++){
					for (int i=0;i<coverRow;i++){
						for (int j=0;j<coverCol;j++){
							data[i][j] = (data[i][j] | (0x1<<a)) & ((0xFF^(1<<a))|bitVals[k]<<a);
							k++;
							//if (k < 24) System.out.println(data[i][j]);
							if (k >= bitVals.length) {
								this.numBits = a+1;
								return data;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int imgRow = threeDPix.length;
		int imgCol = threeDPix[0].length;
		
		int[][] dataToEmbed = preProcess(threeDPix);
		if (dataToEmbed == null) return null;
		int[][][] data = new int[imgRow][imgCol][4];
		
		for (int i=0;i<imgRow;i++){
			for (int j=0;j<imgCol;j++){
				data[i][j][0] = 255;
				int v = (threeDPix[i][j][1] & (0xFF<<numBits)) | (dataToEmbed[i][j]);
				data[i][j][1] = v;
				data[i][j][2] = v;
				data[i][j][3] = v;
			}
		}
		return data;
	}

}
