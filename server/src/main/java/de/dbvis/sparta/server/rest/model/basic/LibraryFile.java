package de.dbvis.sparta.server.rest.model.basic;

import de.dbvis.sparta.server.rest.model.DataItem;

import java.io.Serializable;
import java.util.Set;

/**
 * This class stores the data of a library dependency, usually describing a jar-file.
 */
public class LibraryFile extends DataItem implements Serializable {

    private static final long serialVersionUID = 4L;

    /**
     * The name of the file
     */
    private String name;

    /**
     * The SHA1 of the file
     */
    private String sha1;

    /**
     * The set present bugs referenced by their IDs.
     */
    private Set<Integer> bugIds;

    public LibraryFile() {

    }

    public LibraryFile(int id,
                       String name,
                       String sha1) {
        super(id);
        this.name = name;
        this.sha1 = sha1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public Set<Integer> getBugIds() {
        return bugIds;
    }

    public void setBugIds(Set<Integer> bugIds) {
        this.bugIds = bugIds;
    }

    @Override
    public String toString() {
        return LibraryFile.class.getName() + "[\n"
                + "  id: " + getId() + ",\n"
                + "  name: " + name + ",\n"
                + "  sha1: " + sha1 + ",\n"
                + "  bugIds: " + bugIds + "\n"
                + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LibraryFile)) {
            return false;
        }
        LibraryFile file = (LibraryFile) other;
        return this.name.equals(file.name);
    }

}
