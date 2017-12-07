package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dataBase.control.DataBaseControlImpl;

public class DropTableStatement implements IStatment {
	// private DataBaseControl Dpms = DataBaseControlImpl.getInstance();
	private String dropTablePattern = "^\\s*((?i)drop)\\s+((?i)table)\\s+(\\w+)\\s*;?\\s*$";

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(dropTablePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();

	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(dropTablePattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {
			Dpms.dropTable(ma.group(3));
		}
	}

}
