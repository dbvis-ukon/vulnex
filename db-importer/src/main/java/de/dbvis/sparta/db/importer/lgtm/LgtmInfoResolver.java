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

    private static final String LGTM_API_TOKEN = System.getenv("LGTM_API_TOKEN");
    private String repo;

    public LgtmInfoResolver(String repo) {
        this.repo = repo;
    }

    public LgtmInfo retrieveLgtmData() {
        try {
            String projectData = downloadData(buildProjectApiUrl());
            return LgtmInfo.createFromJsonString(repo, projectData);
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

}
