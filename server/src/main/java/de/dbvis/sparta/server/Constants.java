package de.dbvis.sparta.server;

import de.dbvis.sparta.server.core.dataset.Dataset;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.core.dataset.steady.ApiConfig;
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

    /** API configuration **/
    public static final ApiConfig API_CONFIG = new ApiConfig(
            "localhost",
            "8033"
    );

    /** Data source **/
    public static final Dataset DATASET = SteadyDataset.getInstance();
    //public static final Dataset DATASET = SqliteDataset.getInstance();

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
