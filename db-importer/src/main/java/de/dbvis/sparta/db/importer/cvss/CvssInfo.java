package de.dbvis.sparta.db.importer.cvss;

public class CvssInfo {

    public final double score;
    public final double version;

    public CvssInfo(double score, double version) {
        this.score = score;
        this.version = version;
    }

    @Override
    public String toString() {
        return CvssInfo.class.getName() + "[\n"
                + "  score: " + score + ",\n"
                + "  version: " + version + "\n"
                + "]";
    }

}
