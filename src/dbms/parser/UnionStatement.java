package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class UnionStatement implements IStatment {

	private String unionPattern = "^";

	public UnionStatement() {
		String selectPattern = "\\s*((?i)select\\s+)"
				+ "((\\*\\s*)|((\\s*(\\w+)\\s*,)*(\\s*(\\w+)\\s+)))\\s*((?i)from\\s+)(\\w+)"
				+ "(\\s+((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?";
		unionPattern += selectPattern + "(\\s+(?i)union\\s+)" + selectPattern;
		unionPattern += "(\\s+(?i)order\\s+(?i)by\\s+(\\w+)(\\s+((?i)asc|(?i)desc))?)?\\s*;?\\s*$";

	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(unionPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		// TODO Auto-generated method stub

	}

}
