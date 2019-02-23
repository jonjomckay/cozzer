package com.jonjomckay.testmate.projects;

import org.jdbi.v3.core.Handle;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectManager {
    private final Handle handle;

    @Inject
    public ProjectManager(Handle handle) {
        this.handle = handle;
    }

    public List<Project> listProjects() {
        return handle.createQuery("SELECT id, slug, name FROM projects ORDER BY name")
                .mapToBean(Project.class)
                .list();
    }

    public Project createProject(ProjectCreateRequest request) {
        var exists = handle.createQuery("SELECT EXISTS (SELECT 1 FROM projects WHERE slug = :slug)")
                .bind("slug", request.getSlug())
                .mapTo(boolean.class)
                .findOnly();

        if (exists) {
            throw Problem.valueOf(Status.CONFLICT, "A project already exists with that slug");
        }

        return handle.createQuery("INSERT INTO projects (id, name, slug) VALUES (:id, :name, :slug) RETURNING id, name, slug")
                .bind("id", UUID.randomUUID())
                .bind("name", request.getName())
                .bind("slug", request.getSlug())
                .mapToBean(Project.class)
                .findOnly();
    }

    public Optional<Project> loadProject(UUID id) {
        return handle.createQuery("SELECT id, name, slug FROM projects WHERE id = :id")
                .bind("id", id)
                .mapToBean(Project.class)
                .findFirst();
    }
}
