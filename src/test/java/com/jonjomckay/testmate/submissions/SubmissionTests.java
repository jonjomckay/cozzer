package com.jonjomckay.testmate.submissions;

import com.jonjomckay.testmate.AppTest;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SubmissionTests extends AppTest {
    private String project;
    private String submission;

    @Before
    public void createProjectAndSubmission() {
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

        // Now create a submission
        var createSubmissionRequest = new JSONObject()
                .put("externalKey", "an-external-key");

        this.submission = given()
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
    }

    @Test
    public void testSubmitAndLoadSurefireReports() throws IOException {
        // First batch
        given()
                .contentType("multipart/form-data")
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-errored.xml"))
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-failed.xml"))
                .when()
                .post("/api/1/submissions/" + submission + "/surefire")
                .then()
                .assertThat()
                .statusCode(204);

        get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites")
                .then()
                .statusCode(200)
                .body("", hasSize(2))
                .body("[0].id", hasLength(36))
                .body("[0].duration", equalTo(0.6f))
                .body("[0].name", equalTo("com.jonjomckay.testmate.junit.ErroringTest"))
                .body("[0].numberOfTestCases", equalTo(1))

                .body("[1].id", hasLength(36))
                .body("[1].duration", equalTo(0.7f))
                .body("[1].name", equalTo("com.jonjomckay.testmate.junit.FailingTest"))
                .body("[1].numberOfTestCases", equalTo(1));

        // Second batch, including already existing results
        given()
                .contentType("multipart/form-data")
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-errored.xml"))
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-failed.xml"))
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-skipped.xml"))
                .multiPart("results", "results", getClass().getClassLoader().getResourceAsStream("fixtures/surefire-successful.xml"))
                .when()
                .post("/api/1/submissions/" + submission + "/surefire")
                .then()
                .assertThat()
                .statusCode(204);

        var suites = get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites")
                .then()
                .statusCode(200);

        var suite1 = suites.body("", hasSize(4))
                .body("[0].id", hasLength(36))
                .body("[0].duration", equalTo(0.6f))
                .body("[0].name", equalTo("com.jonjomckay.testmate.junit.ErroringTest"))
                .body("[0].numberOfTestCases", equalTo(1))
                .extract()
                .path("[0].id");

        var suite2 = suites.body("[1].id", hasLength(36))
                .body("[1].duration", equalTo(0.7f))
                .body("[1].name", equalTo("com.jonjomckay.testmate.junit.FailingTest"))
                .body("[1].numberOfTestCases", equalTo(1))
                .extract()
                .path("[1].id");

        var suite3 = suites.body("[2].id", hasLength(36))
                .body("[2].duration", equalTo(0f))
                .body("[2].name", equalTo("com.jonjomckay.testmate.junit.SkippedTest"))
                .body("[2].numberOfTestCases", equalTo(1))
                .extract()
                .path("[2].id");

        var suite4 = suites.body("[3].id", hasLength(36))
                .body("[3].duration", equalTo(0.8f))
                .body("[3].name", equalTo("com.jonjomckay.testmate.junit.SuccessfulTest"))
                .body("[3].numberOfTestCases", equalTo(2))
                .extract()
                .path("[3].id");

        // Now check the test cases exist too
        get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites/" + suite1)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(suite1))
                .body("duration", equalTo(0.6f))
                .body("name", equalTo("com.jonjomckay.testmate.junit.ErroringTest"))
                .body("testCases", hasSize(1))
                .body("testCases[0].duration", equalTo(0.59f))
                .body("testCases[0].errored", equalTo(true))
                .body("testCases[0].failed", equalTo(false))
                .body("testCases[0].failureMessage", equalTo("Connection refused (Connection refused)"))
                .body("testCases[0].id", hasLength(36))
                .body("testCases[0].name", equalTo("com.jonjomckay.testmate.junit.ErroringTest"))
                .body("testCases[0].skipped", equalTo(false))
                .body("testCases[0].successful", equalTo(false));

        get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites/" + suite2)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(suite2))
                .body("duration", equalTo(0.7f))
                .body("name", equalTo("com.jonjomckay.testmate.junit.FailingTest"))
                .body("testCases", hasSize(1))
                .body("testCases[0].duration", equalTo(0.7f))
                .body("testCases[0].errored", equalTo(false))
                .body("testCases[0].failed", equalTo(true))
                .body("testCases[0].failureMessage", equalTo("A failure message"))
                .body("testCases[0].id", hasLength(36))
                .body("testCases[0].name", equalTo("com.jonjomckay.testmate.junit.FailingTest"))
                .body("testCases[0].skipped", equalTo(false))
                .body("testCases[0].successful", equalTo(false));

        get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites/" + suite3)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(suite3))
                .body("duration", equalTo(0f))
                .body("name", equalTo("com.jonjomckay.testmate.junit.SkippedTest"))
                .body("testCases", hasSize(1))
                .body("testCases[0].duration", equalTo(0f))
                .body("testCases[0].errored", equalTo(false))
                .body("testCases[0].failed", equalTo(false))
                .body("testCases[0].failureMessage", equalTo("Skipped for some reason"))
                .body("testCases[0].id", hasLength(36))
                .body("testCases[0].name", equalTo("com.jonjomckay.testmate.junit.SkippedTest"))
                .body("testCases[0].skipped", equalTo(true))
                .body("testCases[0].successful", equalTo(false));

        get("/api/1/projects/" + project + "/submissions/" + submission + "/tests/suites/" + suite4)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(suite4))
                .body("duration", equalTo(0.8f))
                .body("name", equalTo("com.jonjomckay.testmate.junit.SuccessfulTest"))
                .body("testCases", hasSize(2))
                .body("testCases[0].duration", equalTo(0.4f))
                .body("testCases[0].errored", equalTo(false))
                .body("testCases[0].failed", equalTo(false))
                .body("testCases[0].failureMessage", nullValue())
                .body("testCases[0].id", hasLength(36))
                .body("testCases[0].name", equalTo("testSuccessfulOne"))
                .body("testCases[0].skipped", equalTo(false))
                .body("testCases[0].successful", equalTo(true))

                .body("testCases[1].duration", equalTo(0.4f))
                .body("testCases[1].errored", equalTo(false))
                .body("testCases[1].failed", equalTo(false))
                .body("testCases[1].failureMessage", nullValue())
                .body("testCases[1].id", hasLength(36))
                .body("testCases[1].name", equalTo("testSuccessfulTwo"))
                .body("testCases[1].skipped", equalTo(false))
                .body("testCases[1].successful", equalTo(true));
    }

    @Test
    public void testSubmitWithInvalidSurefireReport() {
        given()
                .contentType("multipart/form-data")
                .multiPart("results", "results", "hello".getBytes())
                .when()
                .post("/api/1/submissions/" + submission + "/surefire")
                .then()
                .assertThat()
                .statusCode(400)
                .body("title", equalTo("Bad Request"))
                .body("detail", equalTo("Unable to parse the incoming Surefire report"))
                .body("status", equalTo(400));
    }

    @Test
    public void testSubmitSurefireWithNonExistentSubmission() {
        given()
                .contentType("multipart/form-data")
                .when()
                .post("/api/1/submissions/00000000-0000-0000-0000-000000000000/surefire")
                .then()
                .assertThat()
                .statusCode(404)
                .body("title", equalTo("Not Found"))
                .body("detail", equalTo("No submission could be found with that ID"))
                .body("status", equalTo(404));
    }
}
