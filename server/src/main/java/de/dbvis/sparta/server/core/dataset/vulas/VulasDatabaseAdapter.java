package de.dbvis.sparta.server.core.dataset.vulas;

import de.dbvis.sparta.db.connector.DbConnector;
import de.dbvis.sparta.db.connector.PostgreSqlConnector;
import de.dbvis.sparta.server.Constants;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class contains all database necessary database queries. It contains no
 * fields or variables that store any kind of mutable state, to be thread safe.
 * The only field {@link #db}, containing the DbConnector, does not contain any
 * mutable state either.
 */
public class VulasDatabaseAdapter {

    private static final Logger log = Logger.getLogger(VulasDatabaseAdapter.class.getName());

    /**
     * Database configuration
     **/
    private static final DatabaseConfig DATABASE_CONFIG = new DatabaseConfig(
            "localhost",
            "5432",
            "vulas",
            "postgres",
            "postgres");

    // Thread-safe initialization of singleton
    private static VulasDatabaseAdapter instance = new VulasDatabaseAdapter();

    private DbConnector db;

    /**
     * Constructs a VulasDatabaseAdapter.
     */
    private VulasDatabaseAdapter() {
        try {
            this.db = PostgreSqlConnector.createPostgreSqlConnectorWithNonValidatingSsl(
                    DATABASE_CONFIG.host,
                    DATABASE_CONFIG.port,
                    DATABASE_CONFIG.database,
                    DATABASE_CONFIG.username,
                    DATABASE_CONFIG.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the single instance of the VulasDatabaseAdapter class.
     *
     * @return the concrete instance of this class
     */
    public static VulasDatabaseAdapter getInstance() {
        return instance;
    }

    public CachedRowSet retrieveAllBugsAsCachedResult() {
        final String query = "SELECT id, bug_id, description, cvss_score, cvss_vector, cvss_version FROM bug;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe("Could not retrieve file info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveAllLibrariesAsCachedResult() {
        final String query = "SELECT DISTINCT lib.id, bug_id, filename, digest"
                + " FROM bug_affected_library, lib, app_dependency"
                + " WHERE bug_affected_library.library_id = lib.library_id_id"
                + " AND lib.digest = app_dependency.lib;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe("Could not retrieve bug info from database!");
        }
        return null;
    }

    public CachedRowSet retrieveAllPlainModulesAsCachedResult() {
        final String query = "SELECT id, mvn_group, artifact, version FROM app;";
        try {
            return db.executeQueryWithCachedResult(query);
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe("Could not retrieve module info from database!");
        }
        return null;
    }

    /**
     * Tests the database connection. Retrieves a value from the database and logs it.
     */
    public boolean test() {
        try {
            CachedRowSet cachedRowSet =
                    db.executeQueryWithCachedResult("SELECT * FROM app LIMIT 1;");
            if (cachedRowSet.next()) {
                log.info("Test result: " + cachedRowSet.getLong(1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* TODO Put all database queries and API calls here.
     * See the test() to see how to execute DB queries. Check
     * https://github.com/eclipse/steady/blob/master/rest-backend/src/main/java/com/sap/psr/vulas/backend/repo/ApplicationRepository.java
     * for queries of the database as they are used in Eclipse Steady.
     */

}
