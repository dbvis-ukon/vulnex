package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("dataset")
public class DatasetResource {

    @Path("reload")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String reloadDataset() {
        Constants.DATASET.initialize();
        return "{\"result\":\"OK\"}";
    }

}
