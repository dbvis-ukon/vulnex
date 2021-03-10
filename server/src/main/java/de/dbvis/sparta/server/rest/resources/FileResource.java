package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.basic.LibraryFile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Provides queries for file information (exposed at "files" path).
 */
@Path("files")
public class FileResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<LibraryFile> getAll() {
        try {
            return Constants.DATASET.getFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
