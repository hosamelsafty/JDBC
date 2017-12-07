package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class UpdateStatement implements IStatment {
	// private DataBaseControl Dpms = DataBaseControlImpl.getInstance();

	private String updatepattern = "^\\s*((?i)update)\\s+(\\w+)\\s+((?i)set)\\s+"
			+ "((\\s*(\\w+)\\s*=\\s*(('[^']*')|(\\d+))\\s*,)*(\\s*(\\w+)\\s*=\\s*(('[^']*')|(\\d+))))"
			+ "(\\s*((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?\\s*$";

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(updatepattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		// TODO Auto-generated method stub
		Pattern pat = Pattern.compile(updatepattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {

			String tableName = ma.group(2);
			ArrayList<ArrayList<String>> clmAndVlu = colomValuesSpliter(ma.group(4));
			String[] wherecondition = new WhereCondition().Where(ma.group(17));
			ArrayList<String> columns = new ArrayList<>();

			ArrayList<String> value = new ArrayList<>();
			for (ArrayList<String> st : clmAndVlu) {
				columns.add(st.get(0));
				value.add(st.get(1));
			}
			Dpms.updateTable(columns, value, wherecondition, tableName);
		}

	}

	public ArrayList<ArrayList<String>> colomValuesSpliter(String colomStateMent) {
		ArrayList<ArrayList<String>> clmAndVlu = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		// if (colomStateMent.endsWith("'"))
		colomStateMent = colomStateMent.replaceAll("'\\s*$", "");
		colomStateMent = colomStateMent.replaceAll("\\s*=\\s*'?", "=");
		colomStateMent = colomStateMent.replaceAll("'\\s*", "'");
		colomStateMent = colomStateMent.replaceAll("(\\d+)\\s*,\\s*", "$1',");
		String oneClm = "\\s*(\\w+)\\s*=\\s*(.*)\\s*";
		Pattern pa = Pattern.compile(oneClm);
		ArrayList<String> coloms = new ArrayList<String>(Arrays.asList(colomStateMent.split("((('))\\s*,\\s*)")));
		for (String ss : coloms) {
			Matcher ma = pa.matcher(ss);
			if (ma.find()) {
				temp = new ArrayList<>();
				temp.add(ma.group(1));
				temp.add(ma.group(2));
				clmAndVlu.add(temp);
			}
		}
		return clmAndVlu;
	}

}
