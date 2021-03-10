package de.dbvis.sparta.server.rest.model.data;

import de.dbvis.sparta.server.rest.model.basic.Module;

import java.io.Serializable;

public class ModuleData implements Serializable {

    private static final long serialVersionUID = 11L;

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

    public ModuleData() {

    }

    public ModuleData(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

}
