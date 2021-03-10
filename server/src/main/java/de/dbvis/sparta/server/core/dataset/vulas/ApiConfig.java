package de.dbvis.sparta.server.core.dataset.vulas;

import de.dbvis.sparta.server.rest.model.basic.Bug;

public class ApiConfig {
    public final String host;
    public final String port;
    public final String spaceToken;

    public ApiConfig(final String host,
                          final String port,
                          final String spaceToken) {
        this.host = host;
        this.port = port;
        this.spaceToken = spaceToken;
    }

    @Override
    public String toString() {
        return Bug.class.getName() + "[\n"
                + "  host: " + host + ",\n"
                + "  port: " + port + ",\n"
                + "  spaceToken: " + spaceToken + "\n"
                + "]";
    }

}
