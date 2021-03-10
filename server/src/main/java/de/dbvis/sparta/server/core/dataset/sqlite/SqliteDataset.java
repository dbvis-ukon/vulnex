package de.dbvis.sparta.server.core.dataset.sqlite;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.core.dataset.sqlite.factories.*;
import de.dbvis.sparta.server.rest.model.basic.*;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class stores the dataset. The initialization method is tailored to the SQLite database.
 */
public class SqliteDataset extends Dataset {

    private static final Logger log = Logger.getLogger(SqliteDataset.class.getName());

    private static final SqliteDatabaseAdapter databaseAdapter = SqliteDatabaseAdapter.getInstance();

    // Thread-safe initialization of singleton
    private static final SqliteDataset instance = new SqliteDataset();

    private transient List<Module> plainModules;

    private SqliteDataset() {
        log.info("Instantiated cached dataset.");
    }

    public static SqliteDataset getInstance() {
        return instance;
    }

    @Override
    public Dataset initialize() {

        if (tryLoadFromSerializedDataFile()) {
            return this;
        }

        log.info("Building data structures.");

        // Load plain/unlinked/unreferenced data
        plainModules = new PlainModuleFactory().createPlainModulesFromCachedResult(databaseAdapter.retrieveAllModulesAsCachedResult());
        files = new LibraryFileFactory().createFilesFromCachedResult(databaseAdapter.retrieveAllFilesAsCachedResult());
        bugs = new BugFactory().createBugsFromCachedResult(databaseAdapter.retrieveAllBugsAsCachedResult());

        // Link files with bugs (reference by ID)
        updateFilesWithBugIds();

        vulnerabilities = new VulnerabilityFactory().createVulnerabilitiesFromCachedResult(databaseAdapter.retrieveAllVulnerabilitiesAsCachedResult());
        bugIdSets = new ModuleInfoFactory().createBugIdSetsForAllModules();
        modules = new ModuleFactory().createModules();

        repositories = new RepositoryFactory().createReportsFromCachedResult(
                databaseAdapter.retrieveAllRepositoriesAsCachedResult(),
                databaseAdapter.retrieveAllMetaInfoAsCachedResult());
        bugCounts = retrieveBugCounts();

        log.info("Basic data structure is ready.");

        initializeAllReferencedItems();

        log.info("All data structures ready.");

        serialize();

        return this;
    }

    private List<BugCount> retrieveBugCounts() {
        try {
            CachedRowSet cachedRowSet = databaseAdapter.retrieveBugCounts();
            List<BugCount> result = new ArrayList<BugCount>(cachedRowSet.size());
            while (cachedRowSet.next()) {
                result.add(new BugCount(cachedRowSet.getInt(1),
                        cachedRowSet.getString(2),
                        cachedRowSet.getInt(3)));

            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Module> getPlainModules() {
        return plainModules;
    }

}
