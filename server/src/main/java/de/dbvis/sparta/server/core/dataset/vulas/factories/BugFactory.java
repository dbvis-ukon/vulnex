package de.dbvis.sparta.server.core.dataset.vulas.factories;

import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.core.dataset.vulas.BugFilePair;
import de.dbvis.sparta.server.core.dataset.vulas.VulasDatabaseAdapter;
import de.dbvis.sparta.server.core.dataset.vulas.VulasDataset;
import de.dbvis.sparta.server.rest.model.basic.Bug;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class BugFactory {

    private static final Logger log = Logger.getLogger(BugFactory.class.getName());

    private List<LibraryFile> files;

    public BugFactory() {
        this.files = VulasDataset.getInstance().getFiles();
    }

    /**
     * Creates Bug objects from a database query result.
     * @param cachedRowSet the CachedRowset with the columns id, bug_id, description, cvss_score, cvss_vector, cvss_version
     * @param fileMap a Map of all LibraryFile objects
     * @return The list of Bug objects
     */
    public List<Bug> createBugsFromCachedResult(CachedRowSet cachedRowSet, Map<String, LibraryFile> fileMap) {
        List<Bug> result = new ArrayList<Bug>(cachedRowSet.size());
        try {
            while (cachedRowSet.next()) {
                final String bugName = cachedRowSet.getString(2);
                Bug bug = new Bug(
                        cachedRowSet.getInt(1),
                        determineFileForBug(bugName, fileMap),
                        bugName,
                        cachedRowSet.getString(3),
                        cachedRowSet.getDouble(4),
                        cachedRowSet.getString(5),
                        cachedRowSet.getString(6));
                result.add(bug);
            }
        } catch (SQLException e) {
            log.severe("Could not create bugs from cached result!");
        }
        return result;
    }

    private LibraryFile determineFileForBug(String bugName, Map<String, LibraryFile> fileMap) {
        LibraryFile file = fileMap.get(bugName);
        if (file == null) {
            //log.info("No file found for bug " + bugName + ".");
        }
        return file;
    }

}
