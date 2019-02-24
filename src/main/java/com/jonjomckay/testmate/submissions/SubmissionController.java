package com.jonjomckay.testmate.submissions;

import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;

// TODO: Consider replacing this whole class with just a "complete" method that checks an S3 bucket folder and just processes everything in there at once
@Path("/api/1/submissions/{id}")
@Consumes("multipart/form-data")
@Produces("application/json")
public class SubmissionController {
    private final SubmissionManager manager;

    @Inject
    public SubmissionController(SubmissionManager manager) {
        this.manager = manager;
    }

//    @Path("metrics")
//    public void submitCustomMetrics(UUID id) {
//        // If a metric already exists, overwrite it
//    }

    @Path("surefire")
    @POST
    public void submitSurefireResults(UUID id, Request request) throws IOException {
        manager.submitSurefireResults(id, request.files("results"));
    }

//    @Path("opencover")
//    public void submitOpencoverResults(UUID id) {
//        // Merge incoming results with existing results
//    }
}
