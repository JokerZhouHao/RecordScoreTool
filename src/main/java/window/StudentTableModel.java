package window;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections4.map.HashedMap;

import entity.Student;
import utility.Global;
import utility.excel.ExcelReader;

public class StudentTableModel extends AbstractTableModel {
	private List<Student> stus = new ArrayList<>();
	private Map<String, Integer> id2Row = new HashedMap<>();
	private List<String> colNames = new ArrayList<>();
	
	/**
	 * @param filePath 带有学号，姓名信息的xlsx格式的表格路径
	 * @param startRow 从1开始
	 * @param startCol 从1开始
	 * @param numStu 学生数
	 * @throws Exception
	 */
	public StudentTableModel(String filePath, int startRow, int startCol, int scoreCol, int numStu) throws Exception{
		this.init(filePath, startRow, startCol, scoreCol, numStu);
	}
	
	private void init(String filePath, int startRow, int startCol, int scoreCol, int numStu) throws Exception{
		colNames.add("序号");
		colNames.add("学号");
		colNames.add("姓名");
		colNames.add("成绩");
		
		String path = filePath;
		int counter = 0;
		ExcelReader reader = new ExcelReader(path);
		for(int row = startRow; row < startRow + numStu; row++) {
			Student stu = new Student();
			stu.setNumber(String.valueOf(counter + 1));
			stu.setId(reader.get(row, startCol));
			stu.setName(reader.get(row, startCol + 1));
			stu.setScore(reader.get(row, scoreCol));
			this.stus.add(stu);
			id2Row.put(stu.getId(), counter);
			counter++;
		}
		reader.close();
	}
	
	@Override
	public int getRowCount() {
		return stus.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colNames.get(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0)	return String.valueOf(rowIndex + 1);
		Student stu = stus.get(rowIndex);
		switch(columnIndex) {
		case 1: return stu.getId();
		case 2: return stu.getName();
		case 3: return stu.getScore();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Student stu = stus.get(rowIndex);
		switch(columnIndex) {
		case 1: stu.setId((String)aValue);	break;
		case 2: stu.setName((String)aValue);	break;
		case 3: stu.setScore((String)aValue);	break;
		}
	}
	
	public Student getStudent(String id) {
		Integer index = null;
		if(null == (index = id2Row.get(id)))	return null;
		else return stus.get(index);
	}

	public List<Student> getStus() {
		return stus;
	}
}
