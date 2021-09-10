package de.dbvis.sparta.db.importer;

public class MavenIdentifier {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public MavenIdentifier(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return MavenIdentifier.class.getName() + "[\n"
                + "  groupId: " + groupId + ",\n"
                + "  artifactId: " + artifactId + ",\n"
                + "  version: " + version + "\n"
                + "]";
    }

}
