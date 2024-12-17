package com.adventofcode.routes;

import java.io.FileNotFoundException;

import org.eclipse.microprofile.faulttolerance.Timeout;

import com.adventofcode.service.SolverService;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/solve")
public class SolverResource {
    private SolverService solverService;

    @Path("/{dayNumber}/{partNumber}")
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(600_000)
    @RunOnVirtualThread
    public String solve(@BeanParam RequestBean request) throws FileNotFoundException {

        solverService = new SolverService(request);

        return solverService.solve();
    }
}
