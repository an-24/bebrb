package org.bebrb.forms;

import org.bebrb.data.Record;
import org.bebrb.forms.controls.DataGrid;
import org.bebrb.forms.controls.DataGrid.Column;

public interface OnSelectData {
	/**
	 * Событие происходит при изменении текущей записи 
	 * @param grid таблица
	 * @param newr запись после перехода
	 * @param oldr запись до перехода
	 */
	public void onSelectRecord(DataGrid grid, Record newr, Record oldr);
	/**
	 * Событие происходит при изменении текущей записи или колонки
	 * @param grid таблица
	 * @param newc колонка после перехода
	 * @param oldc колонка до перехода
	 */
	public void onSelectCell(DataGrid grid, Column newc, Column oldc);
}
