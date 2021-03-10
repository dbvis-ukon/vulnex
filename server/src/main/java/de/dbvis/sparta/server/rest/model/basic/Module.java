package de.dbvis.sparta.server.rest.model.basic;

import de.dbvis.sparta.server.rest.model.DataItem;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * This class stores the data of a module.
 */
public class Module extends DataItem implements Serializable {

    private static final long serialVersionUID = 5L;

    /**
     * A list of submodules
     */
    private List<Module> subModules;

    /**
     * The Maven group ID
     */
    private String groupId;

    /**
     * The Maven artifact ID
     */
    private String artifactId;

    /**
     * The Maven version
     */
    private String version;

    /**
     * The set present bugs referenced by their IDs.
     */
    private Set<Integer> bugIds;

    public Module() {

    }

    public Module(int id,
                  List<Module> subModules,
                  String groupId,
                  String artifactId,
                  String version) {
        super(id);
        this.subModules = subModules;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public List<Module> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<Module> subModules) {
        this.subModules = subModules;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Integer> getBugIds() {
        return bugIds;
    }

    public void setBugIds(Set<Integer> bugIds) {
        this.bugIds = bugIds;
    }

    @Override
    public String toString() {
        return Bug.class.getName() + "[\n"
                + "  id: " + getId() + ",\n"
                + "  subModules: " + (subModules == null ? null : subModules.size()) + ",\n"
                + "  groupId: " + groupId + ",\n"
                + "  artifactId: " + artifactId + ",\n"
                + "  version: " + version + "\n"
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
        Module module = (Module) other;
        return this.getId() == module.getId();
    }

}
