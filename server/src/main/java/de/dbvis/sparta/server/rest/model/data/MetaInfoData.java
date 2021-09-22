package de.dbvis.sparta.server.rest.model.data;

import java.io.Serializable;

public class MetaInfoData implements Serializable {

    private static final long serialVersionUID = 10L;

    private int lgtmAlerts;

    private String lgtmGrade;

    private int githubIssues;

    private int githubStars;

    private int githubWatchers;

    public MetaInfoData() {
        this.lgtmAlerts = -1;
        this.lgtmGrade = null;
        this.githubIssues = -1;
        this.githubStars = -1;
        this.githubWatchers = -1;
    }

    public MetaInfoData(int lgtmAlerts,
                        String lgtmGrade,
                        int githubIssues,
                        int githubStars,
                        int githubWatchers) {
        this.lgtmAlerts = lgtmAlerts;
        this.lgtmGrade = lgtmGrade;
        this.githubIssues = githubIssues;
        this.githubStars = githubStars;
        this.githubWatchers = githubWatchers;
    }

    public int getLgtmAlerts() {
        return lgtmAlerts;
    }

    public void setLgtmAlerts(int lgtmAlerts) {
        this.lgtmAlerts = lgtmAlerts;
    }

    public String getLgtmGrade() {
        return lgtmGrade;
    }

    public void setLgtmGrade(String lgtmGrade) {
        this.lgtmGrade = lgtmGrade;
    }

    public int getGithubIssues() {
        return githubIssues;
    }

    public void setGithubIssues(int githubIssues) {
        this.githubIssues = githubIssues;
    }

    public int getGithubStars() {
        return githubStars;
    }

    public void setGithubStars(int githubStars) {
        this.githubStars = githubStars;
    }

    public int getGithubWatchers() {
        return githubWatchers;
    }

    public void setGithubWatchers(int githubWatchers) {
        this.githubWatchers = githubWatchers;
    }

}
