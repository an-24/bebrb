package org.bebrb.server.utils;

@SuppressWarnings("serial")
public class DoubleOrInt extends Number {
	    private Number number;

	    public DoubleOrInt(Number n) {
	        number = n;
	    }

	    @Override
	    public double doubleValue() {
	        return number.doubleValue();
	    }

	    @Override
	    public float floatValue() {
	        return number.floatValue();
	    }

	    @Override
	    public int intValue() {
	        return number.intValue();
	    }

	    @Override
	    public long longValue() {
	        return number.longValue();
	    }

	    public Class<? extends Number> getNumberClass() {
	        return number.getClass();
	    }

	    @Override
	    public String toString() {
	        return number.toString();
	    }

}
