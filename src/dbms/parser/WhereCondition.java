package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereCondition {
	public String[] Where(String condition) {
		if (condition == null) {
			return new String[0];
		}

		String WhereCondtionPattern = "(\\w+)\\s*(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))";
		Pattern pat = Pattern.compile(WhereCondtionPattern);
		Matcher ma = pat.matcher(condition);
		String[] cndition = new String[3];
		if (ma.find()) {
			cndition[0] = ma.group(1);
			cndition[1] = ma.group(2);
			cndition[2] = ma.group(3);
			if (cndition[2].endsWith("'")) {
				cndition[2] = cndition[2].substring(1, cndition[2].length() - 1);
			}
		}
		return cndition;
	}
}
