package org.bebrb.utils;

public class Padding {
	private int left=0;
	private int top=0;
	private int right=0;
	private int bottom=0;
	
	public Padding() {
	}

	public Padding(int offs) {
		this(offs,offs,offs,offs);
	}

	public Padding(int lr,int tb) {
		this(lr,tb,lr,tb);
	}

	public Padding(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}


}
