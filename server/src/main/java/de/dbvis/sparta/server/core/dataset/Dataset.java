package de.dbvis.sparta.server.core.dataset;

import de.dbvis.sparta.server.rest.model.intersection.ReferencedItem;
import de.dbvis.sparta.server.rest.model.basic.*;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class stores the dataset, i.e., representations of dependencies, bugs, vulnerabilities,
 * modules and repositories. It is used by the REST resource classes.
 */
public abstract class Dataset implements Serializable {

    private static final long serialVersionUID = 20201008L;
    private static final String DATASET_SER = "dataset.ser";

    private static final Logger log = Logger.getLogger(Dataset.class.getName());

    protected List<LibraryFile> files;
    protected List<Bug> bugs;
    protected List<Vulnerability> vulnerabilities;
    protected List<Set<Integer>> bugIdSets;
    protected List<Module> modules;
    protected List<Repository> repositories;
    protected List<BugCount> bugCounts;

    private List<ReferencedItem> referencedBugs;
    private List<ReferencedItem> referencedFiles;
    private List<ReferencedItem> referenceModules;
    private List<ReferencedItem> referencedRepository;

    /**
     * Initializes the data structure by loading all data.
     * This method is called only once.
     *
     * @return the dataset
     */
    public abstract Dataset initialize();

    /**
     * Returns all library files stored in the database.
     *
     * @return a list of LibraryFile objects
     */
    public List<LibraryFile> getFiles() {
        return files;
    }

    /**
     * Returns all bugs stored in the database.
     *
     * @return a list of Bug objects
     */
    public List<Bug> getBugs() {
        return bugs;
    }

    /**
     * Returns all vulnerabilities stored in the database.
     *
     * @return a list of Vulnerability objects
     */
    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public List<Set<Integer>> getBugIdSets() {
        return bugIdSets;
    }

    /**
     * Returns all modules stored in the database.
     *
     * @return a list of Module objects
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Returns all repository stored in the database.
     *
     * @return a list of Repository objects
     */
    public Collection<Repository> getRepositories() {
        return repositories;
    }

    /**
     * Returns the bug counts of all bugs in the database.
     *
     * @return a list of BugCount objects
     */
    public List<BugCount> getBugCounts() {
        return bugCounts;
    }

    public List<ReferencedItem> getReferencedBugs() {
        return referencedBugs;
    }

    public List<ReferencedItem> getReferencedFiles() {
        return referencedFiles;
    }

    public List<ReferencedItem> getReferenceModules() {
        return referenceModules;
    }

    public List<ReferencedItem> getReferencedRepository() {
        return referencedRepository;
    }

    protected void updateFilesWithBugIds() {
        files.parallelStream().forEach(file -> {
            final Set<Integer> bugIds = new HashSet<Integer>();
            for (Bug bug : bugs) {
                if (bug.getFile() != null && bug.getFile().equals(file)) {
                    bugIds.add(bug.getId());
                }
            }
            file.setBugIds(bugIds);
        });
    }

    protected void initializeAllReferencedItems() {
        ReferencedItemFactory rif = new ReferencedItemFactory(bugs, files, modules, repositories, vulnerabilities);
        referencedBugs = rif.createReferencedBugs();
        referencedFiles = rif.createReferencedFiles();
        referenceModules = rif.createReferencedModules();
        referencedRepository = rif.createReferencedRepositories();
    }

    protected void serialize() {
        if (new File(DATASET_SER).exists()) {
            log.info("Serialized data file " + DATASET_SER + " already exists.");
            return;
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(DATASET_SER);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            log.info("Serialized data is saved in " + DATASET_SER + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean tryLoadFromSerializedDataFile() {
        Dataset loaded = deserialize();
        if (loaded == null) {
            return false;
        }
        files = loaded.files;
        bugs = loaded.bugs;
        vulnerabilities = loaded.vulnerabilities;
        bugIdSets = loaded.bugIdSets;
        modules = loaded.modules;
        repositories = loaded.repositories;
        bugCounts = loaded.bugCounts;

        referencedBugs = loaded.referencedBugs;
        referencedFiles = loaded.referencedFiles;
        referenceModules = loaded.referenceModules;
        referencedRepository = loaded.referencedRepository;
        return true;
    }

    private Dataset deserialize() {
        log.info("Loading serialized data from " + DATASET_SER + ".");
        Dataset dataset = null;
        try {
            FileInputStream fileIn = new FileInputStream(DATASET_SER);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            dataset = (Dataset) in.readObject();
            in.close();
            log.info("Data loaded from " + DATASET_SER + ".");
        } catch (FileNotFoundException e) {
            log.info("Serialized data file " + DATASET_SER + " not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }

}
