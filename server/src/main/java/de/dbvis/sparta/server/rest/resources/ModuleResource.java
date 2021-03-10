package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.basic.Module;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Provides queries for module information (exposed at "modules" path).
 */
@Path("modules")
public class ModuleResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Module> getAll() {
        try {
            return Constants.DATASET.getModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
