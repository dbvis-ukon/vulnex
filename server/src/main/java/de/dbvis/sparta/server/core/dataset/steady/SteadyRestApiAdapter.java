package de.dbvis.sparta.server.core.dataset.steady;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.basic.*;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.data.RepositoryData;
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

    private List<LibraryFile> files;
    private List<Bug> bugs;
    private List<Vulnerability> vulnerabilities;
    private List<Module> modules;
    private List<Repository> repositories;

    // Thread-safe initialization of singleton
    private static SteadyRestApiAdapter instance = new SteadyRestApiAdapter();

    /**
     * Constructs a SteadyRestApiAdapter.
     */
    private SteadyRestApiAdapter() {
        reset();
    }

    private void reset() {
        files = new ArrayList<LibraryFile>();
        bugs = new ArrayList<Bug>();
        vulnerabilities = new ArrayList<Vulnerability>();
        modules = new ArrayList<Module>();
        repositories = new ArrayList<Repository>();
    }

    /**
     * Returns the single instance of the SteadyRestApiAdapter class.
     * @return the concrete instance of this class
     */
    public static SteadyRestApiAdapter getInstance() {
        return instance;
    }

    public List<LibraryFile> getFiles() {
        return this.files;
    }

    public List<Bug> getBugs() {
        return this.bugs;
    }

    public List<Vulnerability> getVulnerabilities() {
        return this.vulnerabilities;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public List<Repository> getRepositories() {
        return this.repositories;
    }

    public void retrieveData() throws IOException, ParseException{
        reset();
        for (SteadySpace space : retrieveSpaces()) {
            if (space.isEnabled()) {
                parseAppsIncludingBugs(space, retrieveAppsIncludingBugs(space));
            }
        }
    }

    private List<SteadySpace> retrieveSpaces() throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(Constants.STEADY_BACKEND + "/spaces");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        httpResponse.close();
        httpClient.close();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(responseBody);
        List<SteadySpace> result = new ArrayList<SteadySpace>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            SteadySpace steadySpace = createSteadySpace(jsonObject);
            result.add(steadySpace);
        }
        return result;
    }

    private SteadySpace createSteadySpace(JSONObject jsonObject) {
        String spaceToken = (String) jsonObject.get("spaceToken");
        String spaceName = (String) jsonObject.get("spaceName");
        String exportConfiguration = (String) jsonObject.get("exportConfiguration");
        return new SteadySpace(spaceToken, spaceName, exportConfiguration);
    }

    private String retrieveAppsIncludingBugs(SteadySpace steadySpace) throws IOException {
        final String url = Constants.STEADY_BACKEND + "/spaces/" + steadySpace.getSpaceToken() + "/apps?includeBugs=true";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        httpResponse.close();
        httpClient.close();
        return responseBody;
    }

    private void parseAppsIncludingBugs(SteadySpace steadySpace, String responseBody) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(responseBody);

        List<Module> parentModules = new ArrayList<Module>();

        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            JSONObject workspace = (JSONObject) jsonObject.get("workspace");

            JSONObject app = (JSONObject) jsonObject.get("app");
            Module module = createModule(app);
            module = addOrFindInList(module, modules);
            addOrFindInList(module, parentModules);

            JSONArray vulnerableDependencies = (JSONArray) jsonObject.get("vulnerableDependencies");
            for (Object vulDepObj : vulnerableDependencies) {
                JSONObject vulDep = (JSONObject) vulDepObj;

                JSONObject dep = (JSONObject) vulDep.get("dep");
                LibraryFile file = createLibraryFile(dep);
                file = addOrFindInList(file, files);

                JSONObject jsonBug = (JSONObject) vulDep.get("bug");
                Bug bug = createBug(jsonBug, file);
                bug = addOrFindInList(bug, bugs);

                Vulnerability vulnerability = new Vulnerability(vulnerabilities.size(), bug, module);
                addOrFindInList(vulnerability, vulnerabilities);
            }
        }
        Repository repository = new Repository(
                repositories.size(),
                parentModules,
                new RepositoryData(steadySpace.getSpaceName()));
        repositories.add(repository);
    }

    private static <T> T addOrFindInList(T element, List<T> list) {
        T result = list.stream().filter(e -> e == element).findFirst().orElse(null);
        if (result == null) {
            list.add(element);
            return element;
        }
        return result;
    }

    private Module createModule(JSONObject app) {
        String group = (String) app.get("group");
        String artifact = (String) app.get("artifact");
        String version = (String) app.get("version");
        return new Module(modules.size(), group, artifact, version);
    }

    private LibraryFile createLibraryFile(JSONObject dep) {
        String filename = (String) dep.get("filename");
        JSONObject lib = (JSONObject) dep.get("lib");
        String digest = (String) lib.get("digest");
        return new LibraryFile(files.size(), filename, digest);
    }

    private Bug createBug(JSONObject bug, LibraryFile file) {
        String bugId = (String) bug.get("bugId");
        String description = (String) bug.get("description");
        double cvssScore = -1.0d;
        if (bug.containsKey("cvssScore")) {
            cvssScore = (double) bug.get("cvssScore");
        }
        String cvssVector = "";
        if (bug.containsKey("cvssVector")) {
            cvssVector = (String) bug.get("cvssVector");
        }
        String cvssVersion = "";
        if (bug.containsKey("cvssVersion")) {
            cvssVersion = (String) bug.get("cvssVersion");
        }
        return new Bug(bugs.size(), file, bugId, description, cvssScore, cvssVector, cvssVersion);
    }

}
