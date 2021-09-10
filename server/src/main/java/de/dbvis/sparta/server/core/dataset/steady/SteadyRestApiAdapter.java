package de.dbvis.sparta.server.core.dataset.steady;

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
import java.util.logging.Logger;

public class SteadyRestApiAdapter {

    private static final Logger log = Logger.getLogger(SteadyRestApiAdapter.class.getName());

    public List<SteadySpace> retrieveSpaces() throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8033/backend/spaces");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        httpResponse.close();
        httpClient.close();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(responseBody);
        List<SteadySpace> result = new ArrayList<SteadySpace>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String spaceToken = (String) jsonObject.get("spaceToken");
            String spaceName = (String) jsonObject.get("spaceName");
            result.add(new SteadySpace(spaceToken, spaceName));
        }
        return result;

    }

    public void retrieveApps(final String spaceToken) {

    }

    public void retrieveBugs() {

    }


}
