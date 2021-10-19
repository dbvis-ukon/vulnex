package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.rest.model.basic.Bug;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class BugFactory {

    private static final Logger log = Logger.getLogger(BugFactory.class.getName());

    private List<LibraryFile> files;

    public BugFactory() {
        this.files = SqliteDataset.getInstance().getFiles();
    }

    public List<Bug> createBugsFromCachedResult(CachedRowSet cachedRowSet) {
        List<Bug> result = new ArrayList<Bug>(cachedRowSet.size());
        try {
            while (cachedRowSet.next()) {
                final int fileId = cachedRowSet.getInt(2);
                final Optional<LibraryFile> optional = files.stream().filter(e -> e.getId() == fileId).findAny();
                if (optional.isPresent()) {
                    Bug bug = new Bug(
                            cachedRowSet.getInt(1),
                            createListWithOneFile(optional.get()),
                            cachedRowSet.getString(3),
                            "",
                            cachedRowSet.getDouble(4),
                            "",
                            String.valueOf(cachedRowSet.getDouble(5)));
                    result.add(bug);
                } else {
                    log.severe("Could not find file with id " + fileId + ".");
                }
            }
        } catch (SQLException e) {
            log.severe("Could not create bugs from cached result!");
        }
        return result;
    }

    private Set<LibraryFile> createListWithOneFile(LibraryFile file) {
        if (file == null) {
            return new HashSet<LibraryFile>(0);
        }
        Set<LibraryFile> result = new HashSet<LibraryFile>(1);
        result.add(file);
        return result;
    }

}
