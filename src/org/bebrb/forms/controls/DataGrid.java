/**
 * 
 */
package org.bebrb.forms.controls;

import java.util.List;
import java.util.Set;

import org.bebrb.data.Attribute;
import org.bebrb.data.DataSource;
import org.bebrb.data.Record;
import org.bebrb.forms.Action;
import org.bebrb.forms.ControlGroup;

/**
 * Таблица с данными
 *
 */
public interface DataGrid extends ControlGroup<Void> {
	public static enum SortType{None,Ask,Desk}
	public static enum Option{canUserReorderColumns,canUserResizeColumns,canUserSorting,canSelectColumn,canGroupRecord};
	/**
	 * Источник данных
	 * @return не может быть null
	 */
	public DataSource getDataSource();
	/**
	 * Заголовок таблицы
	 * @return может быть null. Если строка не установлена заголовок не отображается
	 */
	public String getCaption();
	/**
	 * Панель подвала
	 * @return  может быть null
	 */
	public Panel getFooter();
	/**
	 * Список колонок с данными
	 * @return не может быть null
	 */
	public List<Column> getColumns();
	/**
	 * Список зафиксированных колонок
	 * @return не может быть null
	 */
	public List<Column> getFixedColumn();
	/**
	 * Множество опций
	 * @return не может быть null
	 */
	public Set<Option> getOptions();
	/**
	 * Возвращает объект управления выбранной записью
	 * @return не может быть null
	 */
	public Selector getSelector();
	/**
	 * Объект управляющий группой записей
	 * @return не может быть null
	 */
	public GroupRecords getGroupRecords();
	/**
	 * Управление группой записей
	 */
	public interface GroupRecords {
		/**
		 * Записи в группе
		 * @return не может быть null
		 */
		public List<Record> getRecords();
		/**
		 * Добавить все записи в группу
		 */
		public void addAllRecords();
		/**
		 * Добавить запись в группу
		 * @param r не может быть null
		 */
		public void add(Record r);
		/**
		 * Удалить запись в группу
		 * @param r не может быть null
		 */
		public void remove(Record r);	
		/**
		 * Транспонирование записей в группе
		 */
		public void transpose();
		/**
		 * Очистить группу
		 */
		public void clear();
		/**
		 * Выполнение действия для каждой записи в группе. Текущая запись
		 * в группе передается в метод {@link Action#execute(org.bebrb.forms.Control, Object...)} в 
		 * качестве второго аргумента
		 * @param action не может быть null
		 */
		public void execute(Action action);
	}
	/**
	 * Управление текущей выбранной записью
	 */
	public interface Selector {
		/**
		 * Текущая выбранная запись
		 * @return null если запись не выбрана
		 */
		public Record getRecord();
		/**
		 * Установить текущую запись
		 * @param r не может быть null
		 */
		public void setRecord(Record r);
		/**
		 * Выбранная колонка 
		 * @return может быть null
		 */
		public Column getSelectedColumn();
		/**
		 * Выбрать колонку
		 * @param c не может быть null
		 */
		public void setSelectedColumn(Column c);
		/**
		 * Переход на следующую запись
		 * @return true если после операции не достигнут конец выборки 
		 */
		public boolean next();
		/**
		 * Переход на предыдущую запись
		 * @return true если после операции не достигнуто начало выборки 
		 */
		public boolean prev();
		/**
		 * Тест на конец выборки
		 * @return true если конец достугнут
		 */
		public boolean isEof();
		/**
		 * Тест на начало выборки
		 * @return true если начало выборки достигнуто
		 */
		public boolean isBof();
	}
	/**
	 * Колонка с данными
	 */
	public interface Column {
		/**
		 * Атрибут колонки
		 * @return не может быть null
		 */
		public Attribute getAttribute();
		/**
		 * Установка сортировки
		 * @param sorttype тип сортировки
		 */
		public void setSorting(SortType sorttype);
		/**
		 * Ширина колонки
		 * @return ширина колонки в пикселях
		 */
		public int getWidth();
		/**
		 * Тест на возможность сортировки по колонке
		 * @return true означает возможность сортировки
		 */
		public boolean isCanSorting();
		
	}

}
	