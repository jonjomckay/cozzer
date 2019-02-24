package com.jonjomckay.cozzer.projects;

import com.google.common.base.Strings;
import org.jooby.mvc.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Path("/api/1/projects")
@Consumes("application/json")
@Produces("application/json")
public class ProjectController {
    private final ProjectManager manager;

    @Inject
    public ProjectController(ProjectManager manager) {
        this.manager = manager;
    }

    @GET
    public List<Project> listProjects() {
        return manager.listProjects();
    }

    @POST
    public Project createProject(@Body ProjectCreateRequest request) {
        if (Strings.isNullOrEmpty(request.getName())) {
            throw Problem.valueOf(Status.BAD_REQUEST, "No project name was given");
        }

        if (Strings.isNullOrEmpty(request.getSlug())) {
            throw Problem.valueOf(Status.BAD_REQUEST, "No project slug was given");
        }

        if (request.getSlug().matches("^[a-z0-9+-]*$") == false) {
            throw Problem.valueOf(Status.BAD_REQUEST, "The project slug can only contain lowercase alphanumeric characters and hyphens");
        }

        if (request.getSlug().startsWith("-") || request.getSlug().endsWith("-")) {
            throw Problem.valueOf(Status.BAD_REQUEST, "The project slug must not start or end with a hyphen");
        }

        return manager.createProject(request);
    }

    @GET
    @Path("/{id}")
    public Project loadProject(UUID id) {
        return manager.loadProject(id)
                .orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "No project could be found with that ID"));
    }
}
