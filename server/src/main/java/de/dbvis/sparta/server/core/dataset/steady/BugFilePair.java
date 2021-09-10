package de.dbvis.sparta.server.core.dataset.steady;

public class BugFilePair {

    private String bugName;
    private int libraryId;

    public BugFilePair(String bugName, int libraryId) {
        this.bugName = bugName;
        this.libraryId = libraryId;
    }

    public String getBugName() {
        return bugName;
    }

    public int getLibraryId() {
        return libraryId;
    }

    @Override
    public String toString() {
        return BugFilePair.class.getName() + "[\n"
                + "  bugName: " + bugName + ",\n"
                + "  libraryId: " + libraryId + "\n"
                + "]";
    }

}
