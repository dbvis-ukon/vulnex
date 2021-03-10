package de.dbvis.sparta.db.importer.repo;

import de.dbvis.sparta.db.importer.JsonResolver;

import java.io.*;
import java.util.logging.Logger;

public class RepoInfoResolver extends JsonResolver {

    private static final Logger log = Logger.getLogger(JsonResolver.class.getName());

    private static final String GITHUB_API = "https://api.github.com/repos/eclipse/";

    private String repo;

    public RepoInfoResolver(String repo) {
        super(GITHUB_API + repo);
        this.repo = repo;
    }

    public RepoInfo resolve() {
        String result = null;
        if (downloadJsonFile()) {
            result = parseJsonFile();
        }
        // Always try to delete the temp file
        new File(TEMP_FILE_NAME).delete();
        return RepoInfo.createFromJsonString(result);
    }

    private String parseJsonFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(JsonResolver.TEMP_FILE_NAME));
            for (String line; (line = br.readLine()) != null; ) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            log.info("Unable to parse repo info data for repo " + this.repo + ".");
        }
        return stringBuilder.toString();
    }

    public static void main(String args[]) {
        System.out.println(new RepoInfoResolver("openj9-omr").resolve());
    }
}
