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

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EditUserTests extends BaseTestAfterDeleteUser {

    @DisplayName("Изменение имени пользователя существующего в системе")
    @Description("Проверяем код ответа изменения имени пользователя 200 и содержание body ответа нового name")
    @Test
    public void testEditNameUserExistWithRightAccessToken(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);

        userData.setName(UserDataGenerate.generateName());
        Response response = userApi.editUser(userData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("В теле ответа должно прийти новое \"name\": \"" + userData.getName() + "\", пришло "  + response.jsonPath().getString("user.name"), response.jsonPath().getString("user.name"), equalTo(userData.getName()));
    }

    @DisplayName("Изменение email пользователя существующего в системе")
    @Description("Проверяем код ответа изменения email пользователя 200 и содержание body ответа нового email")
    @Test
    public void testEditEmailUserExistWithRightAccessToken(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);

        userData.setEmail(UserDataGenerate.generateEmail());
        Response response = userApi.editUser(userData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("В теле ответа должно прийти новое \"email\": \"" + userData.getEmail() + "\", пришло "  + response.jsonPath().getString("user.email"), response.jsonPath().getString("user.email"), equalTo(userData.getEmail().toLowerCase(Locale.ROOT)));
    }

    @DisplayName("Изменение имени пользователя существующего в системе")
    @Description("Проверяем код ответа изменения имени пользователя 401 и \"message\": \"You should be authorised\"")
    @Test
    public void testEditNameUserExistWithoutAccessToken(){

        userData = new UserData();
        userApi = new UserApi();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);

        userData.setName(UserDataGenerate.generateName());
        String rightAccessToken = userData.getAccessToken();
        userData.setAccessToken("");
        Response response = userApi.editUser(userData);

        assertThat("Код ответа должен быть 401, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("В теле ответа должно прийти \"message\": \"Email, password and name are required fields\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("You should be authorised"));

        userData.setAccessToken(rightAccessToken);
    }
}
