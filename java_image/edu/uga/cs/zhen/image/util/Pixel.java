package edu.uga.cs.zhen.image.util;

public class Pixel {
	private int x,y,value;
	
	public Pixel(int x, int y, int value){
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public Pixel(int x, int y){
		this.x = x;
		this.y = y;
		this.value = 0;
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
}
