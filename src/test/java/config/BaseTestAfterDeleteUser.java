package config;

import data.UserData;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import requests.OrderApi;
import requests.UserApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class BaseTestAfterDeleteUser {

    protected UserApi userApi = new UserApi();
    protected UserData userData;
    protected OrderApi orderApi = new OrderApi();

    @After
    public void deleteUserAfterTest() {

        try {
            Response response = userApi.deleteUser(userData);
            assertThat("Код ответа должен быть 202, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_ACCEPTED));
        } catch (IllegalArgumentException e) {
            System.out.println("Нельзя удалить пользователя, отсуствует accessToken");
        } catch (Exception e) {
            System.out.println("Нельзя удалить несуществующего пользователя");
        }
    }
}
