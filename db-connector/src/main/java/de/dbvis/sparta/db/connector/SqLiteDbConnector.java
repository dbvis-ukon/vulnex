package de.dbvis.sparta.db.connector;

public class SqLiteDbConnector extends DbConnector {

    private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_DB = "jdbc:sqlite:";

    private static String MAIN_DB_FILE_SUFFIX = "sparta_database.db";

    private SqLiteDbConnector() {
        super(SQLITE_DRIVER, SQLITE_DB + "./" + MAIN_DB_FILE_SUFFIX);
    }

    private SqLiteDbConnector(String dir) {
        super(SQLITE_DRIVER, SQLITE_DB + dir + "/" + MAIN_DB_FILE_SUFFIX);
    }

    public static SqLiteDbConnector createSqLiteDbConnector() {
        return new SqLiteDbConnector();
    }

    public static SqLiteDbConnector createSqLiteDbConnector(String dir) {
        return new SqLiteDbConnector(dir);
    }

}
