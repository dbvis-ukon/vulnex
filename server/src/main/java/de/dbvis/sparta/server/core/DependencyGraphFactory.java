package de.dbvis.sparta.server.core;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.graph.*;
import de.dbvis.sparta.server.rest.model.basic.Bug;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.basic.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyGraphFactory {

    private List<GraphNode> nodes;

    public DependencyGraphFactory() {
        this.nodes = new ArrayList<GraphNode>();
    }

    public DependencyGraph createDependencyGraphForModule(Module module) {
        ModuleGraphNode moduleNode = new ModuleGraphNode("M" + module.getId(), new HashSet<String>(), module);
        nodes.add(moduleNode);
        recursivelyAddParentModules(module.getSubModules(), moduleNode);
        return new DependencyGraph(nodes);
    }

    public DependencyGraph createDependencyGraphForRepository(Repository repository) {
        RepositoryGraphNode repoNode = new RepositoryGraphNode("R" + repository.getId(), new HashSet<String>(), repository);
        nodes.add(repoNode);
        recursivelyAddParentModules(repository.getParentModules(), repoNode);
        return new DependencyGraph(nodes);
    }

    private void recursivelyAddParentModules(List<Module> parentModules, GraphNode childNode) {
        for (Module m : parentModules) {
            ModuleGraphNode moduleNode = new ModuleGraphNode("M" + m.getId(), new HashSet<String>(), m);
            if (!nodes.contains(moduleNode)) {
                addBugsOfModuleToNodeList(moduleNode, m);
                nodes.add(moduleNode);
            }
            childNode.getParentIds().add(moduleNode.getId());
            recursivelyAddParentModules(m.getSubModules(), moduleNode);
        }
    }

    private void addBugsOfModuleToNodeList(ModuleGraphNode moduleNode, Module module) {
        for (Bug b : determineBugsOfModule(module)) {
            final String bugNodeId = "B" + b.getId();
            BugGraphNode bugNode = new BugGraphNode(bugNodeId, b);
            LibraryFile library = b.getFile();
            final String libraryNodeId = "L" + library.getId();
            LibraryGraphNode libraryGraphNode = new LibraryGraphNode(libraryNodeId, new HashSet<String>(), library);
            if (!nodes.contains(libraryGraphNode)) {
                libraryGraphNode.getParentIds().add(bugNodeId);
                nodes.add(libraryGraphNode);
            } else {
                GraphNode existingNode = nodes.stream().filter(e -> e.equals(libraryGraphNode)).findFirst().get();
                existingNode.getParentIds().add(bugNodeId);
            }
            if (!nodes.contains(bugNode)) {
                nodes.add(bugNode);
            }
            moduleNode.getParentIds().add(libraryNodeId);
        }
    }

    private List<Bug> determineBugsOfModule(final Module module) {
        return Constants.DATASET.getVulnerabilities().stream()
                .filter(v -> v.getModule() == module)
                .map(v -> v.getBug())
                .collect(Collectors.toList());
    }

}