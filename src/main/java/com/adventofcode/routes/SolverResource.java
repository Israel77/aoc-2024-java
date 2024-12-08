package com.adventofcode.routes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.adventofcode.service.SolverService;
import com.adventofcode.util.Constants.Day;
import com.adventofcode.util.Constants.Part;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
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
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public String solve(@RestPath int dayNumber, @RestPath int partNumber,
            @FormParam("input") FileUpload inputPart) throws FileNotFoundException {

        solverService = new SolverService(Day.fromInteger(dayNumber), Part.fromInteger(partNumber),
                new FileInputStream(inputPart.uploadedFile().toFile()));

        return solverService.solve();
    }
}
