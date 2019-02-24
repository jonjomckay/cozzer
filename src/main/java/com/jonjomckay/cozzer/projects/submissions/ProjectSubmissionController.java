package com.jonjomckay.cozzer.projects.submissions;

import org.jooby.mvc.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Path("/api/1/projects/{project}/submissions")
@Consumes("application/json")
@Produces("application/json")
public class ProjectSubmissionController {
    private final ProjectSubmissionManager manager;

    @Inject
    public ProjectSubmissionController(ProjectSubmissionManager manager) {
        this.manager = manager;
    }

    @POST
    public ProjectSubmissionCreateResponse createSubmission(UUID project, @Body ProjectSubmissionCreateRequest request) {
        return manager.createSubmission(project, request);
    }

    @GET
    public List<ProjectSubmissionResponse> listSubmissions(UUID project) {
        return manager.listSubmissions(project);
    }

    @Path("/{id}")
    @GET
    public ProjectSubmissionResponse loadSubmission(UUID project, UUID id) {
        return manager.loadSubmission(project, id)
                .orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "No submission could be found with that ID"));
    }
}
