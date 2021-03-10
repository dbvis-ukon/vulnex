package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.rest.model.basic.Module;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class PlainModuleFactory {

    private static final Logger log = Logger.getLogger(PlainModuleFactory.class.getName());

    private List<Module> rootModules;
    private List<ModuleParentIdPair> subModules;

    public PlainModuleFactory() {
        this.rootModules = new ArrayList<Module>();
        this.subModules = new ArrayList<ModuleParentIdPair>();
    }

    public List<Module> createPlainModulesFromCachedResult(CachedRowSet cachedRowSet) {
        List<ModuleParentIdPair> pairs = createModuleParentIdPairs(cachedRowSet);
        for (ModuleParentIdPair m : pairs) {
            if (m.getParentId() == -1) {
                rootModules.add(m.getModule());
            } else {
                subModules.add(m);
            }
        }
        while (!subModules.isEmpty()) {
            ModuleParentIdPair s = subModules.remove(0);
            if (!recursivelyAddSubmodules(rootModules, s)) {
                log.severe("Could not assign submodule " + s.getModule().getId() + " to its parent!");
            }
        }
        return rootModules;
    }

    private boolean recursivelyAddSubmodules(List<Module> list, ModuleParentIdPair subModule) {
        for (Module m : list) {
            if (m.getId() == subModule.getParentId()) {
                m.getSubModules().add(subModule.getModule());
                return true;
            }
        }
        for (Module m : list) {
            if (recursivelyAddSubmodules(m.getSubModules(), subModule)) {
                return true;
            }
        }
        return false;
    }

    private List<ModuleParentIdPair> createModuleParentIdPairs(CachedRowSet cachedRowSet) {
        List<ModuleParentIdPair> result = new ArrayList<ModuleParentIdPair>();
        try {
            while (cachedRowSet.next()) {
                result.add(createSingleModuleParentIdPairFromCachedResult(cachedRowSet));
            }
        } catch (SQLException e) {
            log.severe("Could not create module parent id pairs from cached result!");
        }
        return result;
    }

    private ModuleParentIdPair createSingleModuleParentIdPairFromCachedResult(CachedRowSet cachedRowSet) {
        ModuleParentIdPair result = null;
        try {
            Module module = new Module(
                    cachedRowSet.getInt(1),
                    new ArrayList<Module>(),
                    cachedRowSet.getString(3),
                    cachedRowSet.getString(4),
                    cachedRowSet.getString(5)
            );
            result = new ModuleParentIdPair(module, cachedRowSet.getInt(2));
        } catch (SQLException e) {
            log.severe("Could not create single module parent id pair from cached result!");
        }
        return result;
    }

    private class ModuleParentIdPair {

        private Module module;
        private int parentId;

        private ModuleParentIdPair(Module module, int parentId) {
            this.module = module;
            this.parentId = parentId;
        }

        public Module getModule() {
            return module;
        }

        public int getParentId() {
            return parentId;
        }

    }

}
