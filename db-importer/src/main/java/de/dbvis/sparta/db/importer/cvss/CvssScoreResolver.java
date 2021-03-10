package de.dbvis.sparta.db.importer.cvss;

import de.dbvis.sparta.db.importer.JsonResolver;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.logging.Logger;

public class CvssScoreResolver extends JsonResolver {

    private static final Logger log = Logger.getLogger(CvssScoreResolver.class.getName());

    private static final String READHAT_API = "https://access.redhat.com/labs/securitydataapi/cve/";

    private String bugName;

    public CvssScoreResolver(String bugName) {
        super(READHAT_API + bugName + ".json");
        this.bugName = bugName;
    }

    public CvssInfo resolve() {
        CvssInfo result = new CvssInfo(-1, -1);
        if (downloadJsonFile()) {
            result = parseJsonFile();
        }
        // Always try to delete the temp file
        new File(TEMP_FILE_NAME).delete();
        return result;
    }

    private CvssInfo parseJsonFile() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(JsonResolver.TEMP_FILE_NAME)) {
            double score = -1.0d;
            JSONObject rootObject = (JSONObject) parser.parse(reader);
            JSONObject cvss3 = (JSONObject) rootObject.get("cvss3");
            if (cvss3 != null) {
                score = Double.parseDouble((String) cvss3.get("cvss3_base_score"));
            } else {
                JSONObject cvss = (JSONObject) rootObject.get("cvss");
                score = Double.parseDouble((String) cvss.get("cvss_base_score"));
            }
            return new CvssInfo(score, 3.0d);
        } catch (Exception e) {
            log.info("Unable to parse cvss data for bug " + this.bugName + ".");
        }
        return new CvssInfo(-1.0d, -1.0d);
    }

    public static void main(String args[]) {
        CvssInfo cvssInfo = new CvssScoreResolver("CVE-2017-7657").resolve();
        System.out.println(cvssInfo);
    }
}
