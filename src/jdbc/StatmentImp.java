package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import dbms.parser.Parser;
import dbms.parser.SelectStatement;

@SuppressWarnings("all")
public class StatmentImp implements Statement {

	private Parser parser;

	private ArrayList<String> batchsToExcute;

	private Connection currentConnection;

	// private String databaseUrl;

	private boolean closeState = false;

	// last operation result as an object
	private ResultsetImp result;
	private ArrayList<ResultsetImp> allResults;

	// time for the driver to wait the query to execute
	// private long queryTimeout;

	private int updatecounter;

	private boolean isResult = false;

	// private boolean isCount = false;

	public StatmentImp(ConnectionImp c, String databaseUrl) {
		// this.databaseUrl = databaseUrl;
		this.currentConnection = c;
		this.parser = c.parser;
		result = null;
		allResults = new ArrayList<ResultsetImp>();
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		if (this.batchsToExcute == null)
			this.batchsToExcute = new ArrayList<>();
		if (sql != null)
			this.batchsToExcute.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			throw new SQLException();
		}
		if (!(this.batchsToExcute == null || this.batchsToExcute.size() == 0))
			this.batchsToExcute.clear();
	}

	@Override
	public void close() throws SQLException {
		this.closeState = true;
		if (!(this.batchsToExcute == null || this.batchsToExcute.size() == 0))
			this.batchsToExcute.clear();
		this.currentConnection = null;
		for(int i=0;i<allResults.size();i++){
			if(allResults.get(i) == null)continue;
			allResults.get(i).close();
		}
		this.result = null;
		parser = null;
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		if (currentConnection.isClosed() || this.isClosed()) {
			this.close();
			throw new SQLException();
		}
		parser.InsertQuery(sql);
		// result=new
		//getSelectdTable() return null if not select query 
		//result = new ResultsetImp(parser.getDatabaseManger().getSelectdTable(),this);
		allResults.add(result);
		updatecounter = parser.getDatabaseManger().getChangedRowNumber();		
		if (result.isReal()) {
			isResult = true;
		}
		else{
			isResult = false;
		}		
		return isResult;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		int[] updateCounterArray = new int[batchsToExcute.size()];
		for (int i = 0; i < batchsToExcute.size(); i++) {
			try {
				execute(batchsToExcute.get(i));
				//don't undersatand this.
				updateCounterArray[i] = (isResult) ? SUCCESS_NO_INFO : updatecounter;
			} catch (Exception e) {
				updateCounterArray[i] = EXECUTE_FAILED;
			}
		}
		return updateCounterArray;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		SelectStatement se = new SelectStatement();
		if (se.isValid(sql)) {
			execute(sql);
			return result;
		}
		throw new SQLException();
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		if (execute(sql))
			throw new SQLException();

		return updatecounter;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		return currentConnection;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		ResultsetImp temp_result = result;
		result = new ResultsetImp(null,this);
		return (temp_result.isReal()) ? null : temp_result;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			throw new SQLException();
		}
		int temp_count = updatecounter;
		updatecounter = -1;
		return isResult ? -1 : updatecounter;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closeState;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {

		return null;
	}

	@Override
	public void cancel() throws SQLException {

	}

	@Override
	public void clearWarnings() throws SQLException {

	}

	@Override
	public void closeOnCompletion() throws SQLException {

	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {

		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {

		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {

		return false;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {

		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {

		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {

		return 0;
	}

	@Override
	public int getFetchDirection() throws SQLException {

		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {

		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {

		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {

		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {

		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {

		return false;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {

		return false;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {

		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {

		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {

		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {

		return null;
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {

		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {

		return false;
	}

	@Override
	public void setCursorName(String name) throws SQLException {

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {

	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {

	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {

	}

	@Override
	public void setMaxRows(int max) throws SQLException {

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {

	}

}
