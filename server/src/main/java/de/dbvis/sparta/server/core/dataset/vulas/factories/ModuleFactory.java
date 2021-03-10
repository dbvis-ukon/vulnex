package de.dbvis.sparta.server.core.dataset.vulas.factories;

import de.dbvis.sparta.server.rest.model.basic.Module;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ModuleFactory {

    private static final Logger log = Logger.getLogger(ModuleFactory.class.getName());

    public List<Module> createPlainModulesFromCachedResult(CachedRowSet cachedRowSet) {
        List<Module> result = new ArrayList(cachedRowSet.size());
        try {
            while (cachedRowSet.next()) {
                result.add(createSingleModuleCachedResult(cachedRowSet));
            }
        } catch (SQLException e) {
            log.severe("Could not create plain modules from cached result!");
        }
        return result;
    }

    private Module createSingleModuleCachedResult(CachedRowSet cachedRowSet) {
        try {
            return new Module(
                    cachedRowSet.getInt(1),
                    new ArrayList<Module>(),
                    cachedRowSet.getString(2),
                    cachedRowSet.getString(3),
                    cachedRowSet.getString(4)
            );
        } catch (SQLException e) {
            log.severe("Could not create single module from cached result!");
        }
        return null;
    }

}
