package com.jonjomckay.testmate;

import io.restassured.RestAssured;
import org.jooby.test.JoobyRule;
import org.junit.ClassRule;
import org.junit.Rule;

public abstract class AppTest {
    static {
        RestAssured.port = 10001;
    }

    /**
     * One app/server for all the test of this class. If you want to start/stop a new server per test,
     * remove the static modifier and replace the {@link ClassRule} annotation with {@link org.junit.Rule}.
     */
    @Rule
    public JoobyRule app = new JoobyRule(new App());
}
