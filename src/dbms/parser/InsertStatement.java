package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class InsertStatement implements IStatment {

	String insertPattern = "";

	public InsertStatement() {
		insertPattern = "^(\\s*)((?i)insert)(\\s+)((?i)into)(\\s+)(\\w+)";
		insertPattern += "((\\s*)\\(((\\s*)(\\w+)(\\s*),)*((\\s*)(\\w+)(\\s*)\\)(\\s*)))?";
		insertPattern += "((\\s+)((?i)values))(\\s*)";
		insertPattern += "\\((((\\s*)(('[^']*')|(\\d+))(\\s*),)*((\\s*)(('[^']*')|(\\d+))))(\\s*)\\)(\\s*)(\\s*);?(\\s*)$";
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(insertPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		String table_name = new String();
		ArrayList<String> coloums = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		Pattern pat = Pattern.compile(insertPattern);
		Matcher ma = pat.matcher(query.replaceAll("\\)(?i)values", "\\) values"));
		if (ma.matches()) {
			table_name = ma.group(6);
			if (ma.group(7) == null)
				coloums = new ArrayList<String>();
			else
				coloums = new ArrayList<String>(
						Arrays.asList(ma.group(7).replaceAll("\\s+", "").replaceAll("[()]", "").split(",")));
			String trim = ma.group(22) + ",";
			while (!trim.replaceAll("\\s+", "").replaceAll("[,]", "").equals("")) {
				Pattern patt = Pattern.compile("('(\\s*[^']+\\s*)')|(\\s*(\\d+)\\s*,)");
				Matcher matc = patt.matcher(trim);

				if (matc.find()) {
					if (trim.charAt(matc.start()) == '\'') {
						value.add(trim.substring(matc.start() + 1, matc.end() - 1));
					} else {
						value.add(trim.substring(matc.start(), matc.end() - 1));
					}
					trim = trim.substring(matc.end());
				}
			}
			Dpms.insertIntoTable(coloums, value, table_name);

		}

	}
}
