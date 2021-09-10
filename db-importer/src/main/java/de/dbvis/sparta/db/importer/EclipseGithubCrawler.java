package de.dbvis.sparta.db.importer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EclipseGithubCrawler {

    final String ECLIPSE_GITHUB_URL = "https://api.github.com/users/eclipse/repos?page=";

    private List<String> gitUrls;

    public EclipseGithubCrawler() {
        this.gitUrls = new ArrayList<String>();
    }

    public void runCrawler() throws ParseException, IOException {
        int pageNumber = 1;
        boolean pageEmpty = true;
        while (pageEmpty) {
            String page = retrievePage(pageNumber);
            int urlCount = extractUrls(page);
            pageEmpty = urlCount != 0;
        }
    }

    private int extractUrls(String page) throws ParseException {
        int result = 0;
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(page);
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String url = (String) jsonObject.get("html_url");
                gitUrls.add(url);
            }
            result = jsonArray.size();
        } catch (ClassCastException e) {
            System.out.println(page);
        }
        return result;
    }

    private String retrievePage(int pageNumber) throws IOException {
        String url = ECLIPSE_GITHUB_URL + pageNumber;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String responseBody = EntityUtils.toString(httpEntity);
        httpResponse.close();
        httpClient.close();
        return responseBody;
    }

    public static void main(String[] args) throws IOException, ParseException {
        new EclipseGithubCrawler().runCrawler();
    }

}