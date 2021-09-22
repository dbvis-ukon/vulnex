package de.dbvis.sparta.server.core.dataset.steady;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.rest.model.basic.Bug;
import de.dbvis.sparta.server.rest.model.basic.BugCount;
import de.dbvis.sparta.server.rest.model.basic.Vulnerability;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class stores the dataset. The initialization method is tailored to the Vulas database from Eclipse Steady.
 */
public class SteadyDataset extends Dataset {

    private static final Logger log = Logger.getLogger(SteadyDataset.class.getName());

    private static final SteadyRestApiAdapter databaseAdapter = SteadyRestApiAdapter.getInstance();

    // Thread-safe initialization of singleton
    private static SteadyDataset instance = new SteadyDataset();

    public static SteadyDataset getInstance() {
        return instance;
    }

    @Override
    public Dataset initialize() {
        try {
            log.info("Building data structures.");

            SteadyRestApiAdapter steadyRestApiAdapter = SteadyRestApiAdapter.getInstance();
            steadyRestApiAdapter.retrieveData();

            files = steadyRestApiAdapter.getFiles();

            bugs = steadyRestApiAdapter.getBugs();

            // Link files with bugs (reference by ID)
            updateFilesWithBugIds();

            vulnerabilities = steadyRestApiAdapter.getVulnerabilities();

            modules = steadyRestApiAdapter.getModules();

            bugIdSets = createBugIdSetsForAllModules(modules, vulnerabilities);

            repositories = steadyRestApiAdapter.getRepositories();

            bugCounts = createBugCounts(bugs, vulnerabilities);

            log.info("Basic data structure is ready.");

            initializeAllReferencedItems();

            log.info("All data structures ready.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private List<BugCount> createBugCounts(List<Bug> bugs, List<Vulnerability> vulnerabilities) {
        List<BugCount> result = new ArrayList<BugCount>();
        for (Bug b : bugs) {
            long count = vulnerabilities.stream()
                    .filter(e -> e.getBug().getId() == b.getId())
                    .count();
            new BugCount(b.getId(), b.getName(), (int) count);
        }
        return result;
    }

}
