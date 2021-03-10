package de.dbvis.sparta.db.importer;

import de.dbvis.sparta.db.connector.DbConnector;
import de.dbvis.sparta.db.connector.SqLiteDbConnector;
import de.dbvis.sparta.db.importer.cvss.CvssInfo;
import de.dbvis.sparta.db.importer.cvss.CvssScoreResolver;
import de.dbvis.sparta.db.importer.lgtm.LgtmInfo;
import de.dbvis.sparta.db.importer.lgtm.LgtmInfoResolver;
import de.dbvis.sparta.db.importer.repo.RepoInfo;
import de.dbvis.sparta.db.importer.repo.RepoInfoResolver;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static de.dbvis.sparta.db.importer.Main.randomInt;

public class SqLiteImporter {

    private static final Logger log = Logger.getLogger(SqLiteImporter.class.getName());

    public static final String REPORT_FILE_NAME = "vulas-report.json";
    public static final DbConnector db = SqLiteDbConnector.createSqLiteDbConnector();

    public File repoDir;

    public SqLiteImporter() {

    }

    public SqLiteImporter(File repoDir) {
        this.repoDir = repoDir;
    }

    public void createDatabase() throws SQLException {
        createAllTables();
        iterateOverAllReportFiles();
    }

    private void createAllTables() throws SQLException {
        createBugTable();
        createFileTable();
        createModuleTable();
        createVulnerabilityTable();
        createReportTable();
    }

    private void createFileTable() throws SQLException {
        log.info("Creating file table.");
        final String update =
                "CREATE TABLE file ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"name\" TEXT, "
                        + "\"sha1\" TEXT"
                        + ");";
        db.executeUpdate(update);
    }

    private void createBugTable() throws SQLException {
        log.info("Creating bug table.");
        final String update =
                "CREATE TABLE bug ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"fileId\" INTEGER, "
                        + "\"name\" TEXT, "
                        + "\"cvssScore\" REAL, "
                        + "\"cvssVersion\" REAL"
                        + ");";
        db.executeUpdate(update);
    }

    private void createModuleTable() throws SQLException {
        log.info("Creating module table.");
        final String update =
                "CREATE TABLE module ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"parentModuleId\" INTEGER, "
                        + "\"groupId\" TEXT, "
                        + "\"artifactId\" TEXT, "
                        + "\"version\" TEXT"
                        + ");";
        db.executeUpdate(update);
    }

    private void createVulnerabilityTable() throws SQLException {
        log.info("Creating vulnerability table.");
        final String update =
                "CREATE TABLE vulnerability ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"bugId\" INTEGER, "
                        + "\"moduleId\" INTEGER, "
                        + "\"scope\" TEXT, "
                        + "\"isTransitive\" BOOLEAN, "
                        + "\"containsVulnerableCode\" TEXT, "
                        + "\"potentiallyExecutesVulnerableCode\" TEXT, "
                        + "\"actuallyExecutesVulnerableCode\" TEXT"
                        + ");";
        db.executeUpdate(update);
    }

    private void createReportTable() throws SQLException {
        log.info("Creating report table.");
        final String update =
                "CREATE TABLE report ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"parentModuleId\" INTEGER, "
                        + "\"repoName\" TEXT, "
                        + "\"filePath\" TEXT, "
                        + "\"generatedAt\" DATETIME, "
                        + "\"isAggregated\" BOOLEAN"
                        + ");";
        db.executeUpdate(update);
    }

    private void createMetaInfoTable() throws SQLException {
         final String update =
                 "CREATE TABLE metainfo ("
                        + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "\"repo\" TEXT NOT NULL UNIQUE, "
                        + "\"lgtmAlerts\" INTEGER, "
                        + "\"lgtmGrade\" TEXT, "
                        + "\"ghIssues\" INTEGER, "
                        + "\"ghStars\" INTEGER, "
                        + "\"ghWatchers\" INTEGER"
                        + ");";
         db.executeUpdate(update);
    }

    private void dropTableIfExists(final String name) throws SQLException {
        log.info("Dropping table " + name + ".");
        final String update = "DROP TABLE IF EXISTS " + name + ";";
        db.executeUpdate(update);
    }

    private void iterateOverAllReportFiles() {
        log.info("Searching report files.");
        findFilesWithName(this.repoDir, REPORT_FILE_NAME).stream()
                .filter(File::exists)
                .forEach(this::importJsonFile);
        log.info("Done.");
    }

    private void importJsonFile(File jsonFile) {
        String filePath = jsonFile.getAbsolutePath();
        log.info("Loading " + filePath + ".");
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(jsonFile)) {
            JSONObject rootObject = (JSONObject) parser.parse(reader);
            JSONObject vulasReport = (JSONObject) rootObject.get("vulasReport");
            JSONArray vulnerabilities = (JSONArray) vulasReport.get("vulnerabilities");
            addAllFilesToDatabase(vulnerabilities);
            addAllBugsToDatabase(vulnerabilities);
            addAllModulesToDatabase(vulasReport);
            addEntryToReportTable(filePath, vulasReport);
            addAllVulnerabilitiesToDatabase(vulnerabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAllFilesToDatabase(JSONArray vulnerabilities) {
        log.info("Adding all files.");
        vulnerabilities.forEach(e -> {
            JSONObject vulnerability = (JSONObject) e;
            try {
                addEntryToFileTable(vulnerability);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addAllBugsToDatabase(JSONArray vulnerabilities) {
        log.info("Adding all bugs.");
        vulnerabilities.forEach(e -> {
            JSONObject vulnerability = (JSONObject) e;
            try {
                addEntryToBugTable(vulnerability);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addAllVulnerabilitiesToDatabase(JSONArray vulnerabilities) {
        log.info("Adding all vulnerabilities.");
        vulnerabilities.forEach(e -> {
            JSONObject vulnerability = (JSONObject) e;
            JSONObject bug = (JSONObject) vulnerability.get("bug");
            String bugName = (String) bug.get("id");
            try {
                String bugQuery = "SELECT id FROM bug WHERE \"name\"=\"" + bugName + "\";";
                CachedRowSet crs = db.executeQueryWithCachedResult(bugQuery);
                crs.next();
                final int bugId = crs.getInt(1);
                JSONArray modules = (JSONArray) vulnerability.get("modules");
                modules.forEach(m -> {
                    JSONObject module = (JSONObject) m;
                    try {
                        addEntryToVulnerabilityTable(bugId, module);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private int retrieveModuleId(JSONObject module) throws SQLException {
        final String artifactId = (String) module.get("artifactId");
        final String groupId = (String) module.get("groupId");
        final String version = (String) module.get("version");
        String query = "SELECT id FROM module WHERE \"artifactId\"=\"" + artifactId + "\" AND \"groupId\"=\"" + groupId + "\" AND \"version\"=\"" + version + "\";";
        CachedRowSet crs = db.executeQueryWithCachedResult(query);
        crs.next();
        return crs.getInt(1);
    }

    private void addEntryToFileTable(JSONObject vulnerability) throws SQLException {
        final String name = (String) vulnerability.get("filename");
        final String sha1 = (String) vulnerability.get("sha1");
        String test = "SELECT EXISTS(SELECT 1 FROM file WHERE \"sha1\"=\"" + sha1 + "\");";
        CachedRowSet crs = db.executeQueryWithCachedResult(test);
        if (crs.next() && !crs.getBoolean(1)) {
            final String update = "INSERT INTO file (\"name\", \"sha1\") VALUES (\"" + name + "\", \"" + sha1 + "\");";
            db.executeUpdate(update);
        }
    }

    private void addEntryToBugTable(JSONObject vulnerability) throws SQLException {
        final JSONObject bug = (JSONObject) vulnerability.get("bug");
        final String name = (String) bug.get("id");
        final CvssInfo cvssInfo = new CvssScoreResolver(name).resolve();
        String test = "SELECT EXISTS(SELECT 1 FROM bug WHERE \"name\"=\"" + name + "\");";
        CachedRowSet crs = db.executeQueryWithCachedResult(test);
        if (crs.next() && !crs.getBoolean(1)) {
            final String sha1 = (String) vulnerability.get("sha1");
            String query = "SELECT id FROM file WHERE \"sha1\"=\"" + sha1 + "\";";
            crs = db.executeQueryWithCachedResult(query);
            crs.next();
            final int fileId = crs.getInt(1);
            final String update = "INSERT INTO bug (\"fileId\", \"name\", \"cvssScore\", \"cvssVersion\") VALUES (" + fileId + ", \"" + name + "\", " + cvssInfo.score + ", " + cvssInfo.version + ");";
            db.executeUpdate(update);
        }
    }

    private void addAllModulesToDatabase(JSONObject vulasReport) throws SQLException {
        log.info("Adding all modules.");
        JSONObject parentModule = (JSONObject) vulasReport.get("generatedFor");
        addEntryToModuleTable(parentModule, -1);
        final int parentModuleId = retrieveModuleId(parentModule);
        final boolean isAggregated = (boolean) vulasReport.get("isAggregated");
        if (isAggregated) {
            JSONArray aggregatedModules = (JSONArray) vulasReport.get("aggregatedModules");
            aggregatedModules.stream().forEach(e -> {
                JSONObject module = (JSONObject) e;
                try {
                    addEntryToModuleTable(module, parentModuleId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private void addEntryToModuleTable(JSONObject module, Integer parentModuleId) throws SQLException {
        final String groupId = (String) module.get("groupId");
        final String artifactId = (String) module.get("artifactId");
        final String version = (String) module.get("version");
        final String update = "INSERT INTO module (\"parentModuleId\", \"groupId\", \"artifactId\", \"version\") VALUES (" + parentModuleId + ", \"" + groupId + "\", \"" + artifactId + "\", \"" + version + "\");";
        db.executeUpdate(update);
    }

    private void addEntryToReportTable(final String filePath, JSONObject report) throws SQLException {
        String generatedAt = (String) report.get("generatedAt");
        boolean isAggregated = (boolean) report.get("isAggregated");
        final int parentModuleId = retrieveModuleId((JSONObject) report.get("generatedFor"));
        final String repoName = filePath.substring(this.repoDir.getAbsolutePath().length() + 1).split("[/\\\\]")[0];
        final String update = "INSERT INTO report (\"parentModuleId\", \"repoName\", \"filePath\", \"generatedAt\", \"isAggregated\") VALUES (" + parentModuleId + ", \"" + repoName + "\", \"" + filePath + "\", \"" + generatedAt + "\", \"" + isAggregated + "\");";
        db.executeUpdate(update);
    }

    private void addEntryToVulnerabilityTable(int bugId, JSONObject module) throws SQLException {
        final int moduleId = retrieveModuleId(module);
        final String scope = (String) module.get("scope");
        final boolean isTransitive = (boolean) module.get("isTransitive");
        final String containsVulnerableCode = (String) module.get("containsVulnerableCode");
        final String potentiallyExecutesVulnerableCode = (String) module.get("potentiallyExecutesVulnerableCode");
        final String actuallyExecutesVulnerableCode = (String) module.get("actuallyExecutesVulnerableCode");
        final String update = "INSERT INTO vulnerability (\"bugId\", \"moduleId\", \"scope\", \"isTransitive\", \"containsVulnerableCode\", \"potentiallyExecutesVulnerableCode\", \"actuallyExecutesVulnerableCode\") VALUES " +
                "(" + bugId + ", " + moduleId + ", \"" + scope + "\", \"" + isTransitive + "\", \"" + containsVulnerableCode + "\", \"" + potentiallyExecutesVulnerableCode + "\", \"" + actuallyExecutesVulnerableCode + "\");";
        db.executeUpdate(update);
    }

    private void addEntryToMetaInfoTable(LgtmInfo lgtmInfo, RepoInfo repoInfo) throws SQLException {
        if (repoInfo == null) {
            return;
        }
        final String update = "INSERT INTO metainfo (\"repo\", \"lgtmAlerts\", \"lgtmGrade\", \"ghIssues\", \"ghStars\", \"ghWatchers\") VALUES " +
                "(\"" + lgtmInfo.repo + "\", " + lgtmInfo.alerts + ", \"" + lgtmInfo.grade + "\", " + repoInfo.issues + ", " + repoInfo.stars + ", " + repoInfo.watchers + ");";
        db.executeUpdate(update);
    }

    public void createAndFillMetaInfoTable() throws Exception {
        dropTableIfExists("metainfo");
        createMetaInfoTable();
        List<String> repoNames = retrieveAllRepoNames();
        for (String repo : repoNames) {
            System.out.println(repo);
            LgtmInfo lgtmInfo = new LgtmInfoResolver(repo).retrieveLgtmData();
            RepoInfo repoInfo = new RepoInfoResolver(repo).resolve();
            addEntryToMetaInfoTable(lgtmInfo, repoInfo);
            Thread.sleep(100000);
        }
    }

    private static List<String> retrieveAllRepoNames() {
        try {
            String query = "SELECT DISTINCT repoName FROM report;";
            CachedRowSet crs = db.executeQueryWithCachedResult(query);
            List<String> result = new ArrayList<String>(crs.size());
            while (crs.next()) {
                result.add(crs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> retrieveAllReposFromMetaInfoTable() {
        try {
            String query = "SELECT DISTINCT repo FROM metainfo;";
            CachedRowSet crs = db.executeQueryWithCachedResult(query);
            List<String> result = new ArrayList<String>(crs.size());
            while (crs.next()) {
                result.add(crs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<File> findFilesWithName(File directory, String fileName) {
        List<File> result = new ArrayList<File>();
        Collection<File> files = FileUtils.listFiles(directory, null, true);
        for (File f : files) {
            if (f.getName().equals(fileName)) {
                result.add(f);

            }
        }
        return result;
    }

    private static double parseDoubleWrapper(String n) {
        try {
            return Double.parseDouble(n);
        } catch (NumberFormatException e) {
            return -1.0d;
        }
    }

    public static void main(String[] args) {
        List<String> ori = retrieveAllRepoNames();
        List<String> mir = retrieveAllReposFromMetaInfoTable();
        ori.removeAll(mir);
        System.out.println(ori);
    }

}
