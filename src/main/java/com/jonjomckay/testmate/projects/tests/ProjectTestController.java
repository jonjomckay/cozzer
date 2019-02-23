package com.jonjomckay.testmate.projects.tests;

import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Path("/api/1/projects/{project}/tests")
@Consumes("application/json")
@Produces("application/json")
public class ProjectTestController {
    private final ProjectTestManager manager;

    @Inject
    public ProjectTestController(ProjectTestManager manager) {
        this.manager = manager;
    }

    @Path("/suites")
    @GET
    public List<ProjectTestSuiteListResponse> listProjectTestSuites(UUID project) {
        return manager.listProjectTestSuites(project);
    }

    @Path("/suites/{id}")
    @GET
    public ProjectTestSuiteResponse loadProjectTestSuite(UUID project, UUID id) {
        return manager.loadProjectTestSuite(project, id)
                .orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "No test suite could be found with that ID"));
    }
}
