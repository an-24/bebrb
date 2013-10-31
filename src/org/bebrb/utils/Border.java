package org.bebrb.utils;

public interface Border {
	public static enum Type{Left, Top, Bottom, Right};
	public boolean isVisible();
	public void setVisible(boolean value);
}
