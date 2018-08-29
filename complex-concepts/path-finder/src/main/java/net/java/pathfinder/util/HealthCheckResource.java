package net.java.pathfinder.util;

import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/application")
public class HealthCheckResource {

    @GET
    @Path("/diskSpace")
    @Produces("text/plain")
//    @Health
    public long checkDiskspace() {
        return new File(".").getFreeSpace();
    }
}
