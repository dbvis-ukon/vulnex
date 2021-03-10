package de.dbvis.sparta.server.rest.model.data;

import java.io.Serializable;

public class BugData implements Serializable {

    private static final long serialVersionUID = 8L;

    /**
     * The name of the bug, e.g., CVE-2020-9399
     */
    private String name;

    /**
     * The bug description
     */
    private String description;

    /**
     * The CVSS score
     */
    private double cvssScore;

    /**
     * The CVSS vector
     */
    private String cvssVector;

    /**
     * The CVSS version, e.g., 3.0
     */
    private String cvssVersion;

    public BugData() {

    }

    public BugData(String name,
                   String description,
                   double cvssScore,
                   String cvssVector,
                   String cvssVersion) {
        this.name = name;
        this.description = description;
        this.cvssScore = cvssScore;
        this.cvssVector = cvssVector;
        this.cvssVersion = cvssVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCvssScore() {
        return cvssScore;
    }

    public void setCvssScore(double cvssScore) {
        this.cvssScore = cvssScore;
    }

    public String getCvssVector() {
        return cvssVector;
    }

    public void setCvssVector(String cvssVector) {
        this.cvssVector = cvssVector;
    }

    public String getCvssVersion() {
        return cvssVersion;
    }

    public void setCvssVersion(String cvssVersion) {
        this.cvssVersion = cvssVersion;
    }

    @Override
    public String toString() {
        return BugData.class.getName() + "[\n"
                + "  name: " + name + ",\n"
                + "  description: " + description + ",\n"
                + "  cvssScore: " + cvssScore + ",\n"
                + "  cvssVector: " + cvssVector + ",\n"
                + "  cvssVersion: " + cvssVersion + "\n"
                + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BugData)) {
            return false;
        }
        BugData bd = (BugData) other;
        return bd.name.equals(this.name);
    }

}
