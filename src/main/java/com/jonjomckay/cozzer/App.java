package com.jonjomckay.cozzer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.jonjomckay.cozzer.jdbi.Slf4jLogger;
import com.jonjomckay.cozzer.projects.ProjectController;
import com.jonjomckay.cozzer.projects.submissions.ProjectSubmissionController;
import com.jonjomckay.cozzer.projects.submissions.tests.SubmissionTestController;
import com.jonjomckay.cozzer.submissions.SubmissionController;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.flyway.Flywaydb;
import org.jooby.frontend.Yarn;
import org.jooby.handlers.AssetHandler;
import org.jooby.jdbc.Jdbc;
import org.jooby.jdbi.Jdbi3;
import org.jooby.json.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.*;

public class App extends Jooby {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    {
        use(new Jackson()
                .module(new ProblemModule())
                .doWith(mapper -> mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)));
        use(new Jdbc());
        use(new Jdbi3()
                .transactionPerRequest()
                .doWith((jdbi, config) -> jdbi.setSqlLogger(new Slf4jLogger())));
        use(new Flywaydb());

        on("dev", () -> {
            use(new Yarn("v11.10.0", "v1.13.0")
                    .onStart(yarn -> yarn.execute("start"))
            );
        });

        err((req, rsp, ex) -> {
            var cause = ex.getCause() == null
                    ? ex
                    : ex.getCause();

            LOGGER.error(cause.getMessage(), cause);

            ThrowableProblem problem;

            if (cause instanceof ThrowableProblem) {
                problem = (ThrowableProblem) cause;

                if (problem.getStatus() == null) {
                    rsp.status(ex.statusCode());
                } else {
                    rsp.status(problem.getStatus().getStatusCode());
                }
            } else {
                problem = Problem.builder()
                        .withDetail(cause.getMessage())
                        .withStatus(new StatusType() {
                            @Override
                            public int getStatusCode() {
                                return ex.statusCode();
                            }

                            @Override
                            public String getReasonPhrase() {
                                return ex.getMessage();
                            }
                        })
                        .build();
            }

            rsp.type(MediaType.json)
                    .send(problem);
        });

        assets("/", "index.html");
        assets("/static/**");

        use(ProjectController.class);
        use(ProjectSubmissionController.class);
        use(SubmissionTestController.class);
        use(SubmissionController.class);
        use("GET", "*", new AssetHandler("index.html"));
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
