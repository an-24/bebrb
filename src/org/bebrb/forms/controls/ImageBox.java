package org.bebrb.forms.controls;

import java.sql.Blob;

import org.bebrb.forms.Control;
import org.bebrb.utils.Color;

public interface ImageBox extends Control<Blob> {
	public static enum TypeScale{None,Center,Fill};
	/**
	 * Цвет для прозрачных пикселей
	 * @return не может быть null
	 */
	public Color getBackground();
	/**
	 * Адрес для загрузки картинки
	 * @return может быть null, тогда картинка загружается из поля {@link #getField()}
	 */
	public String getSrc();
	/**
	 * Загрузка картинки из файла
	 * @param fname имя файла
	 */
	public void loadFromFile(String fname);
	/**
	 * Загрузка картинки по адресу
	 * @param src Url
	 */
	public void loadFromUrl(String src);
	/**
	 * Тип масштабирования
	 * @return не может быть null, по умолчанию {@link TypeScale#None}
	 */
	public TypeScale getScale();
}
