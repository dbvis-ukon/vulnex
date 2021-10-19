package de.dbvis.sparta.server.core.dataset.steady;

public class SteadySpace {

    private final String spaceToken;
    private final String spaceName;
    private final String exportConfiguration;

    public SteadySpace(String spaceToken, String spaceName, String exportConfiguration) {
        this.spaceToken = spaceToken;
        this.spaceName = spaceName;
        this.exportConfiguration = exportConfiguration;
    }

    public String getSpaceToken() {
        return spaceToken;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getExportConfiguration() {
        return exportConfiguration;
    }

    public boolean isEnabled() {
        return this.spaceName != null && !this.exportConfiguration.equals("OFF");
    }

    @Override
    public String toString() {
        return SteadySpace.class.getName() + "[\n"
                + "  spaceToken: " + spaceToken + ",\n"
                + "  spaceName: " + spaceName + ",\n"
                + "  exportConfiguration: " + exportConfiguration + "\n"
                + "]";
    }

}
