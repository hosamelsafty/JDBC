package dataBase.control;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataTypesValidator {

	private ArrayList<String> coulmnNames;
	private ArrayList<String> coulmnTypes;

	public DataTypesValidator(ArrayList<String> coulmnNames, ArrayList<String> coulmnTypes) {
		this.coulmnNames = coulmnNames;
		this.coulmnTypes = coulmnTypes;
	}

	public int typeAsInSql(Table table, int indexFormZero) throws SQLException {
		// index starts for zero
		Types type = null;
		String columnType = table.getCoulmnTypes().get(indexFormZero);
		if (columnType.equalsIgnoreCase("varchar")) {
			return type.VARCHAR;
		} else if (columnType.equalsIgnoreCase("int")) {
			return type.INTEGER;
		} else if (columnType.equalsIgnoreCase("date")) {
			return type.DATE;
		} else if (columnType.equalsIgnoreCase("float")) {
			return type.FLOAT;
		} else {
			// not sure about sql exception.
			// yes it is correct as we don't support the other types.
			throw new SQLException();
		}
	}

	public boolean isValidDataType(ArrayList<String> coulmnNames, ArrayList<String> coulmnValues) {
		ArrayList<String> coulmnNames1 = toLow((ArrayList<String>) coulmnNames.clone());
		ArrayList<String> mycoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		if (coulmnNames1.size() == 0) {
			coulmnNames1 = (ArrayList<String>) mycoulmnNames.clone();
		}
		for (int i = 0; i < coulmnValues.size(); i++) {
			if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("varchar")) {
				continue;
			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("date")) {
				if (!isValidDate(coulmnValues.get(i))) {
					return false;
				}
			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("float")) {
				if (!isValidFloat(coulmnValues.get(i))) {
					return false;
				}
			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("int")) {
				// to handle if there is space at first or end
				// String str = coulmnValues.get(i);
				// str = str.trim();
				return isValidInt(coulmnNames.get(i));
			}
		}
		return true;
	}

	private boolean isValidInt(String inputInt) {
		try {
			Integer.parseInt(inputInt);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean isValidDate(String inputDate) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			// strict validation.
			formatter.setLenient(false);
			// if not valid, it will throw ParseException.
			formatter.parse(inputDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private boolean isValidFloat(String inputFloat) {
		// you can handle it using Formatexception to handle -ve case
		try {
			Float.parseFloat(inputFloat);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;

		/*
		 * // i think this part should be handled in parser. if
		 * (inputFloat.matches("[0-9]*\\.?[0-9]+")) { return true; } return
		 * false;
		 */
	}

	private ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}
}
