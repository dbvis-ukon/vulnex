package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.intersection.ReferencedItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("referenced")
public class ReferencedItemResource {

    @Path("repositories")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ReferencedItem> getAllRepositories() {
        try {
            return Constants.DATASET.getReferencedRepository();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("bugs")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ReferencedItem> getAllBugs() {
        try {
            return Constants.DATASET.getReferencedBugs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("files")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ReferencedItem> getAllLibraries() {
        try {
            return Constants.DATASET.getReferencedFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("modules")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ReferencedItem> getAllModules() {
        try {
            return Constants.DATASET.getReferenceModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
