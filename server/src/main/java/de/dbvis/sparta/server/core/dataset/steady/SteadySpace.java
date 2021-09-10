package de.dbvis.sparta.server.core.dataset.steady;

public class SteadySpace {

    private final String spaceToken;
    private final String spaceName;

    public SteadySpace(String spaceToken, String spaceName) {
        this.spaceToken = spaceToken;
        this.spaceName = spaceName;
    }

    public String getSpaceToken() {
        return spaceToken;
    }

    public String getSpaceName() {
        return spaceName;
    }

}
