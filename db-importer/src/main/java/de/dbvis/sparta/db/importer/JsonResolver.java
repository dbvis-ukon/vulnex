package de.dbvis.sparta.db.importer;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public abstract class JsonResolver {

    public static final String TEMP_FILE_NAME = "tmp.json";

    private static final Logger log = Logger.getLogger(JsonResolver.class.getName());
    private static final int BUFFER_SIZE = 1024;

    private String url;

    protected JsonResolver(String url) {
        this.url = url;
    }

    protected boolean downloadJsonFile() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(TEMP_FILE_NAME)) {
            byte dataBuffer[] = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch(Exception e){
            log.info("Unable to download json file from: " + url);
            return false;
        }
        return true;
    }


}
