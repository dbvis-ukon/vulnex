package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.basic.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Provides queries for report information (exposed at "repositories" path).
 */
@Path("repositories")
public class RepositoryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Collection<Repository> getAll() {
        try {
            return Constants.DATASET.getRepositories();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
