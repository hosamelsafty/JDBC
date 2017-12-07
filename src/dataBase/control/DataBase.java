package dataBase.control;

import java.io.File;
import java.util.ArrayList;

import files.control.*;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp.Factory;
import model.Printer;
import model.PrinterIF;

public class DataBase implements IDataBase {

	private String dataBaseName;
	private ArrayList<String> tables;
	private Table curTable;
	private IWriter saveObj;
	private IReader loadObj;
	private String path;

	public void setTables(ArrayList<String> tables) {
		this.tables = tables;
	}

	public DataBase(String dataBaseName, String path, String type) {
		this.path = path;
		this.dataBaseName = dataBaseName;
		tables = new ArrayList<String>();
		WriterFactory fw = new WriterFactory();
		saveObj = fw.getWriter(type);
		ReaderFactory fr = new ReaderFactory();
		loadObj = fr.getReader(type);
		load();
	}

	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types) {
		File file = makeFile(dataBaseName, tableName, ".xml");
		if (tables.contains(tableName) || file.exists()) {
			throw new RuntimeException("Can't Create This Table as This Name is already exisitng");
		} else {
			curTable = new Table(this, tableName, columnNames, types, this.path);
			PrinterIF printerObj = Printer.getInstance();
			printerObj.printTable(columnNames, curTable.getWantedData(), tableName);
			tables.add(tableName);
		}
	}

	public void dropTable(String tableName) {
		try {
			if (!this.tables.contains(tableName)) {
				throw new RuntimeException();
			}
			File data = makeFile(dataBaseName, tableName, ".xml");
			data.delete();
			File schema = makeFile(dataBaseName, tableName, ".dtd");
			schema.delete();
			tables.remove(tableName);
			this.load();
		} catch (Exception e) {
			throw new RuntimeException("This Table is not exisitng to be dropped");
		}
	}

	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		Table table = makeTable(tableName);
		table.insertIntoTable(columns, values, tableName);
	}

	public void deleteFromTable(String[] conditions, String tableName) {
		Table table = makeTable(tableName);
		table.deleteFromTable(conditions, tableName);
	}

	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName, String coulmnOrder,
			String order, boolean distinct) {
		Table table = makeTable(tableName);
		table.selectFromTable(column, conditions, tableName, coulmnOrder, order, distinct);
	}

	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {
		Table table = makeTable(tableName);
		table.updateTable(columns, value, conditions, tableName);
	}

	public void alterTable(String tableName, String alterType, ArrayList<String> columnNames,
			ArrayList<String> columnTypes) {
		Table table = makeTable(tableName);
		table.alterTable(tableName, alterType, columnNames, columnTypes);
	}

	public void load() {
		File x = new File(this.path);
		File dataBasesFolder = new File(x.getAbsoluteFile(), this.dataBaseName);
		if (dataBasesFolder.exists()) {
			this.tables.clear();
			String[] databaseFiles = dataBasesFolder.list();
			for (int i = 0; i < databaseFiles.length; i++) {
				databaseFiles[i] = databaseFiles[i].substring(0, databaseFiles[i].indexOf('.'));
				if (!this.tables.contains(databaseFiles[i])) {
					this.tables.add(databaseFiles[i]);
				}
			}
		} else {
			throw new RuntimeException("This Data Base is not exisitng to be used");
		}
	}

	public Table getCurrentTable() {
		return curTable;
	}

	private File makeFile(String dataBaseName, String tableName, String extension) {
		if (dataBaseName == "" || tableName == "") {
			throw new RuntimeException("Invalid Names for Data Base , Table");
		}
		File file = new File(this.path);
		String path = file.getAbsolutePath() + File.separator + dataBaseName + File.separator + tableName + extension;
		File filee = new File(path);
		return filee;
	}

	private Table makeTable(String tableName) {
		if (curTable != null && curTable.getTableName() == tableName) {
			return curTable;
		} else {
			if (this.tables.contains(tableName)) {
				File filee = makeFile(this.dataBaseName, tableName, ".xml");
				ArrayList<ArrayList<String>> data = loadObj.load(filee);
				Table table;
				if (data == null || data.size() == 0) {
					table = new Table(this, tableName, loadObj.getCoulmnNames(),
							loadObj.getCoulmnTypes(), this.path);
				} else {
					table = new Table(this, tableName, loadObj.getCoulmnNames(),
							loadObj.getCoulmnTypes(), data, this.path);
				}
				curTable = table;
				return table;
			}
		}
		throw new RuntimeException("this file doesn't exists");
	}

	@Override
	public int getChangedRowNumber() {
		// TODO Auto-generated method stub
		return curTable.getChangedRowNumber();
	}
	
	public IWriter getWriter(){
		return saveObj;
	}
	
	public IReader getReader(){
		return loadObj;
	}
	
	public String getDataBaseName(){
		return this.dataBaseName;
	}

}
