package de.dbvis.sparta.server.rest.model.graph;

import de.dbvis.sparta.server.rest.model.basic.Repository;

import java.util.Set;

public class RepositoryGraphNode extends GraphNode {

    /**
     * The unique identifier of the repository
     */
    private int repoId;

    /**
     * The name of the repository
     */
    private String name;

    public RepositoryGraphNode(String id,
                               Set<String> parentIds,
                               int repoId,
                               String name) {
        super(id, parentIds);
        this.type = Type.REPOSITORY;
        this.repoId = repoId;
        this.name = name;
    }

    public RepositoryGraphNode(String id,
                               Set<String> patentIds,
                               Repository repository) {
        this(id, patentIds, repository.getId(), repository.getData().getName());
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
