package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.rest.model.basic.Module;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepositoryInfoFactory {

    private List<Module> modules;

    public RepositoryInfoFactory() {
        this.modules = SqliteDataset.getInstance().getPlainModules();
    }

    public Set<Integer> createBugIdSet(final List<Module> parentModules) {
        Set<Integer> bugIds = new HashSet<Integer>();
        for (Module m : parentModules) {
            bugIds.addAll(m.getBugIds());
        }
        return bugIds;
    }
}
