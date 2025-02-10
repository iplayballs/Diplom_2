package requests;

import data.UserData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.UrlConstants.*;
import static io.restassured.RestAssured.given;

public class UserApi extends RestApiBuilder {

    Response response;

    private final RestApiBuilder restApiBuilder = new RestApiBuilder();

    @Step("Регистрация нового пользователя в системе")
    public Response registerUser(UserData userData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithoutAccessToken())
                        .body(userData)
                        .post(REGISTER_USER);

        userData.setAccessToken(response.jsonPath().getString("accessToken"));
        userData.setRefreshToken(response.jsonPath().getString("refreshToken"));

        return response;
    }

    @Step("Авторизация пользователя в системе")
    public Response loginUser(UserData userData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithoutAccessToken())
                        .body(userData)
                        .post(LOGIN_USER);

        userData.setAccessToken(response.jsonPath().getString("accessToken"));
        userData.setRefreshToken(response.jsonPath().getString("refreshToken"));

        return response;
    }

    @Step("Удаление пользователя из системы")
    public Response deleteUser(UserData userData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithAccessToken(userData))
                        .delete(CRUD_USER);

        return response;
    }

    @Step("Редактирование данных пользователя в системе")
    public Response editUser(UserData userData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithAccessToken(userData))
                        .body(userData)
                        .patch(CRUD_USER);

        return response;
    }
}
