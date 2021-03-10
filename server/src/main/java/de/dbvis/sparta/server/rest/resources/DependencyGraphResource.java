package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.core.DependencyGraphFactory;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.basic.Repository;
import de.dbvis.sparta.server.rest.model.graph.DependencyGraph;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Provides queries for a dependency graph (exposed at "graph" path).
 */
@Path("graph")
public class DependencyGraphResource {

    @Path("module/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public DependencyGraph getGraphOfModule(@PathParam("id") int id) {
        Module m = Constants.DATASET.getModules().stream().filter((e -> e.getId() == id)).findFirst().get();
        return new DependencyGraphFactory().createDependencyGraphForModule(m);
    }

    @Path("repository/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public DependencyGraph getGraphOfRepository(@PathParam("id") int id) {
        Repository r = Constants.DATASET.getRepositories().stream().filter(e -> e.getId() == id).findFirst().get();
        return new DependencyGraphFactory().createDependencyGraphForRepository(r);
    }

}
