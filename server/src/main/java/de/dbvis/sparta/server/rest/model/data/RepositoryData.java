package de.dbvis.sparta.server.rest.model.data;

import java.io.Serializable;

public class RepositoryData implements Serializable {

    private static final long serialVersionUID = 12L;

    /**
     * The name of the repository
     */
    private String name;

    private MetaInfoData metaInfo;

    public RepositoryData() {

    }

    public RepositoryData(String name,
                          MetaInfoData metaInfo) {
        this.name = name;
        this.metaInfo = metaInfo;
    }

    public RepositoryData(String name,
                          int lgtmAlerts,
                          String lgtmGrade,
                          int githubIssues,
                          int githubStars,
                          int githubWatchers) {
        this.name = name;
        this.metaInfo = new MetaInfoData(lgtmAlerts, lgtmGrade, githubIssues, githubStars, githubWatchers);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetaInfoData getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfoData metaInfo) {
        this.metaInfo = metaInfo;
    }
}
