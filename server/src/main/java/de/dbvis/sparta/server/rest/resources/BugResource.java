package de.dbvis.sparta.server.rest.resources;

import de.dbvis.sparta.server.Constants;
import de.dbvis.sparta.server.rest.model.basic.Bug;
import de.dbvis.sparta.server.rest.model.basic.BugCount;
import de.dbvis.sparta.server.rest.model.basic.BugSeverity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides queries for bug information (exposed at "bugs" path).
 */
@Path("bugs")
public class BugResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Bug> getAll() {
        try {
            return Constants.DATASET.getBugs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("severity/{limit}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<BugSeverity> getBugSeverity(@PathParam("limit") int limit) {
        List<Bug> bugs = new ArrayList<Bug>(Constants.DATASET.getBugs());
        bugs.sort((b1, b2) -> Double.compare(b2.getCvssScore(), b1.getCvssScore()));
        return bugs.stream()
                .limit(limit)
                .map(e -> new BugSeverity(e.getId(), e.getName(), e.getCvssScore()))
                .collect(Collectors.toList());
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<BugCount> getBugCount() {
        return Constants.DATASET.getBugCounts();
    }

    @Path("count/{limit}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<BugCount> getBugCounts(@PathParam("limit") int limit) {
        List<BugCount> bugCounts = Constants.DATASET.getBugCounts();
        return bugCounts.subList(0, limit);
    }

}