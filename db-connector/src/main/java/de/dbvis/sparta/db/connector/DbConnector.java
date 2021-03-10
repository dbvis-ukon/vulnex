package de.dbvis.sparta.db.connector;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;

import java.sql.*;

/**
 * Provides a basic database connector.
 */
public class DbConnector {
		
	private final String driver;
	private final String url;
	
	private Connection connection;

	/**
	 * Creates a DbConnector with the given driver and URL.
	 * @param driver the driver to use
	 * @param url the URL of the database
	 */
	public DbConnector(String driver, String url) {
		this.driver = driver;
		this.url = url;
		tryToEstablishDatabaseConnection();
	}

	/**
	 * Try the establish a connection to the database.
	 */
	private void tryToEstablishDatabaseConnection() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the database connection.
	 * @return the connection
     */
	public Connection getConnection() {
		try {
			if (connection.isClosed()) {
				tryToEstablishDatabaseConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Executes an SQL update
	 * @param sql the SQL statement
	 * @throws SQLException if the update fails
	 */
	public void executeUpdate(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute(sql);
		statement.close();
	}

	/**
	 * Executes a SQL query and creates a cached result. 
	 * @param query the SQL query
	 * @return the CachedRowSet of the SQL query
	 * @throws SQLException if the query fails
	 */
	public CachedRowSet executeQueryWithCachedResult(String query) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		CachedRowSet cachedRowSet = new CachedRowSetImpl();
		cachedRowSet.populate(resultSet);
		resultSet.close();
		return cachedRowSet;
	}

	/**
	 * Executes a prepared SQL query and creates a cached result.
	 * @param preparedStatement the prepared SQL query
	 * @return the CachedRowSet of the SQL query
	 * @throws SQLException if the query fails
	 */
	public CachedRowSet executePreparedQueryWithCachedResult(PreparedStatement preparedStatement) throws SQLException {
		ResultSet resultSet = preparedStatement.executeQuery();
		CachedRowSet cachedRowSet = new CachedRowSetImpl();
		cachedRowSet.populate(resultSet);
		resultSet.close();
		return cachedRowSet;
	}
	
	@Override
	public String toString() {
		return DbConnector.class.getName() + "[driver=" + driver + ", url=" + url + "]";
	}
	
}