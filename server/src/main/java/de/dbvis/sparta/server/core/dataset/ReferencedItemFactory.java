package de.dbvis.sparta.server.core.dataset;

import de.dbvis.sparta.server.rest.model.ItemType;
import de.dbvis.sparta.server.rest.model.basic.*;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.data.BugData;
import de.dbvis.sparta.server.rest.model.data.LibraryFileData;
import de.dbvis.sparta.server.rest.model.data.ModuleData;
import de.dbvis.sparta.server.rest.model.data.RepositoryData;
import de.dbvis.sparta.server.rest.model.intersection.IdReferences;
import de.dbvis.sparta.server.rest.model.intersection.ReferencedItem;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReferencedItemFactory {

    private static final Logger log = Logger.getLogger(ReferencedItemFactory.class.getName());

    private List<Bug> bugs;
    private List<LibraryFile> files;
    private List<Module> modules;
    private List<Repository> repositories;
    private List<Vulnerability> vulnerabilities;

    public ReferencedItemFactory(List<Bug> bugs,
                                 List<LibraryFile> files,
                                 List<Module> modules,
                                 List<Repository> repositories,
                                 List<Vulnerability> vulnerabilities) {
        this.bugs = bugs;
        this.files = files;
        this.modules = modules;
        this.repositories = repositories;
        this.vulnerabilities = vulnerabilities;
    }

    public List<ReferencedItem> createReferencedRepositories() {
       log.info("Building referenced repository data.");
        List<ReferencedItem> result = new ArrayList<ReferencedItem>(repositories.size());
        for (Repository r : repositories) {
            ReferencedItem referencedItem = createReferencedRepositoryItem(r);
            result.add(referencedItem);
        }
        return result;
    }

    private ReferencedItem createReferencedRepositoryItem(Repository r) {
        Set<Bug> repoBugs = determineBugsOfModulesAndSubModules(r.getParentModules());
        IdReferences ir = new IdReferences(
                repoBugs.stream().map(e -> e.getId()).collect(Collectors.toSet()),
                determineFileIdsOfRepository(repoBugs),
                r.getParentModules().stream().map(e -> e.getId()).collect(Collectors.toSet()),
                new HashSet<Integer>());
        return new ReferencedItem(r.getId(), ItemType.REPOSITORY, r.getData(), ir);
    }

    private Set<Integer> determineFileIdsOfRepository(Set<Bug> repoBugs) {
        Set<Integer> fileIds = new HashSet<Integer>();
        for (Bug b : repoBugs) {
            for (LibraryFile f : b.getFiles()) {
                fileIds.add(f.getId());
            }
        }
        return fileIds;
    }

    private Set<Bug> determineBugsOfModulesAndSubModules(List<Module> modules) {
        Set<Bug> result = new HashSet<Bug>();
        List<Module> flatModules = new ModuleListFlattener(modules).flatten();
        for (Module m : flatModules) {
            Set<Bug> b = vulnerabilities.stream()
                    .filter(e -> m.equals(e.getModule()))
                    .map(e -> e.getBug())
                    .collect(Collectors.toSet());
            result.addAll(b);
        }
        return result;
    }

    public List<ReferencedItem> createReferencedBugs() {
        log.info("Building referenced bug data.");
        List<ReferencedItem> result = new ArrayList<ReferencedItem>(bugs.size());
        for (Bug b : bugs) {
            ReferencedItem referencedItem = createReferencedBugItem(b);
            result.add(referencedItem);
        }
        return result;
    }

    private ReferencedItem createReferencedBugItem(Bug b) {
        BugData bd = new BugData(b.getName(),
                b.getDescription(),
                b.getCvssScore(),
                b.getCvssVector(),
                b.getCvssVersion());
        Set<Integer> fileIds = new HashSet<Integer>();
        for (LibraryFile f : b.getFiles()) {
            fileIds.add(f.getId());
        }
        Set<Integer> m = determineAffectedModules(b);
        Set<Integer> r = determineAffectedRepositories(m);
        IdReferences ir = new IdReferences(new HashSet<Integer>(), fileIds, m, r);
        return new ReferencedItem(b.getId(), ItemType.BUG, bd, ir);
    }

    private Set<Integer> determineAffectedModules(Bug b) {
        return vulnerabilities.stream()
                .filter(e -> b.equals(e.getBug()))
                .map(e -> e.getModule().getId())
                .collect(Collectors.toSet());
    }

    private Set<Integer> determineAffectedRepositories(Set<Integer> m) {
        return repositories.stream()
                .filter(e -> {
                    Set<Integer> i = new ModuleListFlattener(e.getParentModules()).flatten().stream()
                            .map(o -> o.getId())
                            .collect(Collectors.toSet());
                    i.retainAll(m);
                    return i.size() > 0;
                }).map(e -> e.getId()).collect(Collectors.toSet());
    }

    public List<ReferencedItem> createReferencedFiles() {
        log.info("Building referenced library data.");
        List<ReferencedItem> result = new ArrayList<ReferencedItem>(files.size());
        for (LibraryFile f : files) {
            ReferencedItem referencedItem = createReferencedFileItem(f);
            result.add(referencedItem);
        }
        return result;
    }

    private ReferencedItem createReferencedFileItem(LibraryFile f) {
        LibraryFileData fd = new LibraryFileData(f.getName(), f.getSha1());
        Set<Integer> m = determineDependentModules(f);
        Set<Integer> r = determineAffectedRepositories(m);
        IdReferences ir = new IdReferences(f.getBugIds(), new HashSet<Integer>(), m, r);
        return new ReferencedItem(f.getId(), ItemType.LIBRARY, fd, ir);
    }

    private Set<Integer> determineDependentModules(LibraryFile f) {
        return vulnerabilities.stream()
                .filter(e -> f.getBugIds().contains(e.getBug().getId()))
                .map(e -> e.getModule().getId())
                .collect(Collectors.toSet());
    }

    public List<ReferencedItem> createReferencedModules() {
        log.info("Building referenced module data.");
        List<Module> flatModuleList = new ModuleListFlattener(modules).flatten();
        List<ReferencedItem> result = new ArrayList<ReferencedItem>(flatModuleList.size());
        for (Module module : flatModuleList) {
            ReferencedItem referencedItem = createReferencedModuleItem(module);
            result.add(referencedItem);
        }
        return result;
    }

    private ReferencedItem createReferencedModuleItem(Module module) {
        ModuleData md = new ModuleData(module.getGroupId(), module.getArtifactId(), module.getVersion());
        List<Bug> bugList = determineAllModuleBugs(module);
        Set<Integer> r = determineAffectedRepository(module.getId());
        Set<Integer> fileIds = new HashSet<Integer>();
        for (Bug b : bugList) {
            for (LibraryFile f : b.getFiles()) {
                fileIds.add(f.getId());
            }
        }
        IdReferences ir = new IdReferences(
                bugList.stream().map(e -> e.getId()).collect(Collectors.toSet()),
                fileIds,
                module.getSubModules().stream().map(e -> e.getId()).collect(Collectors.toSet()),
                r);
        return new ReferencedItem(module.getId(), ItemType.MODULE, md, ir);
    }

    private List<Bug> determineAllModuleBugs(Module m) {
        return vulnerabilities.stream()
                .filter(e -> new ModuleListFlattener(m).flatten().contains(e.getModule()))
                .map(e -> e.getBug())
                .collect(Collectors.toList());
    }

    private Set<Integer> determineAffectedRepository(int m) {
        return repositories.stream()
                .filter(e -> {
                    Set<Integer> i = new ModuleListFlattener(e.getParentModules()).flatten().stream()
                            .map(o -> o.getId())
                            .collect(Collectors.toSet());
                    return i.contains(m);
                }).map(e -> e.getId()).collect(Collectors.toSet());
    }
}
