package de.dbvis.sparta.server.rest.model.data;

import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import java.io.Serializable;

public class LibraryFileData implements Serializable {

    private static final long serialVersionUID = 9L;

    /**
     * The name of the file
     */
    private String name;

    /**
     * The SHA1 of the file
     */
    private String sha1;

    public LibraryFileData() {

    }

    public LibraryFileData(String name, String sha1) {
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

}
