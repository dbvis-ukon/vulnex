package de.dbvis.sparta.server.core.dataset.steady;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.core.dataset.steady.factories.BugFactory;
import de.dbvis.sparta.server.core.dataset.steady.factories.LibraryFileFactory;
import de.dbvis.sparta.server.core.dataset.steady.factories.ModuleFactory;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;
import de.dbvis.sparta.server.rest.model.basic.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class stores the dataset. The initialization method is tailored to the Vulas database from Eclipse Steady.
 */
public class SteadyDataset extends Dataset {

    private static final Logger log = Logger.getLogger(SteadyDataset.class.getName());

    private static final VulasDatabaseAdapter databaseAdapter = VulasDatabaseAdapter.getInstance();

    // Thread-safe initialization of singleton
    private static SteadyDataset instance = new SteadyDataset();

    public static SteadyDataset getInstance() {
        return instance;
    }

    @Override
    public Dataset initialize() {
        log.info("Building data structures.");

        Map<String, LibraryFile> fileMap =
                new LibraryFileFactory().createFilesFromCachedResult(databaseAdapter.retrieveAllLibrariesAsCachedResult());
        files = new ArrayList<>(fileMap.values());
        bugs = new BugFactory().createBugsFromCachedResult(databaseAdapter.retrieveAllBugsAsCachedResult(), fileMap);

        // Link files with bugs (reference by ID)
        updateFilesWithBugIds();

        // TODO Load vulnerabilities
        vulnerabilities = null;

        bugIdSets = null;

        List<Module> plainModules = new ModuleFactory().createPlainModulesFromCachedResult(databaseAdapter.retrieveAllPlainModulesAsCachedResult());

        // TODO Load modules
        modules = null;

        // TODO Load bug counts
        bugCounts = null;

        log.info("All data structures ready.");
        return this;
    }

}
