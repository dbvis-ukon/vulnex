package de.dbvis.sparta.server.rest.model.basic;

import de.dbvis.sparta.server.rest.model.DataItem;
import de.dbvis.sparta.server.rest.model.data.RepositoryData;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * This class stores all information of a repository.
 */
public class Repository extends DataItem implements Serializable {

    private static final long serialVersionUID = 6L;

    /**
     * All parent modules
     */
    private List<Module> parentModules;

    private RepositoryData data;

    /**
     * The set of present bugs referenced by their IDs.
     */
    private Set<Integer> bugIds;

    public Repository() {

    }

    public Repository(int id,
                      List<Module> parentModules,
                      RepositoryData data) {
        super(id);
        this.parentModules = parentModules;
        this.data = data;
    }

    public List<Module> getParentModules() {
        return parentModules;
    }

    public void setParentModules(List<Module> parentModules) {
        this.parentModules = parentModules;
    }

    public RepositoryData getData() {
        return data;
    }

    public void setData(RepositoryData data) {
        this.data = data;
    }

    public Set<Integer> getBugIds() {
        return bugIds;
    }

    public void setBugIds(Set<Integer> bugIds) {
        this.bugIds = bugIds;
    }

    @Override
    public String toString() {
        return Repository.class.getName() + "[\n"
                + "  id: " + getId() + ",\n"
                + "  data.name: " + data.getName() + "\n"
                + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Module)) {
            return false;
        }
        Repository repository = (Repository) other;
        return repository.data.getName().equals(this.data.getName());
    }

}
