package config;

import data.UserData;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import requests.UserApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class BaseTestAfterDeleteUser {

    protected UserApi userApi;
    protected UserData userData;

    @After
    public void deleteUserAfterTest() {
        Response response = null;
        try {
            response = userApi.deleteUser(userData);
            assertThat("Код ответа должен быть 202, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_ACCEPTED));
        } catch (IllegalArgumentException e) {
            System.out.println("Нельзя удалить пользователя, отсуствует accessToken");
        } catch (Exception e) {
            System.out.println("Нельзя удалить несуществующего пользователя");
        }
    }
}
