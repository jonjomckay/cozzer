package com.jonjomckay.testmate.projects;

import com.jonjomckay.testmate.AppTest;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProjectTests extends AppTest {
    @Test
    public void testCreateListAndLoadProjects() {
        var createRequest = new JSONObject()
                .put("name", "a test project")
                .put("slug", "a-test-project-123");

        var id = given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", not(emptyOrNullString()))
                .body("id", hasLength(36))
                .body("name", equalTo("a test project"))
                .body("slug", equalTo("a-test-project-123"))
                .body("lastSubmissionAt", nullValue())
                .extract()
                .path("id");

        // Now list all projects
        get("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", hasSize(1))
                .body("[0].id", equalTo(id))
                .body("[0].name", equalTo("a test project"))
                .body("[0].slug", equalTo("a-test-project-123"))
                .body("[0].lastSubmissionAt", nullValue());

        // Now load the created project
        get("/api/1/projects/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(id))
                .body("name", equalTo("a test project"))
                .body("slug", equalTo("a-test-project-123"))
                .body("lastSubmissionAt", nullValue());

        // Now try and create a project with the same name
        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(409)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Conflict"))
                .body("detail", equalTo("A project already exists with that slug"))
                .body("status", equalTo(409));
    }

    @Test
    public void testCreateProjectWithNoName() {
        var createRequest = new JSONObject();

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("No project name was given"))
                .body("status", equalTo(400));
    }

    @Test
    public void testCreateProjectWithNoSlug() {
        var createRequest = new JSONObject()
                .put("name", "a project name");

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("No project slug was given"))
                .body("status", equalTo(400));
    }

    @Test
    public void testCreateProjectWithInvalidSlug() {
        var createRequest = new JSONObject()
                .put("name", "a project name")
                .put("slug", "a-project-slug-with-*%");

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("The project slug can only contain lowercase alphanumeric characters and hyphens"))
                .body("status", equalTo(400));
    }

    @Test
    public void testCreateProjectWithSlugStartingWithHyphen() {
        var createRequest = new JSONObject()
                .put("name", "a project name")
                .put("slug", "-a-project-slug");

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("The project slug must not start or end with a hyphen"))
                .body("status", equalTo(400));
    }

    @Test
    public void testCreateProjectWithSlugEndingWithHyphen() {
        var createRequest = new JSONObject()
                .put("name", "a project name")
                .put("slug", "a-project-slug-");

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("The project slug must not start or end with a hyphen"))
                .body("status", equalTo(400));
    }

    @Test
    public void testLoadNonExistentProject() {
        get("/api/1/projects/00000000-0000-0000-0000-000000000000")
                .then()
                .assertThat()
                .statusCode(404)
                .body("title", equalTo("Not Found"))
                .body("detail", equalTo("No project could be found with that ID"))
                .body("status", equalTo(404));
    }
}
