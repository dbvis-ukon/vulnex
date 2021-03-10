package de.dbvis.sparta.server.core.dataset.sqlite;

import de.dbvis.sparta.db.connector.DbConnector;
import de.dbvis.sparta.db.connector.SqLiteDbConnector;
import de.dbvis.sparta.server.rest.model.data.MetaInfoData;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class contains all database necessary database queries. It contains no
 * fields or variables that store any kind of mutable state, to be thread safe.
 * The only field {@link #db}, containing the DbConnector, does not contain any
 * mutable state either.
 */
public class SqliteDatabaseAdapter {

    private static final Logger log = Logger.getLogger(SqliteDatabaseAdapter.class.getName());

    // Thread-safe initialization of singleton
    private static SqliteDatabaseAdapter instance = new SqliteDatabaseAdapter();

    private DbConnector db;

    /**
     * Constructs a SqliteDatabaseAdapter.
     */
    private SqliteDatabaseAdapter() {
        this.db = SqLiteDbConnector.createSqLiteDbConnector();
    }

    /**
     * Returns the single instance of the SqliteDatabaseAdapter class.
     * @return the concrete instance of this class
     */
    public static SqliteDatabaseAdapter getInstance() {
        return instance;
    }

    public CachedRowSet retrieveAllModulesAsCachedResult() {
        final String query = "SELECT * FROM module;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve module info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveAllRepositoriesAsCachedResult() {
        final String query = "SELECT * FROM report;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve report info from database!");
        }
        return null;
    }

    public Map<String, MetaInfoData> retrieveAllMetaInfoAsCachedResult() {
        final Map<String, MetaInfoData> result = new HashMap<String, MetaInfoData>();
        final String query = "SELECT * FROM metainfo;";
        try {
            CachedRowSet cachedRowSet = db.executeQueryWithCachedResult(query);
            while (cachedRowSet.next()) {
                String repo = cachedRowSet.getString(2);
                MetaInfoData metaInfoData = new MetaInfoData(
                        cachedRowSet.getInt(3),
                        cachedRowSet.getString(4),
                        cachedRowSet.getInt(5),
                        cachedRowSet.getInt(6),
                        cachedRowSet.getInt(7)
                );
                result.put(repo, metaInfoData);
            }
        } catch (SQLException e) {
            log.severe("Could not retrieve meta info from database!");
        }
        return result;
    }

    public CachedRowSet retrieveAllFilesAsCachedResult() {
        final String query = "SELECT * FROM file;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve file info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveAllBugsAsCachedResult() {
        final String query = "SELECT * FROM bug;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve bug info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveAllVulnerabilitiesAsCachedResult() {
        final String query = "SELECT * FROM vulnerability;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve vulnerability info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveBugCounts() {
        final String query = "SELECT bug.id, bug.name, count(vulnerability.bugId) AS c " +
                "FROM vulnerability, bug WHERE vulnerability.bugId = bug.id " +
                "GROUP BY vulnerability.bugId ORDER BY c DESC;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            log.severe("Could not retrieve bug counts!");
        }
        return null;
    }

}
