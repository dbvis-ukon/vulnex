package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.core.dataset.ModuleListFlattener;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.basic.Vulnerability;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleInfoFactory {

    private List<Module> flatPlainModuleList;
    private List<Vulnerability> vulnerabilities;

    public ModuleInfoFactory() {
        this.flatPlainModuleList = new ModuleListFlattener(SqliteDataset.getInstance().getPlainModules()).flatten();
        this.vulnerabilities = SqliteDataset.getInstance().getVulnerabilities();
    }

    public List<Set<Integer>> createBugIdSetsForAllModules() {
        List<Set<Integer>> result = new ArrayList<Set<Integer>>(flatPlainModuleList.size());
        for (Module m : flatPlainModuleList) {
            List<Vulnerability> moduleVulnerabilities = vulnerabilities.stream()
                    .filter(e -> e.getModule() == m)
                    .collect(Collectors.toList());
            Set<Integer> bugIds = new HashSet<Integer>();
            for (Vulnerability v : moduleVulnerabilities) {
                bugIds.add(v.getBug().getId());
            }
            result.add(bugIds);
        }
        return result;
    }

}
