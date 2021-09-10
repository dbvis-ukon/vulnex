package de.dbvis.sparta.server.core.dataset.steady;

import de.dbvis.sparta.server.rest.model.basic.Bug;

public class ApiConfig {
    public final String host;
    public final String port;

    public ApiConfig(final String host,
                     final String port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return Bug.class.getName() + "[\n"
                + "  host: " + host + ",\n"
                + "  port: " + port + "\n"
                + "]";
    }

}
