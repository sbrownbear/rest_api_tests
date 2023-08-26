package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomApiListener {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl"); // Расширяет логирование
        FILTER.setResponseTemplate("response.ftl"); // Расширяет логирование
        return FILTER;
    }
}
