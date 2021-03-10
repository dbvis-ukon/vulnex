package de.dbvis.sparta.server.rest.model.graph;

import java.util.List;

/**
 * This class defines a dependency graph. The graph is a directed and acyclic
 * graph (DAG). It contains a list of nodes describing the graph.
 */
public class DependencyGraph {

    /**
     * The list of nodes
     */
    private List<GraphNode> nodes;

    public DependencyGraph(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

}
