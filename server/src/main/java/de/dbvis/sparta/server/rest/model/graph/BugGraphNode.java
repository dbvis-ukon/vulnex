package de.dbvis.sparta.server.rest.model.graph;

import de.dbvis.sparta.server.rest.model.basic.Bug;

import java.util.HashSet;

public class BugGraphNode extends GraphNode {

    /**
     * The unique identifier of the bug
     */
    private int bugId;

    /**
     * The name of the bug, e.g., CVE-2020-9399
     */
    private String name;

    /**
     * The CVSS score
     */
    private double cvssScore;

    /**
     * The CVSS version, e.g., 3.0
     */
    private String cvssVersion;

    public BugGraphNode(String id,
                        int bugId,
                        String name,
                        double cvssScore,
                        String cvssVersion) {
        super(id, new HashSet<String>());
        this.type = Type.BUG;
        this.bugId = bugId;
        this.name = name;
        this.cvssScore = cvssScore;
        this.cvssVersion = cvssVersion;
    }

    public BugGraphNode(String id,
                        Bug bug) {
        this(id, bug.getId(), bug.getName(), bug.getCvssScore(), bug.getCvssVersion());
    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCvssScore() {
        return cvssScore;
    }

    public void setCvssScore(double cvssScore) {
        this.cvssScore = cvssScore;
    }

    public String getCvssVersion() {
        return cvssVersion;
    }

    public void setCvssVersion(String cvssVersion) {
        this.cvssVersion = cvssVersion;
    }

}
