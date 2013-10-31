/**
 * 
 */
package org.bebrb.forms.controls;

import java.util.Date;

import org.bebrb.data.Field;
import org.bebrb.forms.ControlGroup;

/**
 * Ввод интервала дат
 */
public interface RangeBox extends ControlGroup<Void> {
	public static enum RangeType{Day,Week,Month,Year};
	/**
	 * Минимальный шаг изменения интервала
	 * @return не может быть null
	 */
	public RangeType getStep(); 
	
	/**
	 * Поле для записи начала интервала
	 * @return может быть null, если поле не назначено
	 */
	public Field<Date> getFieldStart();
	/**
	 * Поле для записи конца интервала
	 * @return может быть null, если поле не назначено
	 */
	public Field<Date> getFieldFinish();
	/**
	 * Текущее значение начала интервала
	 * @return может быть null
	 */
	public Date getStart();
	/**
	 * Текущее значение конца интервала
	 * @return может быть null
	 */
	public Date getFinish();
	/**
	 * Тест на обязательный ввода начала интервала
	 * @return true если начало обязательно
	 */
	public boolean isRequiredStart();
	/**
	 * Тест на обязательный ввода конца интервала
	 * @return true если клнец обязателен
	 */
	public boolean isRequiredFinish();
	/**
	 * Установка интервала
	 * @param start начало
	 * @param finish конец
	 */
	public void setRange(Date start, Date finish);
	/**
	 * Установка интервала
	 * @param start начало
	 * @param duration длительность
	 * @param rt единица измерения длительности
	 */
	public void setRange(Date start, int duration, RangeType rt);
	/**
	 * Установка интервала в текущем периоде
	 * @param rt тип интервала
	 */
	public void setRange(RangeType rt);
	
	/**
	 * Сдвиг интервала
	 * @param count количество единиц
	 * @param rt  единица измерения длительности
	 */
	public void shift(int count, RangeType rt);
	/**
	 * Сдвиг интервала
	 * @param count количество дней
	 */
	public void shift(int count);
	/**
	 * Изменение верхней границы интервала 
	 * @param count количество единиц
	 * @param rt  единица измерения длительности
	 */
	public void inc(int count, RangeType rt);
	/**
	 * Изменение нижней границы интервала 
	 * @param count количество единиц
	 * @param rt  единица измерения длительности
	 */
	public void dec(int count, RangeType rt);
	
}
