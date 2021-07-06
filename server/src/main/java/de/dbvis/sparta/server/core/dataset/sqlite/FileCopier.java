package de.dbvis.sparta.server.core.dataset.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class FileCopier {

    private static final Logger log = Logger.getLogger(FileCopier.class.getName());

    private static final String DATABASE_FILE = "sparta_database.db";

    private static final int BUFFER_SIZE = 4096;

    private File targetFile;
    private URL sourceUrl;
    private FileOutputStream outputStream;
    private InputStream inputStream;

    public void copySingleFile(String file) {
		log.info("Preparing " + file + " file.");
        targetFile = new File("./" + file);
        if (!targetFile.exists()) {
            copyExistingFile(file);
        } else {
            log.info("The " + file + " file already exists.");
        }
    }

    private void copyExistingFile(String file) {
        sourceUrl = getClass().getClassLoader().getResource(file);
        try {
            openStreams();
            copyFileContent();
            closeStreams();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Extracted the " + file + " file.");
    }

    private void openStreams() throws IOException {
        outputStream = new FileOutputStream(targetFile);
        inputStream = sourceUrl.openStream();
    }

    private void copyFileContent() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = inputStream.read(buffer);
        while (bytesRead != -1) {
            outputStream.write(buffer, 0, bytesRead);
            bytesRead = inputStream.read(buffer);
        }
    }

    private void closeStreams() throws IOException {
        outputStream.close();
        inputStream.close();
    }

    public static void copyFiles() {
        new FileCopier().copySingleFile(DATABASE_FILE);
    }

}
