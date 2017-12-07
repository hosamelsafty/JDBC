package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class CreateDatabaseStatement implements IStatment {
	String creatPattern = "^(\\s*)((?i)create)(\\s+)((?i)database)(\\s+)(\\w+)(\\s*);?(\\s*)$";

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		return ma.matches();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		// TODO Auto-generated method stub
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			Dpms.createDataBase(ma.group(6));

		}

	}
}
