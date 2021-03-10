package de.dbvis.sparta.server.rest.model.graph;

import de.dbvis.sparta.server.rest.model.basic.Module;

import java.util.Set;

public class ModuleGraphNode extends GraphNode {

    /**
     * The unique identifier of the module
     */
    private int moduleId;

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

    public ModuleGraphNode(String id,
                           Set<String> parentNodes,
                           int moduleId,
                           String groupId,
                           String artifactId,
                           String version) {
        super(id, parentNodes);
        this.type = Type.MODULE;
        this.moduleId = moduleId;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public ModuleGraphNode(String id,
                           Set<String> parentNodes,
                           Module module) {
        this(id, parentNodes, module.getId(), module.getGroupId(), module.getArtifactId(), module.getVersion());
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
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
