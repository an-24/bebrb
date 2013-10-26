package org.bebrb.utils;

public class Color {
	private int cl;
	private boolean transparent;

	public Color() {
		transparent = true;
	}
	
	public Color(int cl) {
		this.cl = cl;
		transparent = false;
	}

	public int getColor() {
		return cl;
	}

	public void setColor(int cl) {
		this.cl = cl;
		transparent = false;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
	
	

}
