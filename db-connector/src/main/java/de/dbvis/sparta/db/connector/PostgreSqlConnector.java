package de.dbvis.sparta.db.connector;

        import javax.sql.rowset.CachedRowSet;

public class PostgreSqlConnector extends DbConnector {

    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final String POSTGRESQL_DB = "jdbc:postgresql:";

    private PostgreSqlConnector(final String host,
                                final String port,
                                final String database,
                                final String username,
                                final String password,
                                final String options) {
        super(POSTGRESQL_DRIVER, POSTGRESQL_DB + "//" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password + options);
    }

    public static PostgreSqlConnector createPostgreSqlConnector(final String host,
                                                                final String port,
                                                                final String database,
                                                                final String username,
                                                                final String password) {
        return new PostgreSqlConnector(host, port, database, username, password, "");
    }

    public static PostgreSqlConnector createPostgreSqlConnectorWithNonValidatingSsl(final String host,
                                                                                    final String port,
                                                                                    final String database,
                                                                                    final String username,
                                                                                    final String password) {
        return new PostgreSqlConnector(host, port, database, username, password,
                "&ssl=true;sslfactory=org.postgresql.ssl.NonValidatingFactory");
    }

    public static void main(String[] args) {
        try {
            PostgreSqlConnector connector = PostgreSqlConnector.createPostgreSqlConnectorWithNonValidatingSsl(
                    "localhost",
                    "5432",
                    "vulas",
                    "postgres",
                    "postgres");
            CachedRowSet cachedRowSet = connector.executeQueryWithCachedResult("SELECT * FROM app LIMIT 1;");
            if (cachedRowSet.next()) {
                System.out.println(cachedRowSet.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

