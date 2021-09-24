package de.dbvis.sparta.server;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.core.dataset.steady.SteadyDataset;

/**
 * Contains all important constants.
 */
public class Constants {

    /*
     * Server configuration
     */

    /** Base URI the Grizzly HTTP server will listen on. **/
    public static final String API_BASE_URI = "http://0.0.0.0:3000/api/";
    public static final String REST_RESOURCES = "de.dbvis.sparta.server.rest.resources";

    public static final String STEADY_BACKEND = System.getenv("STEADY_BACKEND");
    public static final boolean DEMO_MODE = Boolean.parseBoolean(System.getenv("DEMO_MODE"));

    /** Data source **/
    public static Dataset DATASET = null;

    /*
     * Application and authorship information
     */

    public static final String APP_NAME = "SPARTA-server";
    public static final String APP_VERSION = "1.0-SNAPSHOT";
    public static final String[][] CONTRIBUTORS = {
            {
                    "Frederik L. Dennig",
                    "Research Associate - Data Analysis and Visualization",
                    "https://www.vis.uni-konstanz.de/en/members/dennig"
            },
            {
                    "Eren Cakmak",
                    "Research Associate - Data Analysis and Visualization",
                    "https://www.vis.uni-konstanz.de/en/members/cakmak"
            },
            {
                    "Henrik Plate",
                    "SAP Security Research",
                    "https://people.sap.com/henrik.plate"
            },
            {
                    "Prof. Dr. Daniel A. Keim",
                    null,
                    "https://www.vis.uni-konstanz.de/en/members/keim/"
            }
    };

    /**
     * This class is not instantiable.
     */
    private Constants() { throw new IllegalStateException("Non-instantiable class!"); }

}
