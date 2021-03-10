package de.dbvis.sparta.server.core.dataset.vulas;

import de.dbvis.sparta.server.rest.model.basic.Bug;

public class DatabaseConfig {

    public final String host;
    public final String port;
    public final String database;
    public final String username;
    public final String password;

    public DatabaseConfig(final String host,
                          final String port,
                          final String database,
                          final String username,
                          final String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return Bug.class.getName() + "[\n"
                + "  host: " + host + ",\n"
                + "  port: " + port + ",\n"
                + "  database: " + database + ",\n"
                + "  username: " + username + ",\n"
                + "  password: " + password + "\n"
                + "]";
    }

}
