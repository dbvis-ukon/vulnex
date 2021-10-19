package de.dbvis.sparta.server.rest.model.basic;

import de.dbvis.sparta.server.rest.model.DataItem;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * This class stores the data of a bug.
 */
public class Bug extends DataItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The library affected by the library
     */
    private Set<LibraryFile> files;

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

    public Bug() {

    }

    public Bug(int id,
               Set<LibraryFile> files,
               String name,
               String description,
               double cvssScore,
               String cvssVector,
               String cvssVersion) {
        super(id);
        this.files = files;
        this.name = name;
        this.description = description;
        this.cvssScore = cvssScore;
        this.cvssVector = cvssVector;
        this.cvssVersion = cvssVersion;
    }

    public Set<LibraryFile> getFiles() {
        return files;
    }

    public void setFile(Set<LibraryFile> file) {
        this.files = files;
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
        return Bug.class.getName() + "[\n"
                + "  id: " + getId() + ",\n"
                + "  files: " + files + ",\n"
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
        if (!(other instanceof Bug)) {
            return false;
        }
        Bug bug = (Bug) other;
        return bug.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
