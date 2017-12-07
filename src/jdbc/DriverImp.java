package jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverImp implements Driver {

	private final String xmldbProtocol = "jdbc:xmldb://localhost";
	private final String jsondbProtocol = "jdbc:altdb://localhost";
	/**
	 * all dataBases path.
	 */
	private String path = new String();

	public DriverImp() {
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		if (!url.equals(xmldbProtocol) && !url.equals(jsondbProtocol)) {
			return false;
		}
		return true;
	}

	/**
	 * Attempts to make a database connection to the given URL. The driver
	 * should return "null" if it realizes it is the wrong kind of driver to
	 * connect to the given URL. This will be common, as when the JDBC driver
	 * manager is asked to connect to a given URL it passes the URL to each
	 * loaded driver in turn. The driver should throw an SQLException if it is
	 * the right driver to connect to the given URL but has trouble connecting
	 * to the database.
	 * 
	 * The java.util.Properties argument can be used to pass arbitrary string
	 * tag/value pairs as connection arguments. Normally at least "user" and
	 * "password" properties should be included in the Properties object.
	 * 
	 * @param url
	 *            - the URL of the database to which to connect.
	 * @param info
	 *            - a list of arbitrary string tag/value pairs as connection
	 *            arguments. Normally at least a "user" and "password" property
	 *            should be included.
	 * @return - a Connection object that represents a connection to the URL
	 */
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			return null;
		}
		// info should include at least user and password.
		String path = info.getProperty("path");
		if (path == null) {
			// connect should throw exception if it has a trouble connecting
			// dataBase.
			throw new SQLException();
		}
		File dataBasesFolder = new File(path);

		// why that ??
		// failed to make directory.
		if (!dataBasesFolder.exists()) {
			if (!dataBasesFolder.mkdirs()) {
				throw new SQLException();
			}
		}
		// write path in file and read it when necessary.
		File x = new File("path");
		File paths = new File(x.getAbsolutePath(), "path.txt");
		paths.mkdirs();

		// add that as this.path is empty
		this.path = path;
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.println(this.path);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String type = (url == xmldbProtocol) ? "xml" : "json";
		return new ConnectionImp(info.getProperty("path"), type);
	}

	/**
	 * Gets information about the possible properties for this driver. The
	 * getPropertyInfo method is intended to allow a generic GUI tool to
	 * discover what properties it should prompt a human for in order to get
	 * enough information to connect to a database. Note that depending on the
	 * values the human has supplied so far, additional values may become
	 * necessary, so it may be necessary to iterate though several calls to the
	 * getPropertyInfo method.
	 * 
	 * @param url
	 *            - the URL of the database to which to connect
	 * @param info
	 *            - a proposed list of tag/value pairs that will be sent on
	 *            connect open
	 * @return an array of DriverPropertyInfo objects describing possible
	 *         properties. This array may be an empty array if no properties are
	 *         required.
	 * @throws SQLException
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		final int oneProperty = 1;
		DriverPropertyInfo[] proArray = new DriverPropertyInfo[oneProperty];
		// note that property might be null.
		DriverPropertyInfo object = new DriverPropertyInfo("path", info.getProperty("path"));
		proArray[0] = object;
		// note that proArray might be empty.
		return proArray;
	}

	@Override
	public int getMajorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean jdbcCompliant() {
		throw new UnsupportedOperationException();
	}

}
