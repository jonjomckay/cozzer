package com.jonjomckay.cozzer.projects;

import com.jonjomckay.cozzer.AppTest;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ProjectSubmissionTests extends AppTest {
    private String project;

    @Before
    public void createProject() {
        // First we have to create a project
        var createProjectRequest = new JSONObject()
                .put("name", "a test project")
                .put("slug", "a-test-project-123");

        this.project = given()
                .contentType(ContentType.JSON)
                .body(createProjectRequest.toString())
                .when()
                .post("/api/1/projects")
                .then()
                .extract()
                .path("id");
    }

    @Test
    public void testCreateListAndLoadSubmission() {
        // Now create a submission
        var createSubmissionRequest = new JSONObject()
                .put("externalKey", "an-external-key");

        var id = given()
                .contentType(ContentType.JSON)
                .body(createSubmissionRequest.toString())
                .when()
                .post("/api/1/projects/" + project + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", not(emptyOrNullString()))
                .body("id", hasLength(36))
                .body("externalKey", equalTo("an-external-key"))
                .extract()
                .path("id");

        // Now list all submissions
        get("/api/1/projects/" + project + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", hasSize(1))
                .body("[0].id", equalTo(id))
                .body("[0].externalKey", equalTo("an-external-key"))
                .body("[0].createdAt", not(nullValue()))
                .body("[0].createdAt", containsString(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));

        // Now load the created submission
        get("/api/1/projects/" + project + "/submissions/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(id))
                .body("externalKey", equalTo("an-external-key"))
                .body("createdAt", not(nullValue()))
                .body("createdAt", containsString(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));

        // Now try and create a submission with the same key
        given()
                .contentType(ContentType.JSON)
                .body(createSubmissionRequest.toString())
                .when()
                .post("/api/1/projects/" + project + "/submissions")
                .then()
                .assertThat()
                .statusCode(409)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Conflict"))
                .body("detail", equalTo("A submission already exists with that external key"))
                .body("status", equalTo(409));
    }

    @Test
    public void testCreateSubmissionWithNoExternalKey() {
        // Now we try the actual request
        var createRequest = new JSONObject();

        given()
                .contentType(ContentType.JSON)
                .body(createRequest.toString())
                .when()
                .post("/api/1/projects/" + project + "/submissions")
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("An external key is required to create a submission"))
                .body("status", equalTo(400));
    }

    @Test
    public void testLoadNonExistentSubmission() {
        get("/api/1/projects/" + project + "/submissions/00000000-0000-0000-0000-000000000000")
                .then()
                .assertThat()
                .statusCode(404)
                .body("title", equalTo("Not Found"))
                .body("detail", equalTo("No submission could be found with that ID"))
                .body("status", equalTo(404));
    }
}
