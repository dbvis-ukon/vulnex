package de.dbvis.sparta.db.importer.repo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RepoInfo {

    public final String repo;
    public final long issues; // open_issues
    public final long stars; // stargazers_count
    public final long watchers; // subscribers_count;

    public RepoInfo() {
        this.repo = "-";
        this.issues = -1;
        this.stars = -1;
        this.watchers = -1;
    }

    public RepoInfo(final String repo,
                    final long issues,
                    final long stars,
                    final long watchers) {
        this.repo = repo;
        this.issues = issues;
        this.stars = stars;
        this.watchers = watchers;
    }

    public static RepoInfo createFromJsonString(final String jsonString) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject project = (JSONObject) jsonParser.parse(jsonString);
            String repo = (String) project.get("name");
            long issues = (long) project.get("open_issues");
            long stars = (long) project.get("stargazers_count");
            long watchers = (long) project.get("subscribers_count");
            return new RepoInfo(repo, issues, stars, watchers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // new RepoInfo();
    }

    @Override
    public String toString() {
        return RepoInfo.class.getName() + "[\n"
                + "  repo: " + repo + ",\n"
                + "  issues: " + issues + ",\n"
                + "  stars: " + stars + ",\n"
                + "  watchers: " + watchers + "\n"
                + "]";
    }

}
