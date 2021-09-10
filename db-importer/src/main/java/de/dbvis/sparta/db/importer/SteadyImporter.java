package de.dbvis.sparta.db.importer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.text.Style;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SteadyImporter {

    private static final String STEADY_APP_GOAL = "org.eclipse.steady:plugin-maven:3.2.1-SNAPSHOT:app";
    private static final String REPOSITORIES_FILE = "./repos.txt";
    private static final String REPOSITORIES_DIR = "./vulnex/";
    private static final String STEADY_REST_API = "http://localhost:8033/backend/";

    private List<File> pomFiles;

    public SteadyImporter() {
        this.pomFiles = new ArrayList<File>();
    }

    public void runSteadyImporter() throws InterruptedException, IOException, ParseException, ParserConfigurationException, SAXException {
        File repositoriesDir = new File(REPOSITORIES_DIR);
        File[] repos = repositoriesDir.listFiles();
        for (File r : repos) {
            String spaceToken = createWorkspaceForRepository("https://github.com/eclipse/" + r.getName());
            pomFiles = new ArrayList<File>();
            findPomFiles(r);
            for (File f : pomFiles) {
                performSteadyScanForPomFile(spaceToken, f);
            }
        }
    }

    private void findPomFiles(final File file) {
        File[] list = file.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    findPomFiles(f);
                } else if (f.getName().equalsIgnoreCase("pom.xml")) {
                    pomFiles.add(f);
                }
            }
        }
    }

    private void cloneAllRepositoriesInReposFile() throws InterruptedException, IOException {
        File repositoriesDir = new File(REPOSITORIES_DIR);
        if (!repositoriesDir.exists()) {
            repositoriesDir.mkdir();
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(REPOSITORIES_FILE));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            runGitClone(line);
        }
    }

    private int runGitClone(final String repoUrl) throws InterruptedException, IOException {
        Runtime runtime = Runtime.getRuntime();
        String command = "git clone " + repoUrl + " " + REPOSITORIES_DIR + extractRepoName(repoUrl);
        System.out.println(command);
        Process process = runtime.exec(command);
        return process.waitFor();
    }

    private String extractRepoName(final String repoUrl) {
        String[] split = repoUrl.split("/");
        return split[split.length - 1];
    }

    private String createWorkspaceForRepository(final String repoUrl) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(STEADY_REST_API + "spaces");
        httpPost.setHeader("accept", "application/json");
        httpPost.setHeader("content-type", "application/json");
        String json = createJsonObjectForRepository(repoUrl);
        System.out.println(json);
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        httpResponse.close();
        httpClient.close();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
        String spaceToken = (String) jsonObject.get("spaceToken");
        return spaceToken;
    }

    private String createJsonObjectForRepository(final String repoUrl) {
        String repoName = extractRepoName(repoUrl);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceName", repoName);
        jsonObject.put("spaceDescription", repoName);
        jsonObject.put("exportConfiguration", "DETAILED");
        jsonObject.put("public", true);
        jsonObject.put("default", false);
        JSONArray jsonArray = new JSONArray();
        JSONObject properties = new JSONObject();
        properties.put("source", "USER");
        properties.put("name", "repo");
        properties.put("value", repoUrl);
        jsonArray.add(properties);
        jsonObject.put("properties", jsonArray);
        return jsonObject.toJSONString();
    }

    private void performSteadyScanForPomFile(final String spaceToken, final File pomFile) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        MavenIdentifier mavenIdentifier = extractMavenIdentifier(pomFile.getPath());
        String directoryPath = pomFile.getParentFile().getPath();
        createSteadyPropertiesFile(directoryPath, spaceToken, mavenIdentifier);
        runSteadyScan(directoryPath);
    }

    private MavenIdentifier extractMavenIdentifier(final String pomFilePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(pomFilePath));
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        String groupId = root.getElementsByTagName("groupId").item(0).getTextContent();
        String artifactId = root.getElementsByTagName("artifactId").item(0).getTextContent();
        String version = root.getElementsByTagName("version").item(0).getTextContent();
        return new MavenIdentifier(groupId, artifactId, version);
    }

    private void createSteadyPropertiesFile(final String directoryPath,
                                            final String spaceToken,
                                            final MavenIdentifier mavenIdentifier) throws FileNotFoundException  {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("vulas.core.space.token = ");
        stringBuilder.append(spaceToken);
        stringBuilder.append('\n');
        stringBuilder.append("vulas.core.appContext.group = ");
        stringBuilder.append(mavenIdentifier.getGroupId());
        stringBuilder.append('\n');
        stringBuilder.append("vulas.core.appContext.artifact = ");
        stringBuilder.append(mavenIdentifier.getArtifactId());
        stringBuilder.append('\n');
        stringBuilder.append("vulas.core.appContext.version = ");
        stringBuilder.append(mavenIdentifier.getVersion());
        stringBuilder.append('\n');
        stringBuilder.append("vulas.shared.backend.serviceUrl = http://localhost:8033/backend/");
        PrintWriter printWriter = new PrintWriter(directoryPath + "/vulas-custom.properties");
        printWriter.print(stringBuilder.toString());
        printWriter.flush();
        printWriter.close();
    }

    private void runSteadyScan(final String directoryPath) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /c mvn compile " + STEADY_APP_GOAL, null, new File(directoryPath));
        if (process.waitFor(10, TimeUnit.MINUTES)) {
            System.out.println("Excited!");
        } else {
            System.out.println("Interrupted!");
        }
    }

    public static void main(String[] args) throws Exception {
        SteadyImporter steadyImporter = new SteadyImporter();
        steadyImporter.runSteadyImporter();
    }

}
