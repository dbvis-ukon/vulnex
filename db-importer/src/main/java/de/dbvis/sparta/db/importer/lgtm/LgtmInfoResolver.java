package de.dbvis.sparta.db.importer.lgtm;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class LgtmInfoResolver {

    private static final String LGTM_API_TOKEN = "f50a2814342df64be5ab803988fbcaceef5fc19cfb5bf80d6fc66b9fd09c0206";

    private String repo;
    private String commit;

    public LgtmInfoResolver(String repo) {
        this.repo = repo;
    }

    @Deprecated
    public LgtmInfoResolver(String repo, String commit) {
        this(repo);
        this.commit = commit;
    }

    public LgtmInfo retrieveLgtmData() {
        try {
            String projectData = downloadData(buildProjectApiUrl());
            return LgtmInfo.createFromJsonString(repo, projectData);
            /*
            JSONParser jsonParser = new JSONParser();
            JSONObject projectJson = (JSONObject) jsonParser.parse(projectData);
            long projectId = (long) projectJson.get("id");
            String commitData = downloadData(buildCommitApiUrl(projectId, commit));
            LgtmInfo lgtmInfo = LgtmInfo.createFromJsonString(repo, commitData);
            if (lgtmInfo == null) {
                return LgtmInfo.createFromJsonString(repo, projectData);
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String downloadData(String urlString) {
        try {
            HttpURLConnection con = openConnection(urlString);
            String content = retrieveData(con);
            con.disconnect();
            return content;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + LGTM_API_TOKEN);
        return con;
    }

    private String retrieveData(HttpURLConnection con) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String currLine;
        while ((currLine = inputReader.readLine()) != null) {
            stringBuilder.append(currLine);
        }
        inputReader.close();
        return stringBuilder.toString();
    }

    private String buildProjectApiUrl() {
        return "https://lgtm.com/api/v1.0/projects/g/eclipse/" + repo;
    }

    private String buildCommitApiUrl(long projectId, String commit) {
        return "https://lgtm.com/api/v1.0/analyses/" + projectId + "/commits/" + commit;
    }

    public static void main(String[] args) {
        System.out.println(new LgtmInfoResolver("deeplearning4j", "227dbd8e314cf28cc557a6cf1b4ee4051b7731b5").retrieveLgtmData());
    }

}
