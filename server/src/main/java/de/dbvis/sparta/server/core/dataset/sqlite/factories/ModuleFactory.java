package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.core.dataset.ModuleListFlattener;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.rest.model.basic.Module;

import java.util.List;
import java.util.Set;

public class ModuleFactory {

    private List<Module> plainModules;
    private List<Set<Integer>> allBugIdSets;

    public ModuleFactory() {
        this.plainModules = SqliteDataset.getInstance().getPlainModules();
        this.allBugIdSets = SqliteDataset.getInstance().getBugIdSets();
    }

    public List<Module> createModules() {
        List<Module> flatModuleList = new ModuleListFlattener(plainModules).flatten();
        for (int i = 0; i < flatModuleList.size(); i++) {
            flatModuleList.get(i).setBugIds(allBugIdSets.get(i));
        }
        return plainModules;
    }

}
