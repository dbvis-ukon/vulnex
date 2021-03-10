package de.dbvis.sparta.server.rest.model.intersection;

import java.io.Serializable;
import java.util.Set;

public class IdReferences implements Serializable {

    private static final long serialVersionUID = 13L;

    private Set<Integer> bugIds;
    private Set<Integer> libraryIds;
    private Set<Integer> moduleIds;
    private Set<Integer> repositoryIds;

    public IdReferences() {

    }

    public IdReferences(Set<Integer> bugIds,
                        Set<Integer> libraryIds,
                        Set<Integer> moduleIds,
                        Set<Integer> repositoryIds) {
        this.bugIds = bugIds;
        this.libraryIds = libraryIds;
        this.moduleIds = moduleIds;
        this.repositoryIds = repositoryIds;
    }

    public Set<Integer> getBugIds() {
        return bugIds;
    }

    public void setBugIds(Set<Integer> bugIds) {
        this.bugIds = bugIds;
    }

    public Set<Integer> getLibraryIds() {
        return libraryIds;
    }

    public void setLibraryIds(Set<Integer> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public Set<Integer> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(Set<Integer> moduleIds) {
        this.moduleIds = moduleIds;
    }

    public Set<Integer> getRepositoryIds() {
        return repositoryIds;
    }

    public void setRepositoryIds(Set<Integer> repositoryIds) {
        this.repositoryIds = repositoryIds;
    }

}
