package de.dbvis.sparta.server.rest.model.basic;

import java.io.Serializable;

/**
 * Stores the number of occurrences of a bug.
 */
public class BugCount implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * The unique id of the bug
     */
    private int bugId;

    /**
     * The name of the bug
     */
    private String bugName;

    /**
     * The number of occurrences
     */
    private int count;

    public BugCount() {

    }

    public BugCount(int id, String name, int count) {
        this.bugId = id;
        this.bugName = name;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return BugCount.class.getName() + "[\n"
                + "  bugId: " + bugId + ",\n"
                + "  bugName: " + bugName + ",\n"
                + "  count: " + count + "\n"
                + "]";
    }
}
