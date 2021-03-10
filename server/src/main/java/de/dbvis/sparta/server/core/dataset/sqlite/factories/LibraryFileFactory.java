package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LibraryFileFactory {

    private static final Logger log = Logger.getLogger(LibraryFileFactory.class.getName());

    public List<LibraryFile> createFilesFromCachedResult(CachedRowSet cachedRowSet) {
        List<LibraryFile> result = new ArrayList<LibraryFile>(cachedRowSet.size());
        try {
            while (cachedRowSet.next()) {
                LibraryFile file = new LibraryFile(
                        cachedRowSet.getInt(1),
                        cachedRowSet.getString(2),
                        cachedRowSet.getString(3));
                result.add(file);
            }
        } catch (SQLException e) {
            log.severe("Could not create files from cached result!");
        }
        return result;
    }

}
