package de.dbvis.sparta.server.core.dataset;

import de.dbvis.sparta.server.rest.model.basic.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleListFlattener {

    private List<Module> modules;
    private List<Module> flatModuleList;

    public ModuleListFlattener(final List<Module> modules) {
        this.modules = modules;
        this.flatModuleList = new ArrayList<Module>();
    }

    public ModuleListFlattener(final Module module) {
        this(createListWithOneModule(module));
    }

    public List<Module> flatten() {
        recursivelyAddAllModulesToFlatList(modules);
        return flatModuleList;
    }

    private void recursivelyAddAllModulesToFlatList(List<Module> modules) {
        for (Module m : modules) {
            flatModuleList.add(m);
            recursivelyAddAllModulesToFlatList(m.getSubModules());
        }
    }

    private static List<Module> createListWithOneModule(Module module) {
        List<Module> result = new ArrayList<Module>();
        result.add(module);
        return result;
    }

}
