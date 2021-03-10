package de.dbvis.sparta.server.rest.model.graph;

import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import java.util.Set;

public class LibraryGraphNode extends GraphNode {

    /**
     * The unique identifier of the library
     */
    private int libraryId;

    /**
     * The name of the file
     */
    private String name;

    /**
     * The SHA1 of the file
     */
    private String sha1;

    public LibraryGraphNode(String id,
                            Set<String> parentIds,
                            int libraryId,
                            String name,
                            String sha1) {
        super(id, parentIds);
        this.type = Type.LIBRARY;
        this.libraryId = libraryId;
        this.name = name;
        this.sha1 = sha1;
    }

    public LibraryGraphNode(String id,
                            Set<String> parentIds,
                            LibraryFile libraryFile) {
        super(id, parentIds);
        this.type = Type.LIBRARY;
        this.libraryId = libraryFile.getId();
        this.name = libraryFile.getName();
        this.sha1 = libraryFile.getSha1();
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
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
