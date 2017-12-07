package dbms.parser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.eztech.util.JavaClassFinder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import dataBase.control.DataBaseControlImpl;

public class Parser implements IParser {

	private DataBaseControlImpl Dpms;
	private List<Class<? extends IStatment>> classes;

	public Parser(String path, String type) {
		this.Dpms = new DataBaseControlImpl(path, type);
		JavaClassFinder classFinder = new JavaClassFinder();
		classes = classFinder.findAllMatchingTypes(IStatment.class);

	}

	@Override
	public void InsertQuery(String query) throws SQLException {
		boolean syntex = true;
		for (Class clazz : classes) {

			Constructor<?> cons = null;
			try {
				if (!clazz.getName().equals("dbms.parser.IStatment"))
					cons = clazz.getConstructor(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (!clazz.getName().equals("dbms.parser.IStatment")) {
					IStatment object = (IStatment) cons.newInstance();
					System.out.println(object.getClass());
					if (object.isValid(query)) {
						object.excute(query, this.Dpms);
						syntex = false;
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (syntex)
			throw new SQLException("syntex    error");

	}

	public DataBaseControlImpl getDatabaseManger() {
		return this.Dpms;
	}

	public static void main(String[] args) {
		File x = new File("databases");
		Scanner in = new Scanner(System.in);

		Parser a = new Parser(x.getAbsolutePath(), null);
		try {
			a.InsertQuery("use red;");
			ArrayList<String> names = new ArrayList<String>();
			names.add("age1");
			names.add("names1");
			ArrayList<String> types = new ArrayList<String>();
			types.add("int");
			types.add("varchar");
			a.Dpms.alterTable("id", "ADD", names, types);
		} catch (Exception e1) {
			// TODO Auto-generated catch block

		}

		while (true) {
			try {
				a.InsertQuery(in.nextLine());

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}
}