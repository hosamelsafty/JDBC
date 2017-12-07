package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dataBase.control.DataBaseControlImpl;

public class DeleteStatment implements IStatment {
	// private DataBaseControl Dpms = DataBaseControlImpl.getInstance();

	private String deletePattern = "^(\\s*)((?i)delete)(\\s+)((?i)from)(\\s+)(\\w+)"
			+ "(\\s+((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?$";

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(deletePattern);

		Matcher ma = pat.matcher(query);

		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(deletePattern);

		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			Dpms.deleteFromTable(new WhereCondition().Where((ma.group(9))), ma.group(6));
		}
	}

}
