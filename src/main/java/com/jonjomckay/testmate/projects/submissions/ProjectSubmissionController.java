package com.jonjomckay.testmate.projects.submissions;

import org.jooby.mvc.Consumes;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import javax.inject.Inject;
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
    public ProjectSubmissionCreateResponse createSubmission(UUID project) {
        return manager.createSubmission(project);
    }
}
