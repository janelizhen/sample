package edu.uga.cs.zhen.image.frame;

import java.awt.*;
import java.awt.image.*;

class Display extends Canvas{
	private static final long serialVersionUID = 1L;
	
	private Image rawImg,modImg;
	
	public void setModImage(Image modImg){
		this.modImg = modImg;
	}
	
	public void setRawImage(Image rawImg){
		this.rawImg = rawImg;
	}
	
	public Image getRawImage(){
		int width = rawImg.getWidth(this);
		int height = rawImg.getHeight(this);
		int[] rgbArray = convertToOneDim(img2Pixel(rawImg),width,height);
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, width, height, rgbArray, 0, width);
		return bi;
	}
	
	public BufferedImage getModImage(){
		int width = modImg.getWidth(this);
		int height = modImg.getHeight(this);
		int[] rgbArray = convertToOneDim(img2Pixel(modImg),width,height);
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, width, height, rgbArray, 0, width);
		return bi;
	}
	
	public int[][][] img2Pixel(Image rawImg){
		int imgRows = rawImg.getHeight(this);
		int imgCols = rawImg.getWidth(this);
		
		int[] oneDPix = new int[imgRows*imgCols];
		int[][][] threeDPix = new int[imgRows][imgCols][4];
		
		try{
    		PixelGrabber pgObj = new PixelGrabber(rawImg,0,0,imgCols,imgRows,oneDPix,0,imgCols);
    		pgObj.grabPixels();
    		threeDPix = convertToThreeDim(oneDPix,imgCols,imgRows);
    	}
    	catch(InterruptedException e){
    		e.printStackTrace();
    	}
    	
    	return threeDPix;
	}
	
	public Image pixel2Img(int[][][] threeDPix){
		int imgCols = threeDPix[0].length;
		int imgRows = threeDPix.length;
		
		int[] oneDPix = convertToOneDim(threeDPix,imgCols,imgRows);
		return createImage(new MemoryImageSource(imgCols,imgRows,oneDPix,0,imgCols));
	}
	
	public int[][][] convertToThreeDim(int[] oneDPix,int imgCols,int imgRows){
		int[][][] data = new int[imgRows][imgCols][4];

		for(int row = 0;row < imgRows;row++){
			int[] aRow = new int[imgCols];
			for(int col = 0; col < imgCols;col++){
				int element = row * imgCols + col;
				aRow[col] = oneDPix[element];
			}
			for(int col = 0;col < imgCols;col++){
				data[row][col][0] = (aRow[col] >> 24) & 0xFF;//Alpha data
				data[row][col][1] = (aRow[col] >> 16) & 0xFF;//Red data
				data[row][col][2] = (aRow[col] >> 8)  & 0xFF;//Green data
				data[row][col][3] = (aRow[col]) & 0xFF;//Blue data
			}
		}
		return data;
	}
	
	public int[] convertToOneDim(int[][][] data,int imgCols,int imgRows){
		int[] oneDPix = new int[imgCols * imgRows * 4];

		for(int row = 0,cnt = 0;row < imgRows;row++){
			for(int col = 0;col < imgCols;col++){
				oneDPix[cnt] = ((data[row][col][0] << 24) & 0xFF000000)
                     		 | ((data[row][col][1] << 16) & 0x00FF0000)
                     		 | ((data[row][col][2] << 8) & 0x0000FF00)
                     		 | ((data[row][col][3]) & 0x000000FF);
				cnt++;
			}
		}
		return oneDPix;
	}
	
	@Override
	public void paint(Graphics g){
	    if (rawImg != null) {
	    	g.drawImage(rawImg,20,20,this);
	    }
	    if (modImg != null) {
	    	g.drawImage(modImg,40 + 512,20,this);
	    }
	}
}