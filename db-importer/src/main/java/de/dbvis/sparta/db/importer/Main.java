package de.dbvis.sparta.db.importer;

import com.beust.jcommander.JCommander;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        SteadyImporter steadyImporter = new SteadyImporter();
        steadyImporter.runSteadyImporter();

        /*

        Parameters params = new Parameters();
        JCommander.newBuilder().addObject(params).build().parse(args);

        log.info("- Parameters -\nDB File: " + params.dbFile + "\nInitialize: " + params.initialize + "\nAdd metainfo: " + params.addMetainfo);

        File repoDir = new File(params.dbFile);
        if (!repoDir.isDirectory()) {
            log.severe("No directory path specified!");
            System.exit(-1);
        }

        if (params.initialize) {
            log.info("Loading data from " + repoDir.getAbsolutePath() + ".");
            new SqLiteImporter(repoDir).createAndFillMainTables();
        } else {
            log.info("Adding metainfo from to database in " + repoDir.getAbsolutePath() + ".");
            new SqLiteImporter(repoDir).createAndFillMetaInfoTable();
        }

        */
    }

    public static int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

}
