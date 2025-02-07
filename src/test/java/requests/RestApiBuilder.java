package requests;

import data.UserData;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static config.UrlConstants.BASE_URL;

public class RestApiBuilder {

    public RequestSpecification requestSpecificationWithoutAccessToken(){
        return new RequestSpecBuilder()
                //задаем базовый урл для всех запросов
                .setBaseUri(BASE_URL)
                //устанавливаем хедер для всех запросов
                .setContentType(ContentType.JSON)
                //завершаем настройку RequestSpecBuilder
                .build()
                //добавляем логирование запроса и ответа в Allure отчет
                .filter(new AllureRestAssured())
                //добавляем логирование запроса и ответа в консоль для отладки
                .log().all();
    }

    protected RequestSpecification requestSpecificationWithAccessToken(UserData userData){
        return new RequestSpecBuilder()
                //задаем базовый урл для всех запросов
                .setBaseUri(BASE_URL)
                //устанавливаем хедер для всех запросов
                .setContentType(ContentType.JSON)
                //передает Bearer токен
                .addHeader("Authorization", userData.getAccessToken())
                //завершаем настройку RequestSpecBuilder
                .build()
                //добавляем логирование запроса и ответа в Allure отчет
                .filter(new AllureRestAssured())
                //добавляем логирование запроса и ответа в консоль для отладки
                .log().all();
    }
}
