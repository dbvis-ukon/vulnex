package de.dbvis.sparta.server.rest.model.graph;

import java.util.Set;

/**
 * This is the base class representing a node of the dependency graph. The graph is a directed
 * and acyclic graph (DAG). A node stores references to parents via an ID.
 */
public abstract class GraphNode {

    public enum Type {
        BUG,
        LIBRARY,
        MODULE,
        REPOSITORY
    }

    /**
     * The unique identifier of the node
     */
    private String id;

    /**
     * The IDS of the parents
     */
    private Set<String> parentIds;

    /**
     * The node type, i.e., library file, module or repository
     */
    protected Type type;

    public GraphNode(String id, Set<String> parentIds) {
        this.id = id;
        this.parentIds = parentIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(Set<String> parentIds) {
        this.parentIds = parentIds;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GraphNode)) {
            return false;
        }
        GraphNode dependencyGraph = (GraphNode) other;
        return this.id.equals(dependencyGraph.id) && this.type == dependencyGraph.type;
    }

}
