/**
 * 
 */
package org.bebrb.utils;

import java.util.Currency;

/**
 * Представление денег
 */
public class Money {
	public static int DEFAULTFRACTIONDIGITS = 2;
	
	private static final int[] fracs = new int[] { 1, 10, 100, 1000 };	
	private long value;
	private int fracfactor; 

	public Money() {
		this(0,DEFAULTFRACTIONDIGITS);
	}
	
	public Money(long value) {
		this(value,DEFAULTFRACTIONDIGITS);
	}

	public Money(long value, Currency currency) {
		this(value,currency.getDefaultFractionDigits());
	}
	
	private Money(long v, int fracfactor) {
		value = v;
		fracfactor = fracs[fracfactor];
	}
	
	
	public void setValue(long amound) {
		this.value = amound;
	}

	public void setValue(long aint, int frac) {
		this.value = aint+fracfactor*frac;
	}

	public void setValue(double amound) {
		this.value = (int)(amound*fracfactor);
	}
	
	public double getAsDouble() {
		return (double)value/fracfactor;
	}
	
	public long getValue() {
		return value;
	}
	
	public void inc(long v) {
		value+=v;
	}
	
	public void dec(long v) {
		value-=v;
	}
	
	/**
	 * Cравнения денег.
	 * @param other с чем сравнить
	 * @return
	 * -1 меньше<br>
	 * 0 равно<br>
	 * 1 больше<br>
	*/
	public int compareTo(Money other) {
	    if(value < other.value) return -1;
	    if(value == other.value)return 0;
	    return 1;
	}	
	
	public static Money sum(Money m1, Money m2) {
		if(m1.fracfactor!=m2.fracfactor) throw new IllegalArgumentException("Different currencies");
		return new Money(m1.value+m2.value);
	}

	public static Money sub(Money m1, Money m2) {
		if(m1.fracfactor!=m2.fracfactor) throw new IllegalArgumentException("Different currencies");
		return new Money(m1.value-m2.value);
	}

	public static int compare(Money m1, Money m2) {
		if(m1.fracfactor!=m2.fracfactor) throw new IllegalArgumentException("Different currencies");
		return m1.compareTo(m2);
	}
	
}
