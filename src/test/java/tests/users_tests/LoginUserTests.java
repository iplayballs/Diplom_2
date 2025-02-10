package tests.users_tests;

import config.BaseTestAfterDeleteUser;
import data.UserData;
import data.UserDataGenerate;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import requests.UserApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTests extends BaseTestAfterDeleteUser {

    @DisplayName("Авторизация пользователя существующего в системе")
    @Description("Проверяем код ответа авторизации пользователя 200 и содержание body ответа accessToken, refreshToken")
    @Test
    public void testLoginUserInSystem(){

        userData = new UserData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);
        Response response = userApi.loginUser(userData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("В теле ответа не содержится AccessToken", response.jsonPath().getString("accessToken"), notNullValue());
        assertThat("В теле ответа не содержится RefreshToken", response.jsonPath().getString("refreshToken"), notNullValue());
    }

    @DisplayName("Авторизация пользователя существующего в системе с неверным email")
    @Description("Проверяем код ответа авторизации пользователя 401 и содержание body ответа \"message\": \"email or password are incorrect\"")
    @Test
    public void testLoginUserWithWrongEmail(){

        userData = new UserData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);
        String rightAccessToken = userData.getAccessToken();
        userData.setEmail(UserDataGenerate.generateEmail());
        Response response = userApi.loginUser(userData);

        assertThat("Код ответа должен быть 401, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("email or password are incorrect"));

        userData.setAccessToken(rightAccessToken);
    }

    @DisplayName("Авторизация пользователя существующего в системе с неверным паролем")
    @Description("Проверяем код ответа авторизации пользователя 401 и содержание body ответа \"message\": \"email or password are incorrect\"")
    @Test
    public void testLoginUserWithWrongPassword(){

        userData = new UserData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);
        String rightAccessToken = userData.getAccessToken();

        userData.setEmail(UserDataGenerate.generateEmail());
        Response response = userApi.loginUser(userData);

        assertThat("Код ответа должен быть 401, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("email or password are incorrect"));

        userData.setAccessToken(rightAccessToken);
    }
}
