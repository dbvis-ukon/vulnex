package de.dbvis.sparta.server.rest.model.basic;

import java.io.Serializable;

/**
 * Stores the severity of a bug.
 */
public class BugSeverity implements Serializable {

    private static final long serialVersionUID = 3L;

    /**
     * The unique id of the bug
     */
    private int bugId;

    /**
     * The name of the bug
     */
    private String bugName;

    /**
     * The bug severity
     */
    private double severity;

    public BugSeverity() {

    }

    public BugSeverity(int id, String name, double severity) {
        this.bugId = id;
        this.bugName = name;
        this.severity = severity;
    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int id) {
        this.bugId = id;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public double getSeverity() {
        return severity;
    }

    public void setSeverity(double severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return BugSeverity.class.getName() + "[\n"
                + "  bugId: " + bugId + ",\n"
                + "  bugName: " + bugName + ",\n"
                + "  severity: " + severity + "\n"
                + "]";
    }

}
