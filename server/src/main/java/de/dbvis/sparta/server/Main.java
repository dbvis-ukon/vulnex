package de.dbvis.sparta.server;

import de.dbvis.sparta.server.core.dataset.sqlite.FileCopier;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.core.dataset.steady.SteadyDataset;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Main class.
 *
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in de.dbvis.sparta.server package
        final ResourceConfig rc = new ResourceConfig()
                .packages(Constants.REST_RESOURCES)
                .register(new CorsFilter())
                .register(JacksonFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Constants.API_BASE_URI), rc);

        // Add a handler for static pages (web client)
        final CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Main.class.getClassLoader(), "/");
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");

        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (Constants.DEMO_MODE) {
            // Copy the files to make them accessible
            FileCopier.copyFiles();
        }
        Constants.DATASET = Constants.DEMO_MODE ? SqliteDataset.getInstance() : SteadyDataset.getInstance();
        Constants.DATASET.initialize();
        startServer();
    }

    /**
     * This class is not instantiable.
     */
    private Main() { throw new IllegalStateException("Non-instantiable class!"); }

}

