package de.dbvis.sparta.server.core.dataset.vulas.factories;

import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LibraryFileFactory {

    private static final Logger log = Logger.getLogger(LibraryFileFactory.class.getName());

    public Map<String, LibraryFile> createFilesFromCachedResult(CachedRowSet cachedRowSet) {
        Map<String, LibraryFile> result = new HashMap<String, LibraryFile>(cachedRowSet.size());
        try {
            while (cachedRowSet.next()) {
                LibraryFile file = new LibraryFile(
                        // id
                        cachedRowSet.getInt(1),
                        // filename
                        cachedRowSet.getString(3),
                        // digest
                        cachedRowSet.getString(4));
                result.put(cachedRowSet.getString(2), file);
            }
        } catch (SQLException e) {
            log.severe("Could not create files from cached result!");
        }
        return result;
    }

}
