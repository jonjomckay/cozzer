package com.jonjomckay.cozzer.projects.submissions.tests;

import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Path("/api/1/projects/{project}/submissions/{submission}/tests")
@Consumes("application/json")
@Produces("application/json")
public class SubmissionTestController {
    private final SubmissionTestManager manager;

    @Inject
    public SubmissionTestController(SubmissionTestManager manager) {
        this.manager = manager;
    }

    @Path("/suites")
    @GET
    public List<SubmissionTestSuiteListResponse> listSubmissionTestSuites(UUID project, UUID submission) {
        return manager.listSubmissionTestSuites(submission);
    }

    @Path("/suites/{id}")
    @GET
    public SubmissionTestSuiteResponse loadSubmissionTestSuite(UUID project, UUID submission, UUID id) {
        return manager.loadSubmissionTestSuite(submission, id)
                .orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "No test suite could be found with that ID"));
    }
}
