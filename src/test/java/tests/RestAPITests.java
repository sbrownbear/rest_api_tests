package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAPITests {

        /*
        1. Make request to https://selenoid.autotests.cloud/status
        2. Get response { total: 20, used: 0, queued: 0, pending: 0, browsers: { android: { 8.1: { } },
        chrome: { 100.0: { }, 99.0: { } }, chrome-mobile: { 86.0: { } }, firefox: { 97.0: { }, 98.0: { } },
        opera: { 84.0: { }, 85.0: { } } } }
        3. Check total is 20
         */

    @Test
    void checkTotal() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithLogs() {
        given()
                .log().uri() // Логи запроса
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status() // Логи ответа
                .log().body() // Логи ответа
                .statusCode(200)
                .body("total", is(20));
    }

    @Test // Проверить, что есть браузер с версией 100
    void checkChromeVersion() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("browsers.chrome", hasKey("100.0"));
    }

    /*
    Плохая практика
    @Test
    void checkResponseBadPractice() {
        String expectedResponseString = "{\"total\":20,\"used\":1,\"queued\":0,\"pending\":0,\"browsers\":" +
                "{\"android\":{\"8.1\":{}}," +
                "\"chrome\":{\"100.0\":{},\"99.0\":{}}," +
                "\"chrome-mobile\":{\"86.0\":{}}," +
                "\"firefox\":{\"97.0\":{},\"98.0\":}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";

        Response actualResponse = given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertEquals(expectedResponseString, actualResponse.asString());
    }
    */

    // Хорошая практика
    @Test
    void checkResponseGoodPractice() {
        Integer expectedTotal = 20;

        Integer actualTotal = given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("total");

        assertEquals(expectedTotal, actualTotal);
    }

        /*
        1. Make request to https://selenoid.autotests.cloud/wd/hub/status, log: user1, pass: 1234
        2. Get response { value: { message: "Selenoid 1.10.7 built at 2021-11-21_05:46:32AM", ready: true } }
        3. Check value.ready is true
         */

    @Test
    void checkWdHubStatus() {
        given()
                .log().uri()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    // Тот же тест, логин и пароль передаем не в get(), а auth().basic()
    @Test
    void checkWdHubStatus2() {
        given()
                .log().uri()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    // Негативный тест, пользователь не имеет доступа без авторизации
    @Test
    void checkWdHubStatus401() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }
}