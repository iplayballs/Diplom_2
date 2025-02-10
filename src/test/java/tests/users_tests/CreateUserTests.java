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

public class CreateUserTests extends BaseTestAfterDeleteUser {

    @DisplayName("Создание пользователя с обязательными полями: name, email, password")
    @Description("Проверяем код ответа создания пользователя 200 и содержание body ответа accessToken, refreshToken")
    @Test
    public void testCreateUserWithRequiredFields(){

        userData = new UserData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        Response response = userApi.registerUser(userData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("В теле ответа не содержится AccessToken", response.jsonPath().getString("accessToken"), notNullValue());
        assertThat("В теле ответа не содержится RefreshToken", response.jsonPath().getString("refreshToken"), notNullValue());
    }

    @DisplayName("Создание пользователя с уже существующими в системе полями: name, email, password")
    @Description("Проверяем код ответа создания пользователя 403 и содержание body ответа содержание body ответа \"message\": \"User already exists\"")
    @Test
    public void testCreateUserExistInSystem(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);
        Response response = userApi.registerUser(userData);

        assertThat("Код ответа должен быть 403, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("User already exists"));
    }

    @DisplayName("Создание пользователя с обязательными полями: email, password")
    @Description("Проверяем код ответа создания пользователя 403 и содержание body ответа \"message\": \"Email, password and name are required fields\"")
    @Test
    public void testCreateUserWithoutRequiredFieldName(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        Response response = userApi.registerUser(userData);

        assertThat("Код ответа должен быть 403, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя с обязательными полями: name, password")
    @Description("Проверяем код ответа создания пользователя 403 и содержание body ответа \"message\": \"Email, password and name are required fields\"")
    @Test
    public void testCreateUserWithoutRequiredFieldEmail(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setPassword(UserDataGenerate.generatePassword());

        Response response = userApi.registerUser(userData);

        assertThat("Код ответа должен быть 403, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя с обязательными полями: name, email")
    @Description("Проверяем код ответа создания пользователя 403 и содержание body ответа \"message\": \"Email, password and name are required fields\"")
    @Test
    public void testCreateUserWithoutRequiredFieldPassword(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());

        Response response = userApi.registerUser(userData);

        assertThat("Код ответа должен быть 403, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("Email, password and name are required fields"));
    }
}
