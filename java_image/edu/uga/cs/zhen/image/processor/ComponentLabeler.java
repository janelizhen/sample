package edu.uga.cs.zhen.image.processor;

import java.util.*;

public class ComponentLabeler implements ImgProcesser {
	
	private int numOfComponents;
	private double avgSize;
	
	public int getNumOfComponents(){
		return numOfComponents;
	}
	
	public double avgSize(){
		return avgSize;
	}

	@Override
	public int[][][] processImg(int[][][] threeDPix) {
		// TODO Auto-generated method stub
		int[][][] data = threeDPix;
		int imgRow = threeDPix.length;
		int imgCol = threeDPix[0].length;
		
		int[][] label = new int[imgRow][imgCol];
		int maxLabel = 0;
		List<Integer> list = new ArrayList<Integer>();
		int total = 0;
		
		for (int i=0;i<imgRow;i++){
			for (int j=0;j<imgCol;j++){
				int value = data[i][j][3];
				if (value == 255 || value == 0){
					if (value == 255){
						total++;
						if (i>=1 && j>=1){
							// has upper and left neighbor
							int valueUpper = data[i-1][j][1];
							int valueLeft = data[i][j-1][1];
							if (valueUpper==0 && valueLeft==0){
								list.add(maxLabel, null);
								label[i][j] = maxLabel++;
							}
							else{
								if (valueUpper==0 && valueLeft==255) label[i][j] = label[i][j-1];
								else if (valueLeft==0 && valueUpper==255) label[i][j] = label[i-1][j];
								else {
									label[i][j] = label[i][j-1];
									if (label[i][j-1] != label[i-1][j]){
										list.set(label[i][j-1], new Integer(label[i-1][j]));
									}
								}
							}
						}
						else if (i>=1){
							// only has upper neighbor
							int valueUpper = data[i-1][j][1];
							if (valueUpper==0){
								list.add(maxLabel, null);
								label[i][j] = maxLabel++;
							}
							else{
								label[i][j] = label[i-1][j];
							}
						}
						else if (j>=1){
							// only has left neighbor
							int valueLeft = data[i][j-1][1];
							if (valueLeft==0){
								list.add(maxLabel, null);
								label[i][j] = maxLabel++;
							}
							else{
								label[i][j]=label[i][j-1];
							}
						}
						else{
							// first pixel
							list.add(maxLabel, null);
							label[i][j] = maxLabel++;
						}
					}
				}
			}
		}
		int num = 0;
		Iterator<Integer> itr = list.iterator();
		while (itr.hasNext()){
			if (itr.next() == null) num++;
		}
		
		this.numOfComponents = num;
		this.avgSize = num==0? 0 : total / num;
		return data;
	}
}
