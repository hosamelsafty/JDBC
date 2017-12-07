package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dataBase.control.DataBaseControlImpl;

public class DropdatabaseStatement implements IStatment {
	// private DataBaseControl Dpms = DataBaseControlImpl.getInstance();
	private String dropDatabasePattern = "^\\s*((?i)drop)\\s+((?i)database)\\s+(\\w+)\\s*;?\\s*$";

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(dropDatabasePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		// String dropPattern =
		// "^\\s*((?i)drop)\\s+((?i)database)\\s+(\\w+)\\s*;?\\s*$";
		Pattern pat = Pattern.compile(dropDatabasePattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {
			Dpms.dropDataBase(ma.group(3));

		}
	}

}
