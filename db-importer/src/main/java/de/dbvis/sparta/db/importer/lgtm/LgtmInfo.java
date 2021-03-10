package de.dbvis.sparta.db.importer.lgtm;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LgtmInfo {

    public final String repo;
    public final String commit;
    public final long alerts;
    public final String grade;

    public LgtmInfo(final String repo) {
        this.repo = repo;
        this.commit = "-";
        this.alerts = -1;
        this.grade = "-";
    }

    public LgtmInfo(final String repo,
                    final String commit,
                    final long alerts,
                    final String grade) {
        this.repo = repo;
        this.commit = commit;
        this.alerts = alerts;
        this.grade = grade;
    }

    public static LgtmInfo createFromJsonString(final String repo,
                                                final String jsonString) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject project = (JSONObject) jsonParser.parse(jsonString);
            JSONArray languages = (JSONArray) project.get("languages");
            for (Object langObj : languages) {
                JSONObject lang = (JSONObject) langObj;
                String langString = (String) lang.get("language");
                if (langString.equals("java")) {
                    String commit = (String) lang.get("commit-id");
                    long alerts = (long) lang.get("alerts");
                    String grade = (String) lang.get("grade");
                    return new LgtmInfo(repo, commit, alerts, grade);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new LgtmInfo(repo);
    }

    @Override
    public String toString() {
        return LgtmInfo.class.getName() + "[\n"
                + "  repo: " + repo + ",\n"
                + "  commit: " + commit + ",\n"
                + "  alerts: " + alerts + ",\n"
                + "  grade: " + grade + "\n"
                + "]";
    }

}
