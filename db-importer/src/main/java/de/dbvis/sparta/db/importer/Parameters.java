package de.dbvis.sparta.db.importer;

import com.beust.jcommander.Parameter;

public class Parameters {

    @Parameter(names = {"--db-file", "-d"}, description = "Path to the database file")
    public String dbFile;

    @Parameter(names = {"--initialize", "-i"}, description = "Initialize the database")
    public boolean initialize;

    @Parameter(names = {"--add-metainfo", "-m"}, description = "Add metainfo to database")
    public boolean addMetainfo;

}
