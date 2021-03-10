package de.dbvis.sparta.server.core.dataset.vulas;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.core.dataset.vulas.factories.BugFactory;
import de.dbvis.sparta.server.core.dataset.vulas.factories.LibraryFileFactory;
import de.dbvis.sparta.server.core.dataset.vulas.factories.ModuleFactory;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;
import de.dbvis.sparta.server.rest.model.basic.Module;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class stores the dataset. The initialization method is tailored to the Vulas database from Eclipse Steady.
 */
public class VulasDataset extends Dataset {

    private static final Logger log = Logger.getLogger(VulasDataset.class.getName());

    private static final VulasDatabaseAdapter databaseAdapter = VulasDatabaseAdapter.getInstance();

    // Thread-safe initialization of singleton
    private static VulasDataset instance = new VulasDataset();

    public static VulasDataset getInstance() {
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
